package org.spongepowered.tools.agent;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.IHotSwap;
import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.service.IMixinService;
import org.spongepowered.asm.service.MixinService;

public class MixinAgent implements IHotSwap {
  class Transformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain domain, byte[] classfileBuffer) throws IllegalClassFormatException {
      if (classBeingRedefined == null)
        return null; 
      byte[] mixinBytecode = MixinAgent.classLoader.getFakeMixinBytecode(classBeingRedefined);
      if (mixinBytecode != null) {
        List<String> targets = reloadMixin(className, classfileBuffer);
        if (targets == null || !reApplyMixins(targets))
          return MixinAgent.ERROR_BYTECODE; 
        return mixinBytecode;
      } 
      try {
        MixinAgent.logger.info("Redefining class " + className);
        return MixinAgent.this.classTransformer.transformClassBytes(null, className, classfileBuffer);
      } catch (Throwable th) {
        MixinAgent.logger.error("Error while re-transforming class " + className, th);
        return MixinAgent.ERROR_BYTECODE;
      } 
    }
    
    private List<String> reloadMixin(String className, byte[] classfileBuffer) {
      MixinAgent.logger.info("Redefining mixin {}", new Object[] { className });
      try {
        return MixinAgent.this.classTransformer.reload(className.replace('/', '.'), classfileBuffer);
      } catch (MixinReloadException e) {
        MixinAgent.logger.error("Mixin {} cannot be reloaded, needs a restart to be applied: {} ", new Object[] { e.getMixinInfo(), e.getMessage() });
      } catch (Throwable th) {
        MixinAgent.logger.error("Error while finding targets for mixin " + className, th);
      } 
      return null;
    }
    
    private boolean reApplyMixins(List<String> targets) {
      IMixinService service = MixinService.getService();
      for (String target : targets) {
        String targetName = target.replace('/', '.');
        MixinAgent.logger.debug("Re-transforming target class {}", new Object[] { target });
        try {
          Class<?> targetClass = service.getClassProvider().findClass(targetName);
          byte[] targetBytecode = MixinAgent.classLoader.getOriginalTargetBytecode(targetName);
          if (targetBytecode == null) {
            MixinAgent.logger.error("Target class {} bytecode is not registered", new Object[] { targetName });
            return false;
          } 
          targetBytecode = MixinAgent.this.classTransformer.transformClassBytes(null, targetName, targetBytecode);
          MixinAgent.instrumentation.redefineClasses(new ClassDefinition[] { new ClassDefinition(targetClass, targetBytecode) });
        } catch (Throwable th) {
          MixinAgent.logger.error("Error while re-transforming target class " + target, th);
          return false;
        } 
      } 
      return true;
    }
  }
  
  public static final byte[] ERROR_BYTECODE = new byte[] { 1 };
  
  static final MixinAgentClassLoader classLoader = new MixinAgentClassLoader();
  
  static final Logger logger = LogManager.getLogger("mixin.agent");
  
  static Instrumentation instrumentation = null;
  
  private static List<MixinAgent> agents = new ArrayList<MixinAgent>();
  
  final MixinTransformer classTransformer;
  
  public MixinAgent(MixinTransformer classTransformer) {
    this.classTransformer = classTransformer;
    agents.add(this);
    if (instrumentation != null)
      initTransformer(); 
  }
  
  private void initTransformer() {
    instrumentation.addTransformer(new Transformer(), true);
  }
  
  public void registerMixinClass(String name) {
    classLoader.addMixinClass(name);
  }
  
  public void registerTargetClass(String name, byte[] bytecode) {
    classLoader.addTargetClass(name, bytecode);
  }
  
  public static void init(Instrumentation instrumentation) {
    MixinAgent.instrumentation = instrumentation;
    if (!MixinAgent.instrumentation.isRedefineClassesSupported())
      logger.error("The instrumentation doesn't support re-definition of classes"); 
    for (MixinAgent agent : agents)
      agent.initTransformer(); 
  }
  
  public static void premain(String arg, Instrumentation instrumentation) {
    System.setProperty("mixin.hotSwap", "true");
    init(instrumentation);
  }
  
  public static void agentmain(String arg, Instrumentation instrumentation) {
    init(instrumentation);
  }
}
