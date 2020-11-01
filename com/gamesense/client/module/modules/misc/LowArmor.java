//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.Wrapper;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.HUD;
import java.awt.Color;
import net.minecraft.item.ItemStack;

public class LowArmor extends Module {
  Setting.Integer x;
  
  Setting.Integer y;
  
  Setting.Integer red;
  
  Setting.Integer green;
  
  Setting.Integer blue;
  
  Setting.Integer threshold;
  
  Color c;
  
  public LowArmor() {
    super("Durability Warning", Module.Category.Misc);
  }
  
  public void setup() {
    this.threshold = registerInteger("Percent", "Percent", 50, 0, 100);
    this.x = registerInteger("X", "X", 255, 0, 960);
    this.y = registerInteger("Y", "Y", 255, 0, 530);
    this.red = registerInteger("Red", "Red", 255, 0, 255);
    this.green = registerInteger("Green", "Green", 255, 0, 255);
    this.blue = registerInteger("Blue", "Blue", 255, 0, 255);
  }
  
  public void onRender() {
    float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
    int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
    int r = rgb >> 16 & 0xFF;
    int g = rgb >> 8 & 0xFF;
    int b = rgb & 0xFF;
    if (shouldMend(0) || shouldMend(1) || shouldMend(2) || shouldMend(3)) {
      String text = "Armor Durability Is Below " + this.threshold.getValue() + "%";
      int divider = getScale();
      drawStringWithShadow(text, this.x.getValue(), this.y.getValue(), (new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue())).getRGB());
    } 
  }
  
  private boolean shouldMend(int i) {
    return (((ItemStack)mc.player.inventory.armorInventory.get(i)).getMaxDamage() != 0 && 100 * ((ItemStack)mc.player.inventory.armorInventory.get(i)).getItemDamage() / ((ItemStack)mc.player.inventory.armorInventory.get(i)).getMaxDamage() > reverseNumber(this.threshold.getValue(), 1, 100));
  }
  
  public static int reverseNumber(int num, int min, int max) {
    return max + min - num;
  }
  
  public static int getScale() {
    int scaleFactor = 0;
    int scale = (Wrapper.getMinecraft()).gameSettings.guiScale;
    if (scale == 0)
      scale = 1000; 
    while (scaleFactor < scale && (Wrapper.getMinecraft()).displayWidth / (scaleFactor + 1) >= 320 && (Wrapper.getMinecraft()).displayHeight / (scaleFactor + 1) >= 240)
      scaleFactor++; 
    if (scaleFactor == 0)
      scaleFactor = 1; 
    return scaleFactor;
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (HUD.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
}
