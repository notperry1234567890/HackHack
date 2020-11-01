//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.Render3DEvent;
import com.gamesense.api.event.events.RenderEntityModelEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.EntityUtil;
import com.gamesense.api.util.PhobosRenderUtil;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ESP extends Module {
  Setting.Mode mode;
  
  Setting.Boolean players;
  
  Setting.Boolean animals;
  
  Setting.Boolean mobs;
  
  Setting.Boolean items;
  
  Setting.Boolean xporbs;
  
  Setting.Boolean xpbottles;
  
  Setting.Boolean pearl;
  
  Setting.Integer red;
  
  Setting.Integer green;
  
  Setting.Integer blue;
  
  Setting.Integer boxAlpha;
  
  Setting.Integer alpha;
  
  Setting.Double lineWidth;
  
  Setting.Boolean colorFriends;
  
  Setting.Boolean self;
  
  Setting.Boolean onTop;
  
  Setting.Boolean invisibles;
  
  public ESP() {
    super("ESP", Module.Category.Render);
  }
  
  public void setup() {
    ArrayList<String> modes = new ArrayList<>();
    modes.add("OUTLINE");
    modes.add("WIREFRAME");
    this.mode = registerMode("Mode", "Mode", modes, "OUTLINE");
    this.players = registerBoolean("Players", "Players", true);
    this.animals = registerBoolean("Animals", "Animals", false);
    this.mobs = registerBoolean("Mobs", "Mobs", false);
    this.items = registerBoolean("Items", "Items", false);
    this.xporbs = registerBoolean("XpOrbs", "XpOrbs", false);
    this.xpbottles = registerBoolean("XpBottles", "XpBottles", false);
    this.pearl = registerBoolean("Pearls", "Pearls", false);
    this.red = registerInteger("Red", "Red", 255, 0, 255);
    this.green = registerInteger("Green", "Green", 255, 0, 255);
    this.blue = registerInteger("Blue", "Blue", 255, 0, 255);
    this.boxAlpha = registerInteger("BoxAlpha", "BoxAlpha", 120, 0, 255);
    this.alpha = registerInteger("Alpha", "Alpha", 255, 0, 255);
    this.lineWidth = registerDouble("LineWidth", "LineWidth", 2.0D, 0.10000000149011612D, 5.0D);
    this.colorFriends = registerBoolean("Friends", "Friends", true);
    this.self = registerBoolean("Self", "Self", true);
    this.onTop = registerBoolean("onTop", "onTop", true);
    this.invisibles = registerBoolean("Invisibles", "Invisibles", false);
    setInstance();
  }
  
  private void setInstance() {
    INSTANCE = this;
  }
  
  public static ESP getInstance() {
    if (INSTANCE == null)
      INSTANCE = new ESP(); 
    return INSTANCE;
  }
  
  public void onRender3D(Render3DEvent event) {
    if (this.items.getValue()) {
      int i = 0;
      for (Entity entity : mc.world.loadedEntityList) {
        if (entity instanceof net.minecraft.entity.item.EntityItem && mc.player.getDistanceSq(entity) < 2500.0D) {
          Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
          AxisAlignedBB bb = new AxisAlignedBB((entity.getEntityBoundingBox()).minX - 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).minY - 0.0D - entity.posY + interp.y, (entity.getEntityBoundingBox()).minZ - 0.05D - entity.posZ + interp.z, (entity.getEntityBoundingBox()).maxX + 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).maxY + 0.1D - entity.posY + interp.y, (entity.getEntityBoundingBox()).maxZ + 0.05D - entity.posZ + interp.z);
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          GlStateManager.disableDepth();
          GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
          GlStateManager.disableTexture2D();
          GlStateManager.depthMask(false);
          GL11.glEnable(2848);
          GL11.glHint(3154, 4354);
          GL11.glLineWidth(1.0F);
          RenderGlobal.renderFilledBox(bb, this.red.getValue() / 255.0F, this.green.getValue() / 255.0F, this.blue.getValue() / 255.0F, this.boxAlpha.getValue() / 255.0F);
          GL11.glDisable(2848);
          GlStateManager.depthMask(true);
          GlStateManager.enableDepth();
          GlStateManager.enableTexture2D();
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          PhobosRenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0F);
          if (++i >= 50)
            break; 
        } 
      } 
    } 
    if (this.xporbs.getValue()) {
      int i = 0;
      for (Entity entity : mc.world.loadedEntityList) {
        if (entity instanceof net.minecraft.entity.item.EntityXPOrb && mc.player.getDistanceSq(entity) < 2500.0D) {
          Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
          AxisAlignedBB bb = new AxisAlignedBB((entity.getEntityBoundingBox()).minX - 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).minY - 0.0D - entity.posY + interp.y, (entity.getEntityBoundingBox()).minZ - 0.05D - entity.posZ + interp.z, (entity.getEntityBoundingBox()).maxX + 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).maxY + 0.1D - entity.posY + interp.y, (entity.getEntityBoundingBox()).maxZ + 0.05D - entity.posZ + interp.z);
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          GlStateManager.disableDepth();
          GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
          GlStateManager.disableTexture2D();
          GlStateManager.depthMask(false);
          GL11.glEnable(2848);
          GL11.glHint(3154, 4354);
          GL11.glLineWidth(1.0F);
          RenderGlobal.renderFilledBox(bb, this.red.getValue() / 255.0F, this.green.getValue() / 255.0F, this.blue.getValue() / 255.0F, this.boxAlpha.getValue() / 255.0F);
          GL11.glDisable(2848);
          GlStateManager.depthMask(true);
          GlStateManager.enableDepth();
          GlStateManager.enableTexture2D();
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          PhobosRenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0F);
          if (++i >= 50)
            break; 
        } 
      } 
    } 
    if (this.pearl.getValue()) {
      int i = 0;
      for (Entity entity : mc.world.loadedEntityList) {
        if (entity instanceof net.minecraft.entity.item.EntityEnderPearl && mc.player.getDistanceSq(entity) < 2500.0D) {
          Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
          AxisAlignedBB bb = new AxisAlignedBB((entity.getEntityBoundingBox()).minX - 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).minY - 0.0D - entity.posY + interp.y, (entity.getEntityBoundingBox()).minZ - 0.05D - entity.posZ + interp.z, (entity.getEntityBoundingBox()).maxX + 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).maxY + 0.1D - entity.posY + interp.y, (entity.getEntityBoundingBox()).maxZ + 0.05D - entity.posZ + interp.z);
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          GlStateManager.disableDepth();
          GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
          GlStateManager.disableTexture2D();
          GlStateManager.depthMask(false);
          GL11.glEnable(2848);
          GL11.glHint(3154, 4354);
          GL11.glLineWidth(1.0F);
          RenderGlobal.renderFilledBox(bb, this.red.getValue() / 255.0F, this.green.getValue() / 255.0F, this.blue.getValue() / 255.0F, this.boxAlpha.getValue() / 255.0F);
          GL11.glDisable(2848);
          GlStateManager.depthMask(true);
          GlStateManager.enableDepth();
          GlStateManager.enableTexture2D();
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          PhobosRenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0F);
          if (++i >= 50)
            break; 
        } 
      } 
    } 
    if (this.xpbottles.getValue()) {
      int i = 0;
      for (Entity entity : mc.world.loadedEntityList) {
        if (entity instanceof net.minecraft.entity.item.EntityExpBottle && mc.player.getDistanceSq(entity) < 2500.0D) {
          Vec3d interp = EntityUtil.getInterpolatedRenderPos(entity, mc.getRenderPartialTicks());
          AxisAlignedBB bb = new AxisAlignedBB((entity.getEntityBoundingBox()).minX - 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).minY - 0.0D - entity.posY + interp.y, (entity.getEntityBoundingBox()).minZ - 0.05D - entity.posZ + interp.z, (entity.getEntityBoundingBox()).maxX + 0.05D - entity.posX + interp.x, (entity.getEntityBoundingBox()).maxY + 0.1D - entity.posY + interp.y, (entity.getEntityBoundingBox()).maxZ + 0.05D - entity.posZ + interp.z);
          GlStateManager.pushMatrix();
          GlStateManager.enableBlend();
          GlStateManager.disableDepth();
          GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
          GlStateManager.disableTexture2D();
          GlStateManager.depthMask(false);
          GL11.glEnable(2848);
          GL11.glHint(3154, 4354);
          GL11.glLineWidth(1.0F);
          RenderGlobal.renderFilledBox(bb, this.red.getValue() / 255.0F, this.green.getValue() / 255.0F, this.blue.getValue() / 255.0F, this.boxAlpha.getValue() / 255.0F);
          GL11.glDisable(2848);
          GlStateManager.depthMask(true);
          GlStateManager.enableDepth();
          GlStateManager.enableTexture2D();
          GlStateManager.disableBlend();
          GlStateManager.popMatrix();
          PhobosRenderUtil.drawBlockOutline(bb, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()), 1.0F);
          if (++i >= 50)
            break; 
        } 
      } 
    } 
  }
  
  public void onRenderModel(RenderEntityModelEvent event) {
    if (event.getStage() != 0 || event.entity == null || (event.entity.isInvisible() && !this.invisibles.getValue()) || (!this.self.getValue() && event.entity.equals(mc.player)) || (!this.players.getValue() && event.entity instanceof net.minecraft.entity.player.EntityPlayer) || (!this.animals.getValue() && EntityUtil.isPassive(event.entity)) || (!this.mobs.getValue() && !EntityUtil.isPassive(event.entity) && !(event.entity instanceof net.minecraft.entity.player.EntityPlayer)))
      return; 
    Color color = EntityUtil.getColor(event.entity, this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue(), this.colorFriends.getValue());
    boolean fancyGraphics = mc.gameSettings.fancyGraphics;
    mc.gameSettings.fancyGraphics = false;
    float gamma = mc.gameSettings.gammaSetting;
    mc.gameSettings.gammaSetting = 10000.0F;
    if (this.onTop.getValue())
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale); 
    if (this.mode.getValue().equalsIgnoreCase("OUTLINE")) {
      PhobosRenderUtil.renderOne((float)this.lineWidth.getValue());
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
      GlStateManager.glLineWidth((float)this.lineWidth.getValue());
      PhobosRenderUtil.renderTwo();
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
      GlStateManager.glLineWidth((float)this.lineWidth.getValue());
      PhobosRenderUtil.renderThree();
      PhobosRenderUtil.renderFour(color);
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
      GlStateManager.glLineWidth((float)this.lineWidth.getValue());
      PhobosRenderUtil.renderFive();
    } else {
      GL11.glPushMatrix();
      GL11.glPushAttrib(1048575);
      if (this.mode.getValue().equalsIgnoreCase("WIREFRAME")) {
        GL11.glPolygonMode(1032, 6913);
      } else {
        GL11.glPolygonMode(1028, 6913);
      } 
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      GL11.glEnable(2848);
      GL11.glEnable(3042);
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(this.red.getValue() / 255.0F, this.green.getValue() / 255.0F, this.blue.getValue() / 255.0F, (this.alpha.getValue() / 255));
      GlStateManager.glLineWidth((float)this.lineWidth.getValue());
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale);
      GL11.glPopAttrib();
      GL11.glPopMatrix();
    } 
    if (!this.onTop.getValue())
      event.modelBase.render(event.entity, event.limbSwing, event.limbSwingAmount, event.age, event.headYaw, event.headPitch, event.scale); 
    try {
      mc.gameSettings.fancyGraphics = fancyGraphics;
      mc.gameSettings.gammaSetting = gamma;
    } catch (Exception exception) {}
    event.setCanceled(true);
  }
  
  private static ESP INSTANCE = new ESP();
  
  public String getHudInfo() {
    String t = "";
    if (this.mode.getValue().equalsIgnoreCase("OUTLINE"))
      t = "[" + ChatFormatting.WHITE + "Outline" + ChatFormatting.GRAY + "]"; 
    if (this.mode.getValue().equalsIgnoreCase("WIREFRAME"))
      t = "[" + ChatFormatting.WHITE + "Wireframe" + ChatFormatting.GRAY + "]"; 
    return t;
  }
}
