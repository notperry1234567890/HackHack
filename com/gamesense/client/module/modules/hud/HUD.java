//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.MathUtil;
import com.gamesense.api.util.RenderUtil;
import com.gamesense.api.util.color.ColourHolder;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.api.util.world.TpsUtils;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.combat.AutoCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class HUD extends Module {
  public static Setting.Boolean customFont;
  
  Setting.Boolean PotionEffects;
  
  Setting.Boolean Watermark;
  
  Setting.Boolean Greeter;
  
  Setting.Integer greeterX;
  
  Setting.Integer greeterY;
  
  Setting.Boolean Inventory;
  
  Setting.Integer inventoryX;
  
  Setting.Integer inventoryY;
  
  Setting.Boolean GameSenseInfo;
  
  Setting.Mode Type;
  
  Setting.Boolean ArrayList;
  
  Setting.Boolean ArmorHud;
  
  Setting.Integer potionx;
  
  Setting.Integer potiony;
  
  Setting.Integer infox;
  
  Setting.Integer infoy;
  
  Setting.Boolean sortUp;
  
  Setting.Boolean right;
  
  Setting.Boolean ArrayListHot;
  
  Setting.Boolean psortUp;
  
  Setting.Boolean pright;
  
  Setting.Integer arrayx;
  
  Setting.Integer arrayy;
  
  private BlockPos[] surroundOffset;
  
  ResourceLocation resource;
  
  Color c;
  
  int sort;
  
  int modCount;
  
  int count;
  
  DecimalFormat format1;
  
  DecimalFormat format2;
  
  public HUD() {
    super("HUD", Module.Category.HUD);
    this.format1 = new DecimalFormat("0");
    this.format2 = new DecimalFormat("00");
    setDrawn(false);
    this.resource = new ResourceLocation("minecraft:inventory_viewer.png");
  }
  
  private static final RenderItem itemRender = Minecraft.getMinecraft()
    .getRenderItem();
  
  int totems;
  
  public void setup() {
    ArrayList<String> Modes = new ArrayList<>();
    Modes.add("PvP");
    Modes.add("Combat");
    this.Type = registerMode("Info Type", "InfoType", Modes, "PvP");
    this.infox = registerInteger("Information X", "InformationX", 0, 0, 1000);
    this.infoy = registerInteger("Information Y", "InformationY", 0, 0, 1000);
    this.GameSenseInfo = registerBoolean("Information", "Information", false);
    this.ArmorHud = registerBoolean("Armor Hud", "ArmorHud", false);
    this.ArrayList = registerBoolean("ArrayList", "ArrayList", false);
    this.ArrayListHot = registerBoolean("ArrayList Hot", "ArrayListHot", true);
    this.sortUp = registerBoolean("Array Sort Up", "ArraySortUp", false);
    this.Inventory = registerBoolean("Inventory", "Inventory", false);
    this.inventoryX = registerInteger("Inventory X", "InventoryX", 0, 0, 1000);
    this.inventoryY = registerInteger("Inventory Y", "InventoryY", 12, 0, 1000);
    this.PotionEffects = registerBoolean("Potion Effects", "PotionEffects", false);
    this.potionx = registerInteger("Potion X", "PotionX", 0, 0, 1000);
    this.potiony = registerInteger("Potion Y", "PotionY", 0, 0, 1000);
    this.psortUp = registerBoolean("Potion Sort Up", "PotionSortUp", false);
    this.pright = registerBoolean("Potion Right", "PotionRight", false);
    this.Watermark = registerBoolean("Watermark", "Watermark", false);
    this.Greeter = registerBoolean("Greeter", "Greeter", false);
    this.greeterX = registerInteger("Greeter X", "GreeterX", 0, 0, 1000);
    this.greeterY = registerInteger("Greeter Y", "GreeterY", 0, 0, 1000);
    customFont = registerBoolean("Custom Font", "CustomFont", false);
  }
  
  public void onRender() {
    if (ColorMain.rainbow.getValue()) {
      this.c = Rainbow.getColor();
    } else {
      this.c = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue());
    } 
    if (this.PotionEffects.getValue()) {
      this.count = 0;
      try {
        mc.player.getActivePotionEffects().forEach(effect -> {
              String name = I18n.format(effect.getPotion().getName(), new Object[0]);
              double duration = (effect.getDuration() / TpsUtils.getTickRate());
              int amplifier = effect.getAmplifier() + 1;
              double p1 = duration % 60.0D;
              double p2 = duration / 60.0D;
              double p3 = p2 % 60.0D;
              int color = effect.getPotion().getLiquidColor();
              String minutes = this.format1.format(p3);
              String seconds = this.format2.format(p1);
              String s = getColoredPotionString(effect) + name + " " + amplifier + ChatFormatting.GRAY + " " + minutes + ":" + seconds;
              if (this.psortUp.getValue()) {
                if (this.pright.getValue()) {
                  drawStringWithShadow(s, this.potionx.getValue() - getWidth(s), this.potiony.getValue() + this.count * 10, this.c.getRGB());
                } else {
                  drawStringWithShadow(s, this.potionx.getValue(), this.potiony.getValue() + this.count * 10, this.c.getRGB());
                } 
                this.count++;
              } else {
                if (this.pright.getValue()) {
                  drawStringWithShadow(s, this.potionx.getValue() - getWidth(s), this.potiony.getValue() + this.count * -10, this.c.getRGB());
                } else {
                  drawStringWithShadow(s, this.potionx.getValue(), this.potiony.getValue() + this.count * -10, this.c.getRGB());
                } 
                this.count++;
              } 
            });
      } catch (NullPointerException e) {
        e.printStackTrace();
      } 
    } 
    if (this.Watermark.getValue())
      drawStringWithShadow("HackHack 0.9.4", 0, 0, this.c.getRGB()); 
    if (this.Greeter.getValue())
      drawStringWithShadow(MathUtil.getTimeOfDay() + mc.player.getName(), this.greeterX.getValue(), this.greeterY.getValue(), this.c.getRGB()); 
    if (this.Inventory.getValue())
      drawInventory(this.inventoryX.getValue(), this.inventoryY.getValue()); 
    if (this.GameSenseInfo.getValue())
      if (this.Type.getValue().equalsIgnoreCase("PvP")) {
        Color on = new Color(0, 255, 0);
        Color off = new Color(255, 0, 0);
        Color watermark = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue());
        this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
          this.totems++; 
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(e -> (mc.player.getDistance(e) <= AutoCrystal.range.getValue())).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance((Entity)c)))).orElse(null);
        EntityOtherPlayerMP players = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityOtherPlayerMP).filter(entity -> !Friends.isFriend(entity.getName())).filter(e -> (mc.player.getDistance(e) <= AutoCrystal.placeRange.getValue())).map(entity -> (EntityOtherPlayerMP)entity).min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance((Entity)c)))).orElse(null);
        AutoCrystal a = (AutoCrystal)ModuleManager.getModuleByName("Autocrystal");
        this.surroundOffset = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        List<EntityPlayer> entities = new ArrayList<>((Collection<? extends EntityPlayer>)mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        if (this.Type.getValue().equalsIgnoreCase("PvP")) {
          drawStringWithShadow("hackhack", this.infox.getValue(), this.infoy.getValue(), this.c.getRGB());
          if (players != null && mc.player.getDistance((Entity)players) <= AutoCrystal.range.getValue()) {
            drawStringWithShadow("HTR", this.infox.getValue(), this.infoy.getValue() + 10, on.getRGB());
          } else {
            drawStringWithShadow("HTR", this.infox.getValue(), this.infoy.getValue() + 10, off.getRGB());
          } 
          if (players != null && mc.player.getDistance((Entity)players) <= AutoCrystal.placeRange.getValue()) {
            drawStringWithShadow("PLR", this.infox.getValue(), this.infoy.getValue() + 20, on.getRGB());
          } else {
            drawStringWithShadow("PLR", this.infox.getValue(), this.infoy.getValue() + 20, off.getRGB());
          } 
          if (this.totems > 0 && ModuleManager.isModuleEnabled("AutoTotem")) {
            drawStringWithShadow(this.totems + "", this.infox.getValue(), this.infoy.getValue() + 30, on.getRGB());
          } else {
            drawStringWithShadow(this.totems + "", this.infox.getValue(), this.infoy.getValue() + 30, off.getRGB());
          } 
          if (getPing() > 100) {
            drawStringWithShadow("PING " + getPing(), this.infox.getValue(), this.infoy.getValue() + 40, off.getRGB());
          } else {
            drawStringWithShadow("PING " + getPing(), this.infox.getValue(), this.infoy.getValue() + 40, on.getRGB());
          } 
          for (EntityPlayer e : entities) {
            int i = 0;
            for (BlockPos add : this.surroundOffset) {
              i++;
              BlockPos o = (new BlockPos((e.getPositionVector()).x, (e.getPositionVector()).y, (e.getPositionVector()).z)).add(add.getX(), add.getY(), add.getZ());
              if (mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN) {
                if (i == 1 && a.canPlaceCrystal(o.north(1).down()))
                  drawStringWithShadow("LBY", this.infox.getValue(), this.infoy.getValue() + 50, on.getRGB()); 
                if (i == 2 && a.canPlaceCrystal(o.east(1).down()))
                  drawStringWithShadow("LBY", this.infox.getValue(), this.infoy.getValue() + 50, on.getRGB()); 
                if (i == 3 && a.canPlaceCrystal(o.south(1).down()))
                  drawStringWithShadow("LBY", this.infox.getValue(), this.infoy.getValue() + 50, on.getRGB()); 
                if (i == 4 && a.canPlaceCrystal(o.west(1).down()))
                  drawStringWithShadow("LBY", this.infox.getValue(), this.infoy.getValue() + 50, on.getRGB()); 
              } else {
                drawStringWithShadow("LBY", this.infox.getValue(), this.infoy.getValue() + 50, off.getRGB());
              } 
            } 
          } 
        } 
      } else if (this.Type.getValue().equalsIgnoreCase("Combat")) {
        drawStringWithShadow(" ", this.infox.getValue(), this.infoy.getValue(), this.c.getRGB());
        if (ModuleManager.isModuleEnabled("AutoCrystal")) {
          drawStringWithShadow("AC: ON", this.infox.getValue(), this.infoy.getValue(), Color.green.getRGB());
        } else {
          drawStringWithShadow("AC: OFF", this.infox.getValue(), this.infoy.getValue(), Color.red.getRGB());
        } 
        if (ModuleManager.isModuleEnabled("KillAura")) {
          drawStringWithShadow("KA: ON", this.infox.getValue(), this.infoy.getValue() + 10, Color.green.getRGB());
        } else {
          drawStringWithShadow("KA: OFF", this.infox.getValue(), this.infoy.getValue() + 10, Color.red.getRGB());
        } 
        if (ModuleManager.isModuleEnabled("AutoFeetPlace")) {
          drawStringWithShadow("FP: ON", this.infox.getValue(), this.infoy.getValue() + 20, Color.green.getRGB());
        } else {
          drawStringWithShadow("FP: OFF", this.infox.getValue(), this.infoy.getValue() + 20, Color.red.getRGB());
        } 
        if (ModuleManager.isModuleEnabled("AutoTrap")) {
          drawStringWithShadow("AT: ON", this.infox.getValue(), this.infoy.getValue() + 30, Color.green.getRGB());
        } else {
          drawStringWithShadow("AT: OFF", this.infox.getValue(), this.infoy.getValue() + 30, Color.red.getRGB());
        } 
        if (ModuleManager.isModuleEnabled("SelfTrap")) {
          drawStringWithShadow("ST: ON", this.infox.getValue(), this.infoy.getValue() + 40, Color.green.getRGB());
        } else {
          drawStringWithShadow("ST: OFF", this.infox.getValue(), this.infoy.getValue() + 40, Color.red.getRGB());
        } 
      }  
    float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
    if (this.ArrayList.getValue()) {
      ScaledResolution resolution = new ScaledResolution(mc);
      if (this.sortUp.getValue()) {
        this.sort = -1;
      } else {
        this.sort = 1;
      } 
      this.modCount = 0;
      ModuleManager.getModules()
        .stream()
        .filter(Module::isEnabled)
        .filter(Module::isDrawn)
        .sorted(Comparator.comparing(module -> Integer.valueOf(FontUtils.getStringWidth(customFont.getValue(), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)))
        .forEach(m -> {
            if (ColorMain.rainbow.getValue()) {
              int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
              int r = rgb >> 16 & 0xFF;
              int g = rgb >> 8 & 0xFF;
              int b = rgb & 0xFF;
              this.c = new Color(r, g, b);
            } else {
              this.c = new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue());
            } 
            if (this.sortUp.getValue()) {
              if (this.ArrayListHot.getValue()) {
                RenderUtil.drawRect((resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (0 + this.modCount * 10 - 1), (getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4), 11.0F, (new Color(21, 21, 21, 100)).getRGB());
                RenderUtil.drawRect((resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (0 + this.modCount * 10 - 1), 2.0F, 11.0F, this.c.getRGB());
              } 
              drawStringWithShadow(m.getName() + ChatFormatting.GRAY + m.getHudInfo(), resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()), 0 + this.modCount * 10, this.c.getRGB());
              hue[0] = hue[0] + 0.02F;
              this.modCount++;
            } else {
              if (this.ArrayListHot.getValue()) {
                RenderUtil.drawRect((resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (resolution.getScaledHeight() + this.modCount * -10 - 1), (getWidth(m.getName() + ChatFormatting.GRAY + m.getHudInfo()) + 4), 11.0F, (new Color(21, 21, 21, 100)).getRGB());
                RenderUtil.drawRect((resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()) - 4), (resolution.getScaledHeight() + this.modCount * -10 - 1), 2.0F, 11.0F, this.c.getRGB());
              } 
              drawStringWithShadow(m.getName() + ChatFormatting.GRAY + m.getHudInfo(), resolution.getScaledWidth() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()), resolution.getScaledHeight() + this.modCount * -10, this.c.getRGB());
              hue[0] = hue[0] + 0.02F;
              this.modCount++;
            } 
          });
    } 
    if (this.ArmorHud.getValue()) {
      GlStateManager.enableTexture2D();
      ScaledResolution resolution = new ScaledResolution(mc);
      int i = resolution.getScaledWidth() / 2;
      int iteration = 0;
      int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
      for (ItemStack is : mc.player.inventory.armorInventory) {
        iteration++;
        if (is.isEmpty())
          continue; 
        int x = i - 90 + (9 - iteration) * 20 + 2;
        GlStateManager.enableDepth();
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(is, x, y);
        itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
        itemRender.zLevel = 0.0F;
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
        mc.fontRenderer.drawStringWithShadow(s, (x + 19 - 2 - mc.fontRenderer.getStringWidth(s)), (y + 9), 16777215);
        float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
        float red = 1.0F - green;
        int dmg = 100 - (int)(red * 100.0F);
        drawStringWithShadow(dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, ColourHolder.toHex((int)(red * 255.0F), (int)(green * 255.0F), 0));
      } 
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
    } 
  }
  
  public void drawInventory(int x, int y) {
    if (this.Inventory.getValue()) {
      GlStateManager.enableAlpha();
      mc.renderEngine.bindTexture(this.resource);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 162, 54);
      GlStateManager.disableAlpha();
      GlStateManager.clear(256);
      NonNullList<ItemStack> items = (Minecraft.getMinecraft()).player.inventory.mainInventory;
      for (int size = items.size(), item = 9; item < size; item++) {
        int slotX = x + 1 + item % 9 * 18;
        int slotY = y + 1 + (item / 9 - 1) * 18;
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY);
        RenderHelper.disableStandardItemLighting();
      } 
    } 
  }
  
  public int getPing() {
    int p = -1;
    if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
      p = -1;
    } else {
      p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
    } 
    return p;
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
  
  private int getWidth(String s) {
    if (customFont.getValue())
      return GameSenseMod.fontRenderer.getStringWidth(s); 
    return mc.fontRenderer.getStringWidth(s);
  }
  
  public String getPotionString(PotionEffect effect) {
    Potion potion = effect.getPotion();
    return I18n.format(potion.getName(), new Object[0]) + " " + (effect.getAmplifier() + 1) + " Â§f" + Potion.getPotionDurationString(effect, 1.0F);
  }
  
  public ChatFormatting getColoredPotionString(PotionEffect effect) {
    Potion potion = effect.getPotion();
    String format = I18n.format(potion.getName(), new Object[0]);
    switch (format) {
      case "Jump Boost":
      case "Speed":
        return ChatFormatting.AQUA;
      case "Resistance":
      case "Strength":
        return ChatFormatting.RED;
      case "Wither":
      case "Slowness":
      case "Weakness":
        return ChatFormatting.BLACK;
      case "Absorption":
        return ChatFormatting.BLUE;
      case "Haste":
      case "Fire Resistance":
        return ChatFormatting.GOLD;
      case "Regeneration":
        return ChatFormatting.LIGHT_PURPLE;
      case "Night Vision":
      case "Poison":
        return ChatFormatting.GREEN;
    } 
    return ChatFormatting.WHITE;
  }
}
