//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.PlayerJoinEvent;
import com.gamesense.api.event.events.PlayerLeaveEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.hud.ColorMain;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

public class LogoutSpots extends Module {
  Map<Entity, String> loggedPlayers;
  
  List<Entity> lastTickEntities;
  
  @EventHandler
  private final Listener<PlayerJoinEvent> listener1;
  
  @EventHandler
  private final Listener<PlayerLeaveEvent> listener2;
  
  @EventHandler
  private final Listener<WorldEvent.Unload> listener3;
  
  @EventHandler
  private final Listener<WorldEvent.Load> listener4;
  
  public LogoutSpots() {
    super("LogoutSpots", Module.Category.Render);
    this.loggedPlayers = new ConcurrentHashMap<>();
    this.listener1 = new Listener(event -> this.loggedPlayers.forEach(()), new java.util.function.Predicate[0]);
    this.listener2 = new Listener(event -> {
          if (mc.world == null)
            return; 
          this.lastTickEntities.forEach(());
        }new java.util.function.Predicate[0]);
    this.listener3 = new Listener(event -> {
          this.lastTickEntities.clear();
          if (mc.player == null) {
            this.loggedPlayers.clear();
          } else if (!mc.player.isDead) {
            this.loggedPlayers.clear();
          } 
        }new java.util.function.Predicate[0]);
    this.listener4 = new Listener(event -> {
          this.lastTickEntities.clear();
          if (mc.player == null) {
            this.loggedPlayers.clear();
          } else if (!mc.player.isDead) {
            this.loggedPlayers.clear();
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void onUpdate() {
    this.lastTickEntities = mc.world.loadedEntityList;
  }
  
  public void onWorldRender(RenderEvent event) {
    this.loggedPlayers.forEach((e, time) -> {
          if (mc.player.getDistance(e) < 500.0F) {
            GL11.glPushMatrix();
            drawLogoutBox(e.getRenderBoundingBox(), 1, 0, 0, 0, 255);
            drawNametag(e, time);
            GL11.glPopMatrix();
          } 
        });
  }
  
  public void drawLogoutBox(AxisAlignedBB bb, int width, int r, int b, int g, int a) {
    Color color;
    ColorMain colorMain = (ColorMain)ModuleManager.getModuleByName("Colors");
    Color c = Rainbow.getColor();
    if (ColorMain.rainbow.getValue()) {
      color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
    } else {
      color = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 255);
    } 
    GameSenseTessellator.drawBoundingBox(bb, width, color.getRGB());
  }
  
  public void onEnable() {
    this.lastTickEntities = new ArrayList<>();
    this.loggedPlayers.clear();
    GameSenseMod.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    GameSenseMod.EVENT_BUS.unsubscribe(this);
  }
  
  private void drawNametag(Entity entityIn, String t) {
    float mm;
    Color color;
    GlStateManager.pushMatrix();
    float f = mc.player.getDistance(entityIn);
    float sc = (f < 25.0F) ? 0.5F : 2.0F;
    float m = f / 20.0F * (float)Math.pow(1.258925437927246D, 0.1D / sc);
    if (m < 0.5F)
      m = 0.5F; 
    if (m > 5.0F)
      m = 5.0F; 
    Vec3d interp = getInterpolatedRenderPos(entityIn, mc.getRenderPartialTicks());
    if (m > 2.0F) {
      mm = m / 2.0F;
    } else {
      mm = m;
    } 
    float yAdd = entityIn.height + mm;
    double x = interp.x;
    double y = interp.y + yAdd;
    double z = interp.z;
    float viewerYaw = (mc.getRenderManager()).playerViewY;
    float viewerPitch = (mc.getRenderManager()).playerViewX;
    boolean isThirdPersonFrontal = ((mc.getRenderManager()).options.thirdPersonView == 2);
    GlStateManager.translate(x, y, z);
    GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((isThirdPersonFrontal ? -1 : true) * viewerPitch, 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(m, m, m);
    FontRenderer fontRendererIn = mc.fontRenderer;
    GlStateManager.scale(-0.025F, -0.025F, 0.025F);
    String line1 = entityIn.getName() + "  (" + t + ")";
    String line2 = "x" + entityIn.getPosition().getX() + " y" + entityIn.getPosition().getY() + " z" + entityIn.getPosition().getZ();
    int i = fontRendererIn.getStringWidth(line1) / 2;
    int ii = fontRendererIn.getStringWidth(line2) / 2;
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.enableTexture2D();
    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
    ColorMain colorMain = (ColorMain)ModuleManager.getModuleByName("Colors");
    Color c = Rainbow.getColor();
    if (ColorMain.rainbow.getValue()) {
      color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
    } else {
      color = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 255);
    } 
    fontRendererIn.drawStringWithShadow(line1, -i, 10.0F, color.getRGB());
    fontRendererIn.drawStringWithShadow(line2, -ii, 20.0F, color.getRGB());
    GlStateManager.glNormal3f(0.0F, 0.0F, 0.0F);
    GlStateManager.disableDepth();
    GlStateManager.disableTexture2D();
    GlStateManager.popMatrix();
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
  }
  
  public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
    return getInterpolatedPos(entity, ticks).subtract((mc.getRenderManager()).renderPosX, (mc.getRenderManager()).renderPosY, (mc.getRenderManager()).renderPosZ);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
    return getInterpolatedAmount(entity, ticks, ticks, ticks);
  }
}
