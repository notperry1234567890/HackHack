package org.spongepowered.asm.mixin.transformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinErrorHandler;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.ArgsClassGenerator;
import org.spongepowered.asm.mixin.throwables.ClassAlreadyLoadedException;
import org.spongepowered.asm.mixin.throwables.MixinApplyError;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.mixin.throwables.MixinPrepareError;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckInterfaces;
import org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionClassExporter;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.ITransformer;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.transformers.TreeTransformer;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.util.ReEntranceLock;
import org.spongepowered.asm.util.perf.Profiler;

public class MixinTransformer extends TreeTransformer {
  private static final String MIXIN_AGENT_CLASS = "org.spongepowered.tools.agent.MixinAgent";
  
  private static final String METRONOME_AGENT_CLASS = "org.spongepowered.metronome.Agent";
  
  enum ErrorPhase {
    PREPARE {
      IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler handler, String context, InvalidMixinException ex, IMixinInfo mixin, IMixinErrorHandler.ErrorAction action) {
        try {
          return handler.onPrepareError(mixin.getConfig(), (Throwable)ex, mixin, action);
        } catch (AbstractMethodError ame) {
          return action;
        } 
      }
      
      protected String getContext(IMixinInfo mixin, String context) {
        return String.format("preparing %s in %s", new Object[] { mixin.getName(), context });
      }
    },
    APPLY {
      IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler handler, String context, InvalidMixinException ex, IMixinInfo mixin, IMixinErrorHandler.ErrorAction action) {
        try {
          return handler.onApplyError(context, (Throwable)ex, mixin, action);
        } catch (AbstractMethodError ame) {
          return action;
        } 
      }
      
