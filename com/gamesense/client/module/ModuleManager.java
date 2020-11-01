//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module;

import com.gamesense.api.event.events.Render3DEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.modules.combat.AutoArmor;
import com.gamesense.client.module.modules.combat.AutoCrystal;
import com.gamesense.client.module.modules.combat.AutoCrystalplus;
import com.gamesense.client.module.modules.combat.AutoFeetPlace;
import com.gamesense.client.module.modules.combat.AutoMend;
import com.gamesense.client.module.modules.combat.AutoTotem;
import com.gamesense.client.module.modules.combat.AutoTotemplus;
import com.gamesense.client.module.modules.combat.AutoTrap;
import com.gamesense.client.module.modules.combat.AutoWeb;
import com.gamesense.client.module.modules.combat.BedAura;
import com.gamesense.client.module.modules.combat.CrystalAura;
import com.gamesense.client.module.modules.combat.DesyncAura;
import com.gamesense.client.module.modules.combat.EXPFast;
import com.gamesense.client.module.modules.combat.FastBow;
import com.gamesense.client.module.modules.combat.HoleFill;
import com.gamesense.client.module.modules.combat.KillAura;
import com.gamesense.client.module.modules.combat.OffhandCrystal;
import com.gamesense.client.module.modules.combat.OffhandGap;
import com.gamesense.client.module.modules.combat.SelfTrap;
import com.gamesense.client.module.modules.combat.SelfWeb;
import com.gamesense.client.module.modules.exploits.CoordExploit;
import com.gamesense.client.module.modules.exploits.FastBreak;
import com.gamesense.client.module.modules.exploits.LiquidInteract;
import com.gamesense.client.module.modules.exploits.NoInteract;
import com.gamesense.client.module.modules.exploits.NoSwing;
import com.gamesense.client.module.modules.exploits.PortalGodMode;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.Compass;
import com.gamesense.client.module.modules.hud.Crystals;
import com.gamesense.client.module.modules.hud.HUD;
import com.gamesense.client.module.modules.hud.Homies;
import com.gamesense.client.module.modules.hud.Notifications;
import com.gamesense.client.module.modules.hud.TextRadar;
import com.gamesense.client.module.modules.misc.Announcer;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.gamesense.client.module.modules.misc.AutoReply;
import com.gamesense.client.module.modules.misc.AutoTool;
import com.gamesense.client.module.modules.misc.ChatModifier;
import com.gamesense.client.module.modules.misc.ChatSuffix;
import com.gamesense.client.module.modules.misc.CommandColor;
import com.gamesense.client.module.modules.misc.FakePlayer;
import com.gamesense.client.module.modules.misc.FastPlace;
import com.gamesense.client.module.modules.misc.LowArmor;
import com.gamesense.client.module.modules.misc.MCF;
import com.gamesense.client.module.modules.misc.MultiTask;
import com.gamesense.client.module.modules.misc.NoEntityTrace;
import com.gamesense.client.module.modules.misc.NoKick;
import com.gamesense.client.module.modules.misc.PvPInfo;
import com.gamesense.client.module.modules.misc.RPCModule;
import com.gamesense.client.module.modules.misc.TotemCounter;
import com.gamesense.client.module.modules.movement.Blink;
import com.gamesense.client.module.modules.movement.HoleTP;
import com.gamesense.client.module.modules.movement.PlayerTweaks;
import com.gamesense.client.module.modules.movement.ReverseStep;
import com.gamesense.client.module.modules.movement.Speed;
import com.gamesense.client.module.modules.movement.Sprint;
import com.gamesense.client.module.modules.movement.Step;
import com.gamesense.client.module.modules.render.BlockHighlight;
import com.gamesense.client.module.modules.render.CapesModule;
import com.gamesense.client.module.modules.render.Chams;
import com.gamesense.client.module.modules.render.ESP;
import com.gamesense.client.module.modules.render.EntityESP;
import com.gamesense.client.module.modules.render.Freecam;
import com.gamesense.client.module.modules.render.Fullbright;
import com.gamesense.client.module.modules.render.HoleESP;
import com.gamesense.client.module.modules.render.LogoutSpots;
import com.gamesense.client.module.modules.render.MobOwner;
import com.gamesense.client.module.modules.render.Nametags;
import com.gamesense.client.module.modules.render.NoRender;
import com.gamesense.client.module.modules.render.RenderTweaks;
import com.gamesense.client.module.modules.render.ShulkerViewer;
import com.gamesense.client.module.modules.render.Skeleton;
import com.gamesense.client.module.modules.render.StorageESP;
import com.gamesense.client.module.modules.render.Tracers;
import com.gamesense.client.module.modules.render.Trajectories;
import com.gamesense.client.module.modules.render.Trajectories2;
import com.gamesense.client.module.modules.render.ViewModel;
import com.gamesense.client.module.modules.render.VoidESP;
import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
  public static ArrayList<Module> modules;
  
  public ModuleManager() {
    modules = new ArrayList<>();
    addMod((Module)new AutoArmor());
    addMod((Module)new AutoCrystal());
    addMod((Module)new CrystalAura());
    addMod((Module)new AutoCrystalplus());
    addMod((Module)new BedAura());
    addMod((Module)new AutoFeetPlace());
    addMod((Module)new AutoTotem());
    addMod((Module)new AutoTotemplus());
    addMod((Module)new AutoTrap());
    addMod((Module)new AutoWeb());
    addMod((Module)new DesyncAura());
    addMod((Module)new FastBow());
    addMod((Module)new EXPFast());
    addMod((Module)new HoleFill());
    addMod((Module)new KillAura());
    addMod((Module)new OffhandCrystal());
    addMod((Module)new OffhandGap());
    addMod((Module)new SelfTrap());
    addMod((Module)new SelfWeb());
    addMod((Module)new AutoMend());
    addMod((Module)new CoordExploit());
    addMod((Module)new FastBreak());
    addMod((Module)new LiquidInteract());
    addMod((Module)new NoInteract());
    addMod((Module)new NoSwing());
    addMod((Module)new PortalGodMode());
    addMod((Module)new Blink());
    addMod((Module)new HoleTP());
    addMod((Module)new PlayerTweaks());
    addMod((Module)new ReverseStep());
    addMod((Module)new Speed());
    addMod((Module)new Sprint());
    addMod((Module)new Step());
    addMod((Module)new Announcer());
    addMod((Module)new AutoGG());
    addMod((Module)new AutoReply());
    addMod((Module)new AutoTool());
    addMod((Module)new ChatModifier());
    addMod((Module)new ChatSuffix());
    addMod((Module)new FastPlace());
    addMod((Module)new FakePlayer());
    addMod((Module)new MCF());
    addMod((Module)new MultiTask());
    addMod((Module)new NoEntityTrace());
    addMod((Module)new NoKick());
    addMod((Module)new PvPInfo());
    addMod((Module)new LowArmor());
    addMod((Module)new TotemCounter());
    addMod((Module)new CommandColor());
    addMod((Module)new RPCModule());
    addMod((Module)new BlockHighlight());
    addMod((Module)new CapesModule());
    addMod((Module)new ESP());
    addMod((Module)new Skeleton());
    addMod((Module)new EntityESP());
    addMod((Module)new Chams());
    addMod((Module)new Freecam());
    addMod((Module)new Fullbright());
    addMod((Module)new HoleESP());
    addMod((Module)new LogoutSpots());
    addMod((Module)new MobOwner());
    addMod((Module)new Nametags());
    addMod((Module)new NoRender());
    addMod((Module)new RenderTweaks());
    addMod((Module)new ShulkerViewer());
    addMod((Module)new StorageESP());
    addMod((Module)new Tracers());
    addMod((Module)new ViewModel());
    addMod((Module)new VoidESP());
    addMod((Module)new Trajectories());
    addMod((Module)new Trajectories2());
    addMod((Module)new ClickGuiModule());
    addMod((Module)new ColorMain());
    addMod((Module)new HUD());
    addMod((Module)new Notifications());
    addMod((Module)new TextRadar());
    addMod((Module)new Crystals());
    addMod((Module)new Compass());
    addMod((Module)new Homies());
  }
  
  public static void addMod(Module m) {
    modules.add(m);
  }
  
  public static void onUpdate() {
    modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
  }
  
  public static void onRender() {
    modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
  }
  
  public static void onRender3D(Render3DEvent event) {
    modules.stream().filter(Module::isEnabled).forEach(module -> module.onRender3D(event));
  }
  
  public static void onWorldRender(RenderWorldLastEvent event) {
    (Minecraft.getMinecraft()).profiler.startSection("gamesense");
    (Minecraft.getMinecraft()).profiler.startSection("setup");
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.shadeModel(7425);
    GlStateManager.disableDepth();
    GlStateManager.glLineWidth(1.0F);
    Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
    onRender3D(render3dEvent);
    Vec3d renderPos = getInterpolatedPos((Entity)(Minecraft.getMinecraft()).player, event.getPartialTicks());
    RenderEvent e = new RenderEvent((Tessellator)GameSenseTessellator.INSTANCE, renderPos, event.getPartialTicks());
    e.resetTranslation();
    (Minecraft.getMinecraft()).profiler.endSection();
    modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
          (Minecraft.getMinecraft()).profiler.startSection(module.getName());
          module.onWorldRender(e);
          (Minecraft.getMinecraft()).profiler.endSection();
        });
    (Minecraft.getMinecraft()).profiler.startSection("release");
    GlStateManager.glLineWidth(1.0F);
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.enableDepth();
    GlStateManager.enableCull();
    GameSenseTessellator.releaseGL();
    (Minecraft.getMinecraft()).profiler.endSection();
    (Minecraft.getMinecraft()).profiler.endSection();
  }
  
  public static ArrayList<Module> getModules() {
    return modules;
  }
  
  public static ArrayList<Module> getModulesInCategory(Module.Category c) {
    ArrayList<Module> list = (ArrayList<Module>)getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
    return list;
  }
  
  public static void onBind(int key) {
    if (key == 0 || key == 0)
      return; 
    modules.forEach(module -> {
          if (module.getBind() == key)
            module.toggle(); 
        });
  }
  
  public static Module getModuleByName(String name) {
    Module m = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    return m;
  }
  
  public static boolean isModuleEnabled(String name) {
    Module m = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    return m.isEnabled();
  }
  
  public static boolean isModuleEnabled(Module m) {
    return m.isEnabled();
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
    return getInterpolatedAmount(entity, ticks, ticks, ticks);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
}
