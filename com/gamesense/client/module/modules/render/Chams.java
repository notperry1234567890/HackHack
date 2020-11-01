package com.gamesense.client.module.modules.render;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;

public class Chams extends Module {
  public static Setting.Integer width;
  
  public static Setting.Integer red;
  
  public static Setting.Integer green;
  
  public static Setting.Integer blue;
  
  public static Setting.Integer alpha;
  
  public static Setting.Integer invisRed;
  
  public static Setting.Integer invisGreen;
  
  public static Setting.Integer invisBlue;
  
  public static Setting.Integer invisAlpha;
  
  public Chams() {
    super("Chams", Module.Category.Render);
  }
  
  public void setup() {
    width = registerInteger("Width", "Width", 2, 1, 10);
    red = registerInteger("InvisibleRed", "InvisibleRed", 255, 0, 255);
    green = registerInteger("InvisibleGreen", "InvisibleGreen", 0, 0, 255);
    blue = registerInteger("InvisibleBlue", "InvisibleBlue", 255, 0, 255);
    alpha = registerInteger("InvisibleAlpha", "InvisibleAlpha", 255, 0, 255);
    invisRed = registerInteger("VisibleRed", "VisibleRed", 0, 0, 255);
    invisGreen = registerInteger("VisibleGreen", "VisibleGreen", 0, 0, 255);
    invisBlue = registerInteger("VisibleBlue", "VisibleBlue", 0, 0, 255);
    invisAlpha = registerInteger("VisibleAlpha", "VisibleAlpha", 200, 0, 255);
  }
}
