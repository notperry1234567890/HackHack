//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Notifications extends Module {
  Setting.Integer notX;
  
  Setting.Integer notY;
  
  Setting.Boolean sortUp;
  
  Setting.Boolean sortRight;
  
  public static Setting.Boolean disableChat;
  
  int sort;
  
  int notCount;
  
  int waitCounter;
  
  TextFormatting notColor;
  
  public Notifications() {
    super("Notifications", Module.Category.HUD);
  }
  
  public void setup() {
    disableChat = registerBoolean("No Chat Msg", "NoChatMsg", true);
    this.sortUp = registerBoolean("Sort Up", "SortUp", false);
    this.sortRight = registerBoolean("Sort Right", "SortRight", false);
    this.notX = registerInteger("X", "X", 0, 0, 1000);
    this.notY = registerInteger("Y", "Y", 50, 0, 1000);
  }
  
  static List<TextComponentString> list = new ArrayList<>();
  
  public void onUpdate() {
    if (this.waitCounter < 500) {
      this.waitCounter++;
      return;
    } 
    this.waitCounter = 0;
    if (list.size() > 0)
      list.remove(0); 
  }
  
  public void onRender() {
    if (this.sortUp.getValue()) {
      this.sort = -1;
    } else {
      this.sort = 1;
    } 
    this.notCount = 0;
    for (TextComponentString s : list) {
      this.notCount = list.indexOf(s) + 1;
      this.notColor = s.getStyle().getColor();
      if (this.sortUp.getValue()) {
        if (this.sortRight.getValue()) {
          drawStringWithShadow(s.getText(), this.notX.getValue() - getWidth(s.getText()), this.notY.getValue() + this.notCount * 10, -1);
          continue;
        } 
        drawStringWithShadow(s.getText(), this.notX.getValue(), this.notY.getValue() + this.notCount * 10, -1);
        continue;
      } 
      if (this.sortRight.getValue()) {
        drawStringWithShadow(s.getText(), this.notX.getValue() - getWidth(s.getText()), this.notY.getValue() + this.notCount * -10, -1);
        continue;
      } 
      drawStringWithShadow(s.getText(), this.notX.getValue(), this.notY.getValue() + this.notCount * -10, -1);
    } 
  }
  
  public static void addMessage(TextComponentString m) {
    if (list.size() < 3) {
      list.remove(m);
      list.add(m);
    } else {
      list.remove(0);
      list.remove(m);
      list.add(m);
    } 
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (HUD.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
  
  private int getWidth(String s) {
    if (HUD.customFont.getValue())
      return GameSenseMod.fontRenderer.getStringWidth(s); 
    return mc.fontRenderer.getStringWidth(s);
  }
}
