//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.Render3DEvent;
import com.gamesense.api.event.events.RenderEntityModelEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.EntityUtil;
import com.gamesense.api.util.PhobosRenderUtil;
import com.gamesense.client.module.Module;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Skeleton extends Module {
  Setting.Integer reds;
  
  Setting.Integer greens;
  
  Setting.Integer blues;
  
  Setting.Integer alphas;
  
  Setting.Double lineWidths;
  
  Setting.Boolean colorFriendss;
  
  Setting.Boolean invisibless;
  
  public Skeleton() {
    super("Skeleton", Module.Category.Render);
  }
  
  public void setup() {
    this.reds = registerInteger("Red", "RedS", 255, 0, 255);
    this.greens = registerInteger("Green", "GreenS", 255, 0, 255);
    this.blues = registerInteger("Blue", "BlueS", 255, 0, 255);
    this.alphas = registerInteger("Alpha", "AlphaS", 255, 0, 255);
    this.lineWidths = registerDouble("LineWidth", "LineWidthS", 1.5D, 0.10000000149011612D, 5.0D);
    this.colorFriendss = registerBoolean("Friends", "FriendsS", true);
    this.invisibless = registerBoolean("Invisibles", "InvisiblesS", false);
    this.rotationList = (Map)new HashMap<>();
    setInstance();
  }
  
  private void setInstance() {
    INSTANCE = this;
  }
  
  public static Skeleton getInstance() {
    if (INSTANCE == null)
      INSTANCE = new Skeleton(); 
    return INSTANCE;
  }
  
  public void onRender3D(Render3DEvent event) {
    PhobosRenderUtil.GLPre((float)this.lineWidths.getValue());
    for (EntityPlayer player : mc.world.playerEntities) {
      if (player != null && player != mc.getRenderViewEntity() && player.isEntityAlive() && !player.isPlayerSleeping() && (!player.isInvisible() || this.invisibless.getValue()) && this.rotationList.get(player) != null && mc.player.getDistanceSq((Entity)player) < 2500.0D)
        renderSkeleton(player, this.rotationList.get(player), EntityUtil.getColor((Entity)player, this.reds.getValue(), this.greens.getValue(), this.blues.getValue(), this.alphas.getValue(), this.colorFriendss.getValue())); 
    } 
    PhobosRenderUtil.GlPost();
  }
  
  public void onRenderModel(RenderEntityModelEvent event) {
    if (event.getStage() == 0 && event.entity instanceof EntityPlayer && event.modelBase instanceof ModelBiped) {
      ModelBiped biped = (ModelBiped)event.modelBase;
      float[][] rotations = PhobosRenderUtil.getBipedRotations(biped);
      EntityPlayer player = (EntityPlayer)event.entity;
      this.rotationList.put(player, rotations);
    } 
  }
  
  private void renderSkeleton(EntityPlayer player, float[][] rotations, Color color) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GlStateManager.pushMatrix();
    GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
    Vec3d interp = EntityUtil.getInterpolatedRenderPos((Entity)player, mc.getRenderPartialTicks());
    double pX = interp.x;
    double pY = interp.y;
    double pZ = interp.z;
    GlStateManager.translate(pX, pY, pZ);
    GlStateManager.rotate(-player.renderYawOffset, 0.0F, 1.0F, 0.0F);
    GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? -0.235D : 0.0D);
    float sneak = player.isSneaking() ? 0.6F : 0.75F;
    GlStateManager.pushMatrix();
    GlStateManager.translate(-0.125D, sneak, 0.0D);
    if (rotations[3][0] != 0.0F)
      GlStateManager.rotate(rotations[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F); 
    if (rotations[3][1] != 0.0F)
      GlStateManager.rotate(rotations[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F); 
    if (rotations[3][2] != 0.0F)
      GlStateManager.rotate(rotations[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F); 
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, -sneak, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.125D, sneak, 0.0D);
    if (rotations[4][0] != 0.0F)
      GlStateManager.rotate(rotations[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F); 
    if (rotations[4][1] != 0.0F)
      GlStateManager.rotate(rotations[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F); 
    if (rotations[4][2] != 0.0F)
      GlStateManager.rotate(rotations[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F); 
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, -sneak, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.translate(0.0D, 0.0D, player.isSneaking() ? 0.25D : 0.0D);
    GlStateManager.pushMatrix();
    double sneakOffset = 0.0D;
    if (player.isSneaking())
      sneakOffset = -0.05D; 
    GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.01725D : 0.0D);
    GlStateManager.pushMatrix();
    GlStateManager.translate(-0.375D, sneak + 0.55D, 0.0D);
    if (rotations[1][0] != 0.0F)
      GlStateManager.rotate(rotations[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F); 
    if (rotations[1][1] != 0.0F)
      GlStateManager.rotate(rotations[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F); 
    if (rotations[1][2] != 0.0F)
      GlStateManager.rotate(-rotations[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F); 
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, -0.5D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.375D, sneak + 0.55D, 0.0D);
    if (rotations[2][0] != 0.0F)
      GlStateManager.rotate(rotations[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F); 
    if (rotations[2][1] != 0.0F)
      GlStateManager.rotate(rotations[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F); 
    if (rotations[2][2] != 0.0F)
      GlStateManager.rotate(-rotations[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F); 
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, -0.5D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0D, sneak + 0.55D, 0.0D);
    if (rotations[0][0] != 0.0F)
      GlStateManager.rotate(rotations[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F); 
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, 0.3D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
    GlStateManager.rotate(player.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
    if (player.isSneaking())
      sneakOffset = -0.16175D; 
    GlStateManager.translate(0.0D, sneakOffset, player.isSneaking() ? -0.48025D : 0.0D);
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0D, sneak, 0.0D);
    GlStateManager.glBegin(3);
    GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
    GL11.glVertex3d(0.125D, 0.0D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0D, sneak, 0.0D);
    GlStateManager.glBegin(3);
    GL11.glVertex3d(0.0D, 0.0D, 0.0D);
    GL11.glVertex3d(0.0D, 0.55D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.pushMatrix();
    GlStateManager.translate(0.0D, sneak + 0.55D, 0.0D);
    GlStateManager.glBegin(3);
    GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
    GL11.glVertex3d(0.375D, 0.0D, 0.0D);
    GlStateManager.glEnd();
    GlStateManager.popMatrix();
    GlStateManager.popMatrix();
  }
  
  private static Skeleton INSTANCE = new Skeleton();
  
  Map<EntityPlayer, float[][]> rotationList;
}
