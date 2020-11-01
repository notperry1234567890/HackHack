//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.ColorMain;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
  Setting.Integer renderDistance;
  
  Setting.Mode pointsTo;
  
  int tracerColor;
  
  public Tracers() {
    super("Tracers", Module.Category.Render);
  }
  
  public void setup() {
    this.renderDistance = registerInteger("Distance", "Distance", 100, 10, 260);
    ArrayList<String> link = new ArrayList<>();
    link.add("Head");
    link.add("Feet");
    this.pointsTo = registerMode("Draw To", "DrawTo", link, "Feet");
  }
  
  public void onWorldRender(RenderEvent event) {
    mc.world.loadedEntityList.stream()
      .filter(e -> e instanceof net.minecraft.entity.player.EntityPlayer)
      .filter(e -> (e != mc.player))
      .forEach(e -> {
          if (mc.player.getDistance(e) > this.renderDistance.getValue())
            return; 
          if (Friends.isFriend(e.getName())) {
            this.tracerColor = ColorMain.getFriendColorInt();
          } else if (Enemies.isEnemy(e.getName())) {
            this.tracerColor = ColorMain.getEnemyColorInt();
          } else {
            if (mc.player.getDistance(e) < 20.0F)
              this.tracerColor = Color.RED.getRGB(); 
            if (mc.player.getDistance(e) >= 20.0F && mc.player.getDistance(e) < 50.0F)
              this.tracerColor = Color.YELLOW.getRGB(); 
            if (mc.player.getDistance(e) >= 50.0F)
              this.tracerColor = Color.GREEN.getRGB(); 
          } 
          if (this.pointsTo.getValue().equalsIgnoreCase("Head")) {
            drawLineToEntityPlayer(e, this.tracerColor, 255);
          } else if (this.pointsTo.getValue().equalsIgnoreCase("Feet")) {
            drawLineToEntityPlayer(e, this.tracerColor, 255);
          } 
        });
  }
  
  public void drawLineToEntityPlayer(Entity e, int rgb, int a) {
    double[] xyz = interpolate(e);
    int r = rgb >>> 16 & 0xFF;
    int g = rgb >>> 8 & 0xFF;
    int b = rgb & 0xFF;
    drawLine1(xyz[0], xyz[1], xyz[2], e.height, r, g, b, a);
  }
  
  public static double[] interpolate(Entity entity) {
    double posX = interpolate(entity.posX, entity.lastTickPosX) - (mc.getRenderManager()).renderPosX;
    double posY = interpolate(entity.posY, entity.lastTickPosY) - (mc.getRenderManager()).renderPosY;
    double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - (mc.getRenderManager()).renderPosZ;
    return new double[] { posX, posY, posZ };
  }
  
  public static double interpolate(double now, double then) {
    return then + (now - then) * mc.getRenderPartialTicks();
  }
  
  public void drawLine1(double posx, double posy, double posz, double up, float red, float green, float blue, float opacity) {
    Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float)Math.toRadians((Minecraft.getMinecraft()).player.rotationPitch))).rotateYaw(
        -((float)Math.toRadians((Minecraft.getMinecraft()).player.rotationYaw)));
    if (this.pointsTo.getValue().equalsIgnoreCase("Head")) {
      renderLine1(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    } else {
      renderLine2(eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, posx, posy, posz, up, red, green, blue, opacity);
    } 
  }
  
  public static void renderLine1(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
    GL11.glPushMatrix();
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(3042);
    GL11.glLineWidth(1.0F);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glColor4f(red, green, blue, opacity);
    GlStateManager.disableLighting();
    GL11.glLoadIdentity();
    mc.entityRenderer.orientCamera(mc.getRenderPartialTicks());
    GL11.glBegin(1);
    GL11.glVertex3d(posx, posy, posz);
    GL11.glVertex3d(posx2, posy2 + up, posz2);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glColor3d(1.0D, 1.0D, 1.0D);
    GlStateManager.enableLighting();
    GL11.glPopMatrix();
  }
  
  public static void renderLine2(double posx, double posy, double posz, double posx2, double posy2, double posz2, double up, float red, float green, float blue, float opacity) {
    GL11.glPushMatrix();
    GL11.glBlendFunc(770, 771);
    GL11.glEnable(3042);
    GL11.glLineWidth(1.0F);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glColor4f(red, green, blue, opacity);
    GlStateManager.disableLighting();
    GL11.glLoadIdentity();
    mc.entityRenderer.orientCamera(mc.getRenderPartialTicks());
    GL11.glBegin(1);
    GL11.glVertex3d(posx, posy, posz);
    GL11.glVertex3d(posx2, posy2, posz2);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glColor3d(1.0D, 1.0D, 1.0D);
    GlStateManager.enableLighting();
    GL11.glPopMatrix();
  }
}
