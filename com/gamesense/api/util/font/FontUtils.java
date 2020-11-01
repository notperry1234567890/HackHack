//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.font;

import com.gamesense.client.GameSenseMod;
import net.minecraft.client.Minecraft;

public class FontUtils {
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  public static float drawStringWithShadow(boolean customFont, String text, int x, int y, int color) {
    if (customFont)
      return GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color); 
    return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
  }
  
  public static int getStringWidth(boolean customFont, String str) {
    if (customFont)
      return GameSenseMod.fontRenderer.getStringWidth(str); 
    return mc.fontRenderer.getStringWidth(str);
  }
  
  public static int getFontHeight(boolean customFont) {
    if (customFont)
      return GameSenseMod.fontRenderer.getHeight(); 
    return mc.fontRenderer.FONT_HEIGHT;
  }
  
  public static float drawKeyStringWithShadow(boolean customFont, String text, int x, int y, int color) {
    if (customFont)
      return GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color); 
    return mc.fontRenderer.drawStringWithShadow(text, x, y, color);
  }
}
