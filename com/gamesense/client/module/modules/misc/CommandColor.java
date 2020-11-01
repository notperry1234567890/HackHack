package com.gamesense.client.module.modules.misc;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;

public class CommandColor extends Module {
  public static Setting.Mode CommandColor;
  
  public CommandColor() {
    super("CommandColor", Module.Category.Misc);
  }
  
  public void setup() {
    ArrayList<String> colors = new ArrayList<>();
    colors.add("Black");
    colors.add("Dark Green");
    colors.add("Dark Red");
    colors.add("Gold");
    colors.add("Dark Gray");
    colors.add("Green");
    colors.add("Red");
    colors.add("Yellow");
    colors.add("Dark Blue");
    colors.add("Dark Aqua");
    colors.add("Dark Purple");
    colors.add("Gray");
    colors.add("Blue");
    colors.add("Aqua");
    colors.add("Light Purple");
    colors.add("White");
    CommandColor = registerMode("Color", "Color", colors, "Light Purple");
  }
  
  public static ChatFormatting getTextColor() {
    if (CommandColor.getValue().equalsIgnoreCase("Black"))
      return ChatFormatting.BLACK; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Green"))
      return ChatFormatting.DARK_GREEN; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Red"))
      return ChatFormatting.DARK_RED; 
    if (CommandColor.getValue().equalsIgnoreCase("Gold"))
      return ChatFormatting.GOLD; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Gray"))
      return ChatFormatting.DARK_GRAY; 
    if (CommandColor.getValue().equalsIgnoreCase("Green"))
      return ChatFormatting.GREEN; 
    if (CommandColor.getValue().equalsIgnoreCase("Red"))
      return ChatFormatting.RED; 
    if (CommandColor.getValue().equalsIgnoreCase("Yellow"))
      return ChatFormatting.YELLOW; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Blue"))
      return ChatFormatting.DARK_BLUE; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Aqua"))
      return ChatFormatting.DARK_AQUA; 
    if (CommandColor.getValue().equalsIgnoreCase("Dark Purple"))
      return ChatFormatting.DARK_PURPLE; 
    if (CommandColor.getValue().equalsIgnoreCase("Gray"))
      return ChatFormatting.GRAY; 
    if (CommandColor.getValue().equalsIgnoreCase("Blue"))
      return ChatFormatting.BLUE; 
    if (CommandColor.getValue().equalsIgnoreCase("Light Purple"))
      return ChatFormatting.LIGHT_PURPLE; 
    if (CommandColor.getValue().equalsIgnoreCase("White"))
      return ChatFormatting.WHITE; 
    if (CommandColor.getValue().equalsIgnoreCase("Aqua"))
      return ChatFormatting.AQUA; 
    return null;
  }
}
