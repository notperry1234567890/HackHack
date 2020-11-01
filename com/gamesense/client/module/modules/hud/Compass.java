//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.Wrapper;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;

public class Compass extends Module {
  Setting.Double scale;
  
  Color c;
  
  private static final double HALF_PI = 1.5707963267948966D;
  
  ScaledResolution resolution;
  
  public Compass() {
    super("Compass", Module.Category.HUD);
    this.resolution = new ScaledResolution(mc);
  }
  
  public void setup() {
    this.scale = registerDouble("Scale", "Scale", 3.0D, 1.0D, 5.0D);
  }
  
  public void onRender() {
    double centerX = this.resolution.getScaledWidth() * 1.11D;
    double centerY = this.resolution.getScaledHeight_double() * 1.8D;
    for (Direction dir : Direction.values()) {
      double rad = getPosOnCompass(dir);
      drawStringWithShadow(dir.name(), (int)(centerX + getX(rad)), (int)(centerY + getY(rad)), (dir == Direction.N) ? (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 255)).getRGB() : (new Color(255, 255, 255, 255)).getRGB());
    } 
  }
  
  private double getX(double rad) {
    return Math.sin(rad) * this.scale.getValue() * 10.0D;
  }
  
  private double getY(double rad) {
    double epicPitch = MathHelper.clamp((Wrapper.getRenderEntity()).rotationPitch + 30.0F, -90.0F, 90.0F);
    double pitchRadians = Math.toRadians(epicPitch);
    return Math.cos(rad) * Math.sin(pitchRadians) * this.scale.getValue() * 10.0D;
  }
  
  private static double getPosOnCompass(Direction dir) {
    double yaw = Math.toRadians(MathHelper.wrapDegrees((Wrapper.getRenderEntity()).rotationYaw));
    int index = dir.ordinal();
    return yaw + index * 1.5707963267948966D;
  }
  
  private enum Direction {
    N, W, S, E;
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (HUD.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
}