      protected String getContext(IMixinInfo mixin, String context) {
        return String.format("%s -> %s", new Object[] { mixin, context });
      }
    };
    
    private final String text;
    
    ErrorPhase() {
      this.text = name().toLowerCase();
    }
    
    public String getLogMessage(String context, InvalidMixinException ex, IMixinInfo mixin) {
      return String.format("Mixin %s failed %s: %s %s", new Object[] { this.text, getContext(mixin, context), ex.getClass().getName(), ex.getMessage() });
    }
    
    public String getErrorMessage(IMixinInfo mixin, IMixinConfig config, MixinEnvironment.Phase phase) {
      return String.format("Mixin [%s] from phase [%s] in config [%s] FAILED during %s", new Object[] { mixin, phase, config, name() });
    }
    
    abstract IMixinErrorHandler.ErrorAction onError(IMixinErrorHandler param1IMixinErrorHandler, String param1String, InvalidMixinException param1InvalidMixinException, IMixinInfo param1IMixinInfo, IMixinErrorHandler.ErrorAction param1ErrorAction);
    
    protected abstract String getContext(IMixinInfo param1IMixinInfo, String param1String);
  }
  
  static final Logger logger = LogManager.getLogger("mixin");
  
  private final IMixinService service = MixinService.getService();
  
  private final List<MixinConfig> configs = new ArrayList<MixinConfig>();
  
  private final List<MixinConfig> pendingConfigs = new ArrayList<MixinConfig>();
  
  private final ReEntranceLock lock;
  
  private final String sessionId = UUID.randomUUID().toString();
  
  private final Extensions extensions;
  
  private final IHotSwap hotSwapper;
  
  private final MixinPostProcessor postProcessor;
  
  private final Profiler profiler;
  
  private MixinEnvironment currentEnvironment;
  
  private Level verboseLoggingLevel = Level.DEBUG;
  
  private boolean errorState = false;
  
  private int transformedCount = 0;
  
  MixinTransformer() {
    MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
    Object globalMixinTransformer = environment.getActiveTransformer();
    if (globalMixinTransformer instanceof ITransformer)
      throw new MixinException("Terminating MixinTransformer instance " + this); 
    environment.setActiveTransformer((ITransformer)this);
    this.lock = this.service.getReEntranceLock();
    this.extensions = new Extensions(this);
    this.hotSwapper = initHotSwapper(environment);
    this.postProcessor = new MixinPostProcessor();
    this.extensions.add((IClassGenerator)new ArgsClassGenerator());
    this.extensions.add(new InnerClassGenerator());
    this.extensions.add((IExtension)new ExtensionClassExporter(environment));
    this.extensions.add((IExtension)new ExtensionCheckClass());
    this.extensions.add((IExtension)new ExtensionCheckInterfaces());
    this.profiler = MixinEnvironment.getProfiler();
  }
  
  private IHotSwap initHotSwapper(MixinEnvironment environment) {
    if (!environment.getOption(MixinEnvironment.Option.HOT_SWAP))
      return null; 
    try {
      logger.info("Attempting to load Hot-Swap agent");
      Class<? extends IHotSwap> clazz = (Class)Class.forName("org.spongepowered.tools.agent.MixinAgent");
      Constructor<? extends IHotSwap> ctor = clazz.getDeclaredConstructor(new Class[] { MixinTransformer.class });
      return ctor.newInstance(new Object[] { this });
    } catch (Throwable th) {
      logger.info("Hot-swap agent could not be loaded, hot swapping of mixins won't work. {}: {}", new Object[] { th
            .getClass().getSimpleName(), th.getMessage() });
      return null;
    } 
  }
  
  public void audit(MixinEnvironment environment) {
    Set<String> unhandled = new HashSet<String>();
    for (MixinConfig config : this.configs)
      unhandled.addAll(config.getUnhandledTargets()); 
    Logger auditLogger = LogManager.getLogger("mixin/audit");
    for (String target : unhandled) {
      try {
        auditLogger.info("Force-loading class {}", new Object[] { target });
        this.service.getClassProvider().findClass(target, true);
      } catch (ClassNotFoundException ex) {
        auditLogger.error("Could not force-load " + target, ex);
      } 
    } 
    for (MixinConfig config : this.configs) {
      for (String target : config.getUnhandledTargets()) {
        ClassAlreadyLoadedException ex = new ClassAlreadyLoadedException(target + " was already classloaded");
        auditLogger.error("Could not force-load " + target, (Throwable)ex);
      } 
    } 
    if (environment.getOption(MixinEnvironment.Option.DEBUG_PROFILER))
      printProfilerSummary(); 
  }
  
  private void printProfilerSummary() {
    DecimalFormat threedp = new DecimalFormat("(###0.000");
    DecimalFormat onedp = new DecimalFormat("(###0.0");
    PrettyPrinter printer = this.profiler.printer(false, false);
    long prepareTime = this.profiler.get("mixin.prepare").getTotalTime();
    long readTime = this.profiler.get("mixin.read").getTotalTime();
    long applyTime = this.profiler.get("mixin.apply").getTotalTime();
    long writeTime = this.profiler.get("mixin.write").getTotalTime();
    long totalMixinTime = this.profiler.get("mixin").getTotalTime();
    long loadTime = this.profiler.get("class.load").getTotalTime();
    long transformTime = this.profiler.get("class.transform").getTotalTime();
    long exportTime = this.profiler.get("mixin.debug.export").getTotalTime();
    long actualTime = totalMixinTime - loadTime - transformTime - exportTime;
    double timeSliceMixin = actualTime / totalMixinTime * 100.0D;
    double timeSliceLoad = loadTime / totalMixinTime * 100.0D;
    double timeSliceTransform = transformTime / totalMixinTime * 100.0D;
    double timeSliceExport = exportTime / totalMixinTime * 100.0D;
    long worstTransformerTime = 0L;
    Profiler.Section worstTransformer = null;
    for (Profiler.Section section : this.profiler.getSections()) {
      long transformerTime = section.getName().startsWith("class.transform.") ? section.getTotalTime() : 0L;
      if (transformerTime > worstTransformerTime) {
        worstTransformerTime = transformerTime;
        worstTransformer = section;
      } 
    } 
    printer.hr().add("Summary").hr().add();
    String format = "%9d ms %12s seconds)";
    printer.kv("Total mixin time", format, new Object[] { Long.valueOf(totalMixinTime), threedp.format(totalMixinTime * 0.001D) }).add();
    printer.kv("Preparing mixins", format, new Object[] { Long.valueOf(prepareTime), threedp.format(prepareTime * 0.001D) });
    printer.kv("Reading input", format, new Object[] { Long.valueOf(readTime), threedp.format(readTime * 0.001D) });
    printer.kv("Applying mixins", format, new Object[] { Long.valueOf(applyTime), threedp.format(applyTime * 0.001D) });
    printer.kv("Writing output", format, new Object[] { Long.valueOf(writeTime), threedp.format(writeTime * 0.001D) }).add();
    printer.kv("of which", "");
    printer.kv("Time spent loading from disk", format, new Object[] { Long.valueOf(loadTime), threedp.format(loadTime * 0.001D) });
    printer.kv("Time spent transforming classes", format, new Object[] { Long.valueOf(transformTime), threedp.format(transformTime * 0.001D) }).add();
    if (worstTransformer != null) {
      printer.kv("Worst transformer", worstTransformer.getName());
      printer.kv("Class", worstTransformer.getInfo());
      printer.kv("Time spent", "%s seconds", new Object[] { Double.valueOf(worstTransformer.getTotalSeconds()) });
      printer.kv("called", "%d times", new Object[] { Integer.valueOf(worstTransformer.getTotalCount()) }).add();
    } 
    printer.kv("   Time allocation:     Processing mixins", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(actualTime), onedp.format(timeSliceMixin) });
    printer.kv("Loading classes", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(loadTime), onedp.format(timeSliceLoad) });
    printer.kv("Running transformers", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(transformTime), onedp.format(timeSliceTransform) });
    if (exportTime > 0L)
      printer.kv("Exporting classes (debug)", "%9d ms %10s%% of total)", new Object[] { Long.valueOf(exportTime), onedp.format(timeSliceExport) }); 
    printer.add();
    try {
      Class<?> agent = this.service.getClassProvider().findAgentClass("org.spongepowered.metronome.Agent", false);
      Method mdGetTimes = agent.getDeclaredMethod("getTimes", new Class[0]);
      Map<String, Long> times = (Map<String, Long>)mdGetTimes.invoke(null, new Object[0]);
      printer.hr().add("Transformer Times").hr().add();
      int longest = 10;
      for (Map.Entry<String, Long> entry : times.entrySet())
        longest = Math.max(longest, ((String)entry.getKey()).length()); 
      for (Map.Entry<String, Long> entry : times.entrySet()) {
        String name = entry.getKey();
        long mixinTime = 0L;
        for (Profiler.Section section : this.profiler.getSections()) {
          if (name.equals(section.getInfo())) {
            mixinTime = section.getTotalTime();
            break;
          } 
        } 
        if (mixinTime > 0L) {
          printer.add("%-" + longest + "s %8s ms %8s ms in mixin)", new Object[] { name, Long.valueOf(((Long)entry.getValue()).longValue() + mixinTime), "(" + mixinTime });
          continue;
        } 
        printer.add("%-" + longest + "s %8s ms", new Object[] { name, entry.getValue() });
      } 
      printer.add();
    } catch (Throwable throwable) {}
    printer.print();
  }
  
  public String getName() {
    return getClass().getName();
  }
  
  public boolean isDelegationExcluded() {
    return true;
  }
  
  public synchronized byte[] transformClassBytes(String name, String transformedName, byte[] basicClass) {
    if (transformedName == null || this.errorState)
      return basicClass; 
    MixinEnvironment environment = MixinEnvironment.getCurrentEnvironment();
    if (basicClass == null) {
      for (IClassGenerator generator : this.extensions.getGenerators()) {
        Profiler.Section genTimer = this.profiler.begin(new String[] { "generator", generator.getClass().getSimpleName().toLowerCase() });
        basicClass = generator.generate(transformedName);
        genTimer.end();
        if (basicClass != null) {
          this.extensions.export(environment, transformedName.replace('.', '/'), false, basicClass);
          return basicClass;
        } 
      } 
      return basicClass;
    } 
    boolean locked = this.lock.push().check();
    Profiler.Section mixinTimer = this.profiler.begin("mixin");
    if (!locked)
      try {
        checkSelect(environment);
      } catch (Exception ex) {
        this.lock.pop();
        mixinTimer.end();
        throw new MixinException(ex);
      }  
    try {
      if (this.postProcessor.canTransform(transformedName)) {
        Profiler.Section postTimer = this.profiler.begin("postprocessor");
        byte[] bytes = this.postProcessor.transformClassBytes(name, transformedName, basicClass);
        postTimer.end();
        this.extensions.export(environment, transformedName, false, bytes);
        return bytes;
      } 
      SortedSet<MixinInfo> mixins = null;
      boolean invalidRef = false;
      for (MixinConfig config : this.configs) {
        if (config.packageMatch(transformedName)) {
          invalidRef = true;
          continue;
        } 
        if (config.hasMixinsFor(transformedName)) {
          if (mixins == null)
            mixins = new TreeSet<MixinInfo>(); 
          mixins.addAll(config.getMixinsFor(transformedName));
        } 
      } 
      if (invalidRef)
        throw new NoClassDefFoundError(String.format("%s is a mixin class and cannot be referenced directly", new Object[] { transformedName })); 
      if (mixins != null) {
        if (locked) {
          logger.warn("Re-entrance detected, this will cause serious problems.", (Throwable)new MixinException());
          throw new MixinApplyError("Re-entrance error.");
        } 
        if (this.hotSwapper != null)
          this.hotSwapper.registerTargetClass(transformedName, basicClass); 
        try {
          Profiler.Section timer = this.profiler.begin("read");
          ClassNode targetClassNode = readClass(basicClass, true);
          TargetClassContext context = new TargetClassContext(environment, this.extensions, this.sessionId, transformedName, targetClassNode, mixins);
          timer.end();
          basicClass = applyMixins(environment, context);
          this.transformedCount++;
        } catch (InvalidMixinException th) {
          dumpClassOnFailure(transformedName, basicClass, environment);
          handleMixinApplyError(transformedName, th, environment);
        } 
      } 
      return basicClass;
    } catch (Throwable th) {
      th.printStackTrace();
      dumpClassOnFailure(transformedName, basicClass, environment);
      throw new MixinTransformerError("An unexpected critical error was encountered", th);
    } finally {
      this.lock.pop();
      mixinTimer.end();
    } 
  }
  
  public List<String> reload(String mixinClass, byte[] bytes) {
    if (this.lock.getDepth() > 0)
      throw new MixinApplyError("Cannot reload mixin if re-entrant lock entered"); 
    List<String> targets = new ArrayList<String>();
    for (MixinConfig config : this.configs)
      targets.addAll(config.reloadMixin(mixinClass, bytes)); 
    return targets;
  }
  
  private void checkSelect(MixinEnvironment environment) {
    if (this.currentEnvironment != environment) {
      select(environment);
      return;
    } 
    int unvisitedCount = Mixins.getUnvisitedCount();
    if (unvisitedCount > 0 && this.transformedCount == 0)
      select(environment); 
  }
  
  private void select(MixinEnvironment environment) {
    this.verboseLoggingLevel = environment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE) ? Level.INFO : Level.DEBUG;
    if (this.transformedCount > 0)
      logger.log(this.verboseLoggingLevel, "Ending {}, applied {} mixins", new Object[] { this.currentEnvironment, Integer.valueOf(this.transformedCount) }); 
    String action = (this.currentEnvironment == environment) ? "Checking for additional" : "Preparing";
    logger.log(this.verboseLoggingLevel, "{} mixins for {}", new Object[] { action, environment });
    this.profiler.setActive(true);
    this.profiler.mark(environment.getPhase().toString() + ":prepare");
    Profiler.Section prepareTimer = this.profiler.begin("prepare");
    selectConfigs(environment);
    this.extensions.select(environment);
    int totalMixins = prepareConfigs(environment);
    this.currentEnvironment = environment;
    this.transformedCount = 0;
    prepareTimer.end();
    long elapsedMs = prepareTimer.getTime();
    double elapsedTime = prepareTimer.getSeconds();
    if (elapsedTime > 0.25D) {
      long loadTime = this.profiler.get("class.load").getTime();
      long transformTime = this.profiler.get("class.transform").getTime();
      long pluginTime = this.profiler.get("mixin.plugin").getTime();
      String elapsed = (new DecimalFormat("###0.000")).format(elapsedTime);
      String perMixinTime = (new DecimalFormat("###0.0")).format(elapsedMs / totalMixins);
      logger.log(this.verboseLoggingLevel, "Prepared {} mixins in {} sec ({}ms avg) ({}ms load, {}ms transform, {}ms plugin)", new Object[] { Integer.valueOf(totalMixins), elapsed, perMixinTime, Long.valueOf(loadTime), Long.valueOf(transformTime), Long.valueOf(pluginTime) });
    } 
    this.profiler.mark(environment.getPhase().toString() + ":apply");
    this.profiler.setActive(environment.getOption(MixinEnvironment.Option.DEBUG_PROFILER));
  }
  
  private void selectConfigs(MixinEnvironment environment) {
    for (Iterator<Config> iter = Mixins.getConfigs().iterator(); iter.hasNext(); ) {
      Config handle = iter.next();
      try {
        MixinConfig config = handle.get();
        if (config.select(environment)) {
          iter.remove();
          logger.log(this.verboseLoggingLevel, "Selecting config {}", new Object[] { config });
          config.onSelect();
          this.pendingConfigs.add(config);
        } 
      } catch (Exception ex) {
        logger.warn(String.format("Failed to select mixin config: %s", new Object[] { handle }), ex);
      } 
    } 
    Collections.sort(this.pendingConfigs);
  }
  
  private int prepareConfigs(MixinEnvironment environment) {
    int totalMixins = 0;
    final IHotSwap hotSwapper = this.hotSwapper;
    for (MixinConfig config : this.pendingConfigs) {
      config.addListener(this.postProcessor);
      if (hotSwapper != null)
        config.addListener(new MixinConfig.IListener() {
              public void onPrepare(MixinInfo mixin) {
                hotSwapper.registerMixinClass(mixin.getClassName());
              }
              
              public void onInit(MixinInfo mixin) {}
            }); 
    } 
    for (MixinConfig config : this.pendingConfigs) {
      try {
        logger.log(this.verboseLoggingLevel, "Preparing {} ({})", new Object[] { config, Integer.valueOf(config.getDeclaredMixinCount()) });
        config.prepare();
        totalMixins += config.getMixinCount();
      } catch (InvalidMixinException ex) {
        handleMixinPrepareError(config, ex, environment);
      } catch (Exception ex) {
        String message = ex.getMessage();
        logger.error("Error encountered whilst initialising mixin config '" + config.getName() + "': " + message, ex);
      } 
    } 
    for (MixinConfig config : this.pendingConfigs) {
      IMixinConfigPlugin plugin = config.getPlugin();
      if (plugin == null)
        continue; 
      Set<String> otherTargets = new HashSet<String>();
      for (MixinConfig otherConfig : this.pendingConfigs) {
        if (!otherConfig.equals(config))
          otherTargets.addAll(otherConfig.getTargets()); 
      } 
      plugin.acceptTargets(config.getTargets(), Collections.unmodifiableSet(otherTargets));
    } 
    for (MixinConfig config : this.pendingConfigs) {
      try {
        config.postInitialise();
      } catch (InvalidMixinException ex) {
        handleMixinPrepareError(config, ex, environment);
      } catch (Exception ex) {
        String message = ex.getMessage();
        logger.error("Error encountered during mixin config postInit step'" + config.getName() + "': " + message, ex);
      } 
    } 
    this.configs.addAll(this.pendingConfigs);
    Collections.sort(this.configs);
    this.pendingConfigs.clear();
    return totalMixins;
  }
  
  private byte[] applyMixins(MixinEnvironment environment, TargetClassContext context) {
    Profiler.Section timer = this.profiler.begin("preapply");
    this.extensions.preApply(context);
    timer = timer.next("apply");
    apply(context);
    timer = timer.next("postapply");
    try {
      this.extensions.postApply(context);
    } catch (org.spongepowered.asm.mixin.transformer.ext.extensions.ExtensionCheckClass.ValidationFailedException ex) {
      logger.info(ex.getMessage());
      if (context.isExportForced() || environment.getOption(MixinEnvironment.Option.DEBUG_EXPORT))
        writeClass(context); 
    } 
    timer.end();
    return writeClass(context);
  }
  
  private void apply(TargetClassContext context) {
    context.applyMixins();
  }
  
  private void handleMixinPrepareError(MixinConfig config, InvalidMixinException ex, MixinEnvironment environment) throws MixinPrepareError {
    handleMixinError(config.getName(), ex, environment, ErrorPhase.PREPARE);
  }
  
  private void handleMixinApplyError(String targetClass, InvalidMixinException ex, MixinEnvironment environment) throws MixinApplyError {
    handleMixinError(targetClass, ex, environment, ErrorPhase.APPLY);
  }
  
  private void handleMixinError(String context, InvalidMixinException ex, MixinEnvironment environment, ErrorPhase errorPhase) throws Error {
    this.errorState = true;
    IMixinInfo mixin = ex.getMixin();
    if (mixin == null) {
      logger.error("InvalidMixinException has no mixin!", (Throwable)ex);
      throw ex;
    } 
    IMixinConfig config = mixin.getConfig();
    MixinEnvironment.Phase phase = mixin.getPhase();
    IMixinErrorHandler.ErrorAction action = config.isRequired() ? IMixinErrorHandler.ErrorAction.ERROR : IMixinErrorHandler.ErrorAction.WARN;
    if (environment.getOption(MixinEnvironment.Option.DEBUG_VERBOSE))
      (new PrettyPrinter())
        .add("Invalid Mixin").centre()
        .hr('-')
        .kvWidth(10)
        .kv("Action", errorPhase.name())
        .kv("Mixin", mixin.getClassName())
        .kv("Config", config.getName())
        .kv("Phase", phase)
        .hr('-')
        .add("    %s", new Object[] { ex.getClass().getName() }).hr('-')
        .addWrapped("    %s", new Object[] { ex.getMessage() }).hr('-')
        .add((Throwable)ex, 8)
        .trace(action.logLevel); 
    for (IMixinErrorHandler handler : getErrorHandlers(mixin.getPhase())) {
      IMixinErrorHandler.ErrorAction newAction = errorPhase.onError(handler, context, ex, mixin, action);
      if (newAction != null)
        action = newAction; 
    } 
    logger.log(action.logLevel, errorPhase.getLogMessage(context, ex, mixin), (Throwable)ex);
    this.errorState = false;
    if (action == IMixinErrorHandler.ErrorAction.ERROR)
      throw new MixinApplyError(errorPhase.getErrorMessage(mixin, config, phase), ex); 
  }
  
  private List<IMixinErrorHandler> getErrorHandlers(MixinEnvironment.Phase phase) {
    List<IMixinErrorHandler> handlers = new ArrayList<IMixinErrorHandler>();
    for (String handlerClassName : Mixins.getErrorHandlerClasses()) {
      try {
        logger.info("Instancing error handler class {}", new Object[] { handlerClassName });
        Class<?> handlerClass = this.service.getClassProvider().findClass(handlerClassName, true);
        IMixinErrorHandler handler = (IMixinErrorHandler)handlerClass.newInstance();
        if (handler != null)
          handlers.add(handler); 
      } catch (Throwable throwable) {}
    } 
    return handlers;
  }
  
  private byte[] writeClass(TargetClassContext context) {
    return writeClass(context.getClassName(), context.getClassNode(), context.isExportForced());
  }
  
  private byte[] writeClass(String transformedName, ClassNode targetClass, boolean forceExport) {
    Profiler.Section writeTimer = this.profiler.begin("write");
    byte[] bytes = writeClass(targetClass);
    writeTimer.end();
    this.extensions.export(this.currentEnvironment, transformedName, forceExport, bytes);
    return bytes;
  }
  
  private void dumpClassOnFailure(String className, byte[] bytes, MixinEnvironment env) {
    if (env.getOption(MixinEnvironment.Option.DUMP_TARGET_ON_FAILURE)) {
      ExtensionClassExporter exporter = (ExtensionClassExporter)this.extensions.getExtension(ExtensionClassExporter.class);
      exporter.dumpClass(className.replace('.', '/') + ".target", bytes);
    } 
  }
}
