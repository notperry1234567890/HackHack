package com.gamesense.api.util;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class GSColor extends Color {
  public GSColor(int rgb) {
    super(rgb);
  }
  
  public GSColor(int rgba, boolean hasalpha) {
    super(rgba, hasalpha);
  }
  
  public GSColor(int r, int g, int b) {
    super(r, g, b);
  }
  
  public GSColor(int r, int g, int b, int a) {
    super(r, g, b, a);
  }
  
  public GSColor(Color color) {
    super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }
  
  public GSColor(GSColor color, int a) {
    super(color.getRed(), color.getGreen(), color.getBlue(), a);
  }
  
  public static GSColor fromHSB(float hue, float saturation, float brightness) {
    return new GSColor(Color.getHSBColor(hue, saturation, brightness));
  }
  
  public float getHue() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
  }
  
  public float getSaturation() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
  }
  
  public float getBrightness() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
  }
  
  public void glColor() {
    GL11.glColor4f(getRed() / 255.0F, getGreen() / 255.0F, getBlue() / 255.0F, getAlpha() / 255.0F);
  }
}
