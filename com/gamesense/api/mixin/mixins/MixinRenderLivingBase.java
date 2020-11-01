//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.mixin.mixins;

import com.gamesense.api.event.events.RenderEntityModelEvent;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.render.Chams;
import com.gamesense.client.module.modules.render.ESP;
import com.gamesense.client.module.modules.render.Skeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({RenderLivingBase.class})
public abstract class MixinRenderLivingBase extends Render {
  @Shadow
  protected ModelBase mainModel;
  
  @Shadow
  protected abstract boolean isVisible(EntityLivingBase paramEntityLivingBase);
  
  protected MixinRenderLivingBase(RenderManager renderManager) {
    super(renderManager);
  }
  
  @Redirect(method = {"renderModel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
  private void renderModelHook(ModelBase modelBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    if (Skeleton.getInstance().isEnabled() || ESP.getInstance().isEnabled()) {
      RenderEntityModelEvent event = new RenderEntityModelEvent(0, modelBase, entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
      if (Skeleton.getInstance().isEnabled())
        Skeleton.getInstance().onRenderModel(event); 
      if (ESP.getInstance().isEnabled()) {
        ESP.getInstance().onRenderModel(event);
        if (event.isCanceled())
          return; 
      } 
    } 
    modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
  }
  
  @Overwrite
  protected void renderModel(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    boolean flag = isVisible(entitylivingbaseIn);
    boolean flag1 = (!flag && !entitylivingbaseIn.isInvisibleToPlayer((EntityPlayer)(Minecraft.getMinecraft()).player));
    if (flag || flag1) {
      if (!bindEntityTexture((Entity)entitylivingbaseIn))
        return; 
      if (flag1)
        GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL); 
      if (ModuleManager.isModuleEnabled("Chams") && entitylivingbaseIn != (Minecraft.getMinecraft()).player) {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.5F);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1028, 6913);
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1028, 6914);
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1028, 6913);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(10754);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        GL11.glColor4d((Chams.red.getValue() / 255.0F), (Chams.green.getValue() / 255.0F), (Chams.blue.getValue() / 255.0F), (Chams.alpha.getValue() / 255.0F));
        this.mainModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glColor4d((Chams.invisRed.getValue() / 255.0F), (Chams.invisGreen.getValue() / 255.0F), (Chams.invisBlue.getValue() / 255.0F), (Chams.invisAlpha.getValue() / 255.0F));
        this.mainModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        GL11.glEnable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
      } else {
        this.mainModel.render((Entity)entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
      } 
      if (flag1)
        GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL); 
    } 
  }
}
