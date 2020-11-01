//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.ColorUtil;
import com.gamesense.api.util.PhobosRenderUtil;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.api.util.world.TpsUtils;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Crystals extends Module {
  Setting.Boolean Totems;
  
  Setting.Boolean Crystals;
  
  Setting.Boolean Gapples;
  
  Setting.Boolean Xp;
  
  Setting.Boolean XYZ;
  
  Setting.Boolean TPS;
  
  Setting.Boolean FPS;
  
  Setting.Boolean Hole;
  
  Setting.Boolean Notifications;
  
  Setting.Integer totemx;
  
  Setting.Integer totemy;
  
  Setting.Integer crystalx;
  
  Setting.Integer crystaly;
  
  Setting.Integer gapplex;
  
  Setting.Integer gappley;
  
  Setting.Integer xpx;
  
  Setting.Integer xpy;
  
  Setting.Integer holex;
  
  Setting.Integer holey;
  
  Setting.Boolean playerViewer;
  
  Setting.Integer playerViewerX;
  
  Setting.Integer playerViewerY;
  
  Setting.Double playerScale;
  
  Setting.Boolean targetHud;
  
  Setting.Boolean targetHudBackground;
  
  Setting.Integer targetHudX;
  
  Setting.Integer targetHudY;
  
  Setting.Boolean tots;
  
  Color c;
  
  public Crystals() {
    super("HUD2", Module.Category.HUD);
  }
  
  private static final ItemStack totemm = new ItemStack(Items.TOTEM_OF_UNDYING);
  
  public void setup() {
    ArrayList<String> displayModes = new ArrayList<>();
    this.tots = registerBoolean("Tots", "Tots", true);
    this.Totems = registerBoolean("Totems", "Totems", false);
    this.totemx = registerInteger("Totems X", "TotemsX", 0, 0, 1000);
    this.totemy = registerInteger("Totems Y", "TotemsY", 0, 0, 1000);
    this.Crystals = registerBoolean("Crystals", "Crystals", false);
    this.crystalx = registerInteger("Crystals X", "CrystalsX", 0, 0, 1000);
    this.crystaly = registerInteger("Crystals Y", "CrystalsY", 0, 0, 1000);
    this.Gapples = registerBoolean("Gapples", "Gapples", false);
    this.gapplex = registerInteger("Gapples X", "GapplesX", 0, 0, 1000);
    this.gappley = registerInteger("Gapples Y", "GapplesY", 0, 0, 1000);
    this.Xp = registerBoolean("Xp", "Xp", false);
    this.xpx = registerInteger("Xp X", "XpX", 0, 0, 1000);
    this.xpy = registerInteger("Xp Y", "XpY", 0, 0, 1000);
    this.XYZ = registerBoolean("XYZ", "XYZ", false);
    this.TPS = registerBoolean("TPS", "TPS", false);
    this.FPS = registerBoolean("FPS", "FPS", false);
    this.Hole = registerBoolean("Hole", "Hole", false);
    this.holex = registerInteger("Hole X", "HoleX", 0, 0, 1000);
    this.holey = registerInteger("Hole Y", "HoleY", 0, 0, 1000);
    this.Notifications = registerBoolean("Notifications", "Notifications", false);
    this.playerViewer = registerBoolean("Player Viewer", "PlayerViewer", false);
    this.playerViewerX = registerInteger("Player Viewer X", "PlayerViewerX", 0, 0, 1000);
    this.playerViewerY = registerInteger("Player Viewer Y", "PlayerViewerY", 0, 0, 1000);
    this.playerScale = registerDouble("Player Scale", "PlayerScale", 1.0D, 0.1D, 2.0D);
    this.targetHud = registerBoolean("TargetHud", "TargetHud", false);
    this.targetHudBackground = registerBoolean("TargetHud BG", "TargetHudBG", false);
    this.targetHudX = registerInteger("targetHud X", "targetHudX", 0, 0, 1000);
    this.targetHudY = registerInteger("targetHud Y", "targetHudY", 0, 0, 1000);
  }
  
  public void onRender() {
    if (ColorMain.rainbow.getValue()) {
      this.c = Rainbow.getColor();
    } else {
      this.c = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue());
    } 
    if (this.Totems.getValue())
      drawStringWithShadow("TOTS: " + totemCount(), this.totemx.getValue(), this.totemy.getValue(), this.c.getRGB()); 
    if (this.tots.getValue())
      renderTotemHUD(); 
    if (this.Crystals.getValue())
      drawStringWithShadow("CRYS: " + crystalCount(), this.crystalx.getValue(), this.crystaly.getValue(), this.c.getRGB()); 
    if (this.Gapples.getValue())
      drawStringWithShadow("GAPS: " + gappleCount(), this.gapplex.getValue(), this.gappley.getValue(), this.c.getRGB()); 
    if (this.Xp.getValue())
      drawStringWithShadow("XP: " + xpCount(), this.xpx.getValue(), this.xpy.getValue(), this.c.getRGB()); 
    if (this.Hole.getValue())
      renderHole(this.holex.getValue(), this.holey.getValue()); 
    if (this.playerViewer.getValue())
      drawPlayer(); 
    if (this.targetHud.getValue())
      drawTargetHud(); 
    if (this.Notifications.getValue())
      GameSenseMod.notificationManager.renderNotifications(); 
    if (this.XYZ.getValue()) {
      long x = Math.round(mc.player.posX);
      long y = Math.round(mc.player.posY);
      long z = Math.round(mc.player.posZ);
      String coords = (mc.player.dimension == -1) ? String.format("§7%s §f(%s)§8, §7%s §f(%s)§8, §7%s §f(%s)", new Object[] { Long.valueOf(x), Long.valueOf(x * 8L), Long.valueOf(y), Long.valueOf(y), Long.valueOf(z), Long.valueOf(z * 8L) }) : String.format("§f%s §7(%s)§8, §f%s §7(%s)§8, §f%s §7(%s)", new Object[] { Long.valueOf(x), Long.valueOf(x / 8L), Long.valueOf(y), Long.valueOf(y), Long.valueOf(z), Long.valueOf(z / 8L) });
      drawStringWithShadow("XYZ " + coords, 2, (new ScaledResolution(mc)).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 14) : (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2)), this.c.getRGB());
    } 
    if (this.TPS.getValue())
      drawStringWithShadow("TPS " + ChatFormatting.WHITE + String.format("%.2f", new Object[] { Double.valueOf(TpsUtils.getTickRate()) }), 2, (new ScaledResolution(mc)).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 14) : (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2)) - (this.XYZ.getValue() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2) : 0), this.c.getRGB()); 
    if (this.FPS.getValue())
      drawStringWithShadow("FPS " + ChatFormatting.WHITE + Minecraft.getDebugFPS(), 2, (new ScaledResolution(mc)).getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 14) : (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2)) - (this.XYZ.getValue() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2) : 0) - (this.TPS.getValue() ? (FontUtils.getFontHeight(HUD.customFont.getValue()) + 2) : 0), this.c.getRGB()); 
  }
  
  public static int getItems(Item totemOfUndying) {
    return mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
  }
  
  public static int getItems2(Item endCrystal) {
    return mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.END_CRYSTAL)).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.END_CRYSTAL)).mapToInt(ItemStack::getCount).sum();
  }
  
  public static int getItems3(Item goldenApple) {
    return mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.GOLDEN_APPLE)).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.GOLDEN_APPLE)).mapToInt(ItemStack::getCount).sum();
  }
  
  public static int getItems4(Item experienceBottle) {
    return mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)).mapToInt(ItemStack::getCount).sum() + mc.player.inventory.offHandInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)).mapToInt(ItemStack::getCount).sum();
  }
  
  public static EntityPlayer getClosestEnemy() {
    EntityPlayer closestPlayer = null;
    for (EntityPlayer player : mc.world.playerEntities) {
      if (player == mc.player)
        continue; 
      if (closestPlayer == null) {
        closestPlayer = player;
        continue;
      } 
      if (mc.player.getDistanceSq((Entity)player) >= mc.player.getDistanceSq((Entity)closestPlayer))
        continue; 
      closestPlayer = player;
    } 
    return closestPlayer;
  }
  
  public void renderTotemHUD() {
    ScaledResolution resolution = new ScaledResolution(mc);
    int width = resolution.getScaledWidth();
    int height = resolution.getScaledHeight();
    int totems = HUD.mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
    if (HUD.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
      totems += HUD.mc.player.getHeldItemOffhand().getCount(); 
    if (totems > 0) {
      GlStateManager.enableTexture2D();
      int i = width / 2;
      int iteration = 0;
      int y = height - 55 - ((HUD.mc.player.isInWater() && HUD.mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
      int x = i - 189 + 180 + 2;
      GlStateManager.enableDepth();
      PhobosRenderUtil.itemRender.zLevel = 200.0F;
      PhobosRenderUtil.itemRender.renderItemAndEffectIntoGUI(totemm, x, y);
      PhobosRenderUtil.itemRender.renderItemOverlayIntoGUI(HUD.mc.fontRenderer, totemm, x, y, "");
      PhobosRenderUtil.itemRender.zLevel = 0.0F;
      GlStateManager.enableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      drawStringWithShadow(totems + "", x + 19 - 2 - FontUtils.getStringWidth(HUD.customFont.getValue(), totems + ""), y + 9, 16777215);
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
    } 
  }
  
  public void drawTargetHud() {
    int healthColor, color;
    EntityPlayer target = getClosestEnemy();
    if (target == null)
      return; 
    if (this.targetHudBackground.getValue())
      PhobosRenderUtil.drawRectangleCorrectly(this.targetHudX.getValue(), this.targetHudY.getValue(), 210, 100, ColorUtil.toRGBA(20, 20, 20, 160)); 
    GlStateManager.disableRescaleNormal();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    try {
      GuiInventory.drawEntityOnScreen(this.targetHudX.getValue() + 30, this.targetHudY.getValue() + 90, 45, 0.0F, 0.0F, (EntityLivingBase)target);
    } catch (Exception e) {
      e.printStackTrace();
    } 
    GlStateManager.enableRescaleNormal();
    GlStateManager.enableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    drawStringWithShadow(target.getName(), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + 10, ColorUtil.toRGBA(255, 0, 0, 255));
    float health = target.getHealth() + target.getAbsorptionAmount();
    if (health >= 16.0F) {
      healthColor = ColorUtil.toRGBA(0, 255, 0, 255);
    } else if (health >= 10.0F) {
      healthColor = ColorUtil.toRGBA(255, 255, 0, 255);
    } else {
      healthColor = ColorUtil.toRGBA(255, 0, 0, 255);
    } 
    DecimalFormat df = new DecimalFormat("##.#");
    drawStringWithShadow(df.format((target.getHealth() + target.getAbsorptionAmount())), this.targetHudX.getValue() + 60 + FontUtils.getStringWidth(HUD.customFont.getValue(), target.getName() + "  "), this.targetHudY.getValue() + 10, healthColor);
    Integer ping = Integer.valueOf((mc.getConnection().getPlayerInfo(target.getUniqueID()) == null) ? 0 : mc.getConnection().getPlayerInfo(target.getUniqueID()).getResponseTime());
    if (ping.intValue() >= 100) {
      color = ColorUtil.toRGBA(0, 255, 0, 255);
    } else if (ping.intValue() > 50) {
      color = ColorUtil.toRGBA(255, 255, 0, 255);
    } else {
      color = ColorUtil.toRGBA(255, 0, 0, 255);
    } 
    drawStringWithShadow("Ping: " + ((ping == null) ? 0 : ping.intValue()), this.targetHudX.getValue() + 60, this.targetHudY.getValue() + FontUtils.getFontHeight(HUD.customFont.getValue()) + 20, color);
    GlStateManager.enableTexture2D();
    int iteration = 0;
    int i = this.targetHudX.getValue() + 50;
    int y = this.targetHudY.getValue() + FontUtils.getFontHeight(HUD.customFont.getValue()) * 3 + 44;
    for (ItemStack is : target.inventory.armorInventory) {
      iteration++;
      if (is.isEmpty())
        continue; 
      int x = i - 90 + (9 - iteration) * 20 + 2;
      GlStateManager.enableDepth();
      PhobosRenderUtil.itemRender.zLevel = 200.0F;
      PhobosRenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
      PhobosRenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
      PhobosRenderUtil.itemRender.zLevel = 0.0F;
      GlStateManager.enableTexture2D();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      int dmg = 0;
      int itemDurability = is.getMaxDamage() - is.getItemDamage();
      float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
      float red = 1.0F - green;
      dmg = 100 - (int)(red * 100.0F);
      drawStringWithShadow(dmg + "", (int)((x + 8) - FontUtils.getStringWidth(HUD.customFont.getValue(), dmg + "") / 2.0F), y - 5, ColorUtil.toRGBA((int)(red * 255.0F), (int)(green * 255.0F), 0));
    } 
    drawStringWithShadow("Strength", this.targetHudX.getValue() + 150, this.targetHudY.getValue() + 60, target.isPotionActive(MobEffects.STRENGTH) ? ColorUtil.toRGBA(0, 255, 0, 255) : ColorUtil.toRGBA(255, 0, 0, 255));
  }
  
  public void drawPlayer(EntityPlayer player, int x, int y) {
    EntityPlayer ent = player;
    GlStateManager.pushMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enableAlpha();
    GlStateManager.shadeModel(7424);
    GlStateManager.enableAlpha();
    GlStateManager.enableDepth();
    GlStateManager.rotate(0.0F, 0.0F, 5.0F, 0.0F);
    GlStateManager.enableColorMaterial();
    GlStateManager.pushMatrix();
    GlStateManager.translate((this.playerViewerX.getValue() + 25), (this.playerViewerY.getValue() + 25), 50.0F);
    GlStateManager.scale(-50.0D * this.playerScale.getValue(), 50.0D * this.playerScale.getValue(), 50.0D * this.playerScale.getValue());
    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
    GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-((float)Math.atan((this.playerViewerY.getValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, 0.0F);
    RenderManager rendermanager = mc.getRenderManager();
    rendermanager.setPlayerViewY(180.0F);
    rendermanager.setRenderShadow(false);
    try {
      rendermanager.renderEntity((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
    } catch (Exception exception) {}
    rendermanager.setRenderShadow(true);
    GlStateManager.popMatrix();
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.depthFunc(515);
    GlStateManager.resetColor();
    GlStateManager.disableDepth();
    GlStateManager.popMatrix();
  }
  
  public void drawPlayer() {
    EntityPlayerSP entityPlayerSP = mc.player;
    GlStateManager.pushMatrix();
    GlStateManager.color(1.0F, 1.0F, 1.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enableAlpha();
    GlStateManager.shadeModel(7424);
    GlStateManager.enableAlpha();
    GlStateManager.enableDepth();
    GlStateManager.rotate(0.0F, 0.0F, 5.0F, 0.0F);
    GlStateManager.enableColorMaterial();
    GlStateManager.pushMatrix();
    GlStateManager.translate((this.playerViewerX.getValue() + 25), (this.playerViewerY.getValue() + 25), 50.0F);
    GlStateManager.scale(-50.0D * this.playerScale.getValue(), 50.0D * this.playerScale.getValue(), 50.0D * this.playerScale.getValue());
    GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
    GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
    RenderHelper.enableStandardItemLighting();
    GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-((float)Math.atan((this.playerViewerY.getValue() / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
    GlStateManager.translate(0.0F, 0.0F, 0.0F);
    RenderManager rendermanager = mc.getRenderManager();
    rendermanager.setPlayerViewY(180.0F);
    rendermanager.setRenderShadow(false);
    try {
      rendermanager.renderEntity((Entity)entityPlayerSP, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
    } catch (Exception exception) {}
    rendermanager.setRenderShadow(true);
    GlStateManager.popMatrix();
    RenderHelper.disableStandardItemLighting();
    GlStateManager.disableRescaleNormal();
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
    GlStateManager.disableTexture2D();
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    GlStateManager.depthFunc(515);
    GlStateManager.resetColor();
    GlStateManager.disableDepth();
    GlStateManager.popMatrix();
  }
  
  private String totemCount() {
    return String.valueOf(getItems(Items.TOTEM_OF_UNDYING));
  }
  
  private String crystalCount() {
    return String.valueOf(getItems2(Items.END_CRYSTAL));
  }
  
  private String gappleCount() {
    return String.valueOf(getItems3(Items.GOLDEN_APPLE));
  }
  
  private String xpCount() {
    return String.valueOf(getItems4(Items.EXPERIENCE_BOTTLE));
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (HUD.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
  
  private void renderHole(double holex, double holey) {
    double leftX = holex;
    double leftY = holey + 16.0D;
    double upX = holex + 16.0D;
    double upY = holey;
    double rightX = holex + 32.0D;
    double rightY = holey + 16.0D;
    double bottomX = holex + 16.0D;
    double bottomY = holey + 32.0D;
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    switch (mc.getRenderViewEntity().getHorizontalFacing()) {
      case NORTH:
        if (northObby() || northBrock())
          renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock())); 
        if (westObby() || westBrock())
          renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock())); 
        if (eastObby() || eastBrock())
          renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock())); 
        if (southObby() || southBrock())
          renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock())); 
        break;
      case SOUTH:
        if (southObby() || southBrock())
          renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock())); 
        if (eastObby() || eastBrock())
          renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock())); 
        if (westObby() || westBrock())
          renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock())); 
        if (northObby() || northBrock())
          renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock())); 
        break;
      case WEST:
        if (westObby() || westBrock())
          renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock())); 
        if (southObby() || southBrock())
          renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock())); 
        if (northObby() || northBrock())
          renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock())); 
        if (eastObby() || eastBrock())
          renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock())); 
        break;
      case EAST:
        if (eastObby() || eastBrock())
          renderItem(upX, upY, new ItemStack(mc.world.getBlockState(playerPos.east()).getBlock())); 
        if (northObby() || northBrock())
          renderItem(leftX, leftY, new ItemStack(mc.world.getBlockState(playerPos.north()).getBlock())); 
        if (southObby() || southBrock())
          renderItem(rightX, rightY, new ItemStack(mc.world.getBlockState(playerPos.south()).getBlock())); 
        if (westObby() || westBrock())
          renderItem(bottomX, bottomY, new ItemStack(mc.world.getBlockState(playerPos.west()).getBlock())); 
        break;
    } 
  }
  
  private void renderItem(double x, double y, ItemStack is) {
    RenderHelper.enableGUIStandardItemLighting();
    mc.getRenderItem().renderItemAndEffectIntoGUI(is, (int)x, (int)y);
    RenderHelper.disableStandardItemLighting();
  }
  
  private boolean northObby() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.OBSIDIAN);
  }
  
  private boolean eastObby() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.OBSIDIAN);
  }
  
  private boolean southObby() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.OBSIDIAN);
  }
  
  private boolean westObby() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.OBSIDIAN);
  }
  
  private boolean northBrock() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.north()).getBlock() == Blocks.BEDROCK);
  }
  
  private boolean eastBrock() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.east()).getBlock() == Blocks.BEDROCK);
  }
  
  private boolean southBrock() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.south()).getBlock() == Blocks.BEDROCK);
  }
  
  private boolean westBrock() {
    Vec3d vec3d = BlockUtils.getInterpolatedPos((Entity)mc.player, 0.0F);
    BlockPos playerPos = new BlockPos(vec3d);
    return (mc.world.getBlockState(playerPos.west()).getBlock() == Blocks.BEDROCK);
  }
}
