//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;

public class TextRadar extends Module {
  Setting.Boolean sortUp;
  
  Setting.Boolean sortRight;
  
  Setting.Integer radarX;
  
  Setting.Integer radarY;
  
  Setting.Mode display;
  
  int sort;
  
  int playerCount;
  
  TextFormatting friendcolor;
  
  TextFormatting distancecolor;
  
  TextFormatting healthcolor;
  
  public TextRadar() {
    super("TextRadar", Module.Category.HUD);
  }
  
  public void setup() {
    ArrayList<String> displayModes = new ArrayList<>();
    displayModes.add("All");
    displayModes.add("Friend");
    displayModes.add("Enemy");
    this.display = registerMode("Display", "Display", displayModes, "All");
    this.sortUp = registerBoolean("Sort Up", "SortUp", false);
    this.sortRight = registerBoolean("Sort Right", "SortRight", false);
    this.radarX = registerInteger("X", "X", 0, 0, 1000);
    this.radarY = registerInteger("Y", "Y", 50, 0, 1000);
  }
  
  public void onRender() {
    if (this.sortUp.getValue()) {
      this.sort = -1;
    } else {
      this.sort = 1;
    } 
    this.playerCount = 0;
    mc.world.loadedEntityList.stream()
      .filter(e -> e instanceof EntityPlayer)
      .filter(e -> (e != mc.player))
      .forEach(e -> {
          if (Friends.isFriend(e.getName())) {
            this.friendcolor = ColorMain.getFriendColor();
          } else if (Enemies.isEnemy(e.getName())) {
            this.friendcolor = ColorMain.getEnemyColor();
          } else {
            this.friendcolor = TextFormatting.GRAY;
          } 
          if (((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount() <= 5.0F)
            this.healthcolor = TextFormatting.RED; 
          if (((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount() > 5.0F && ((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount() < 15.0F)
            this.healthcolor = TextFormatting.YELLOW; 
          if (((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount() >= 15.0F)
            this.healthcolor = TextFormatting.GREEN; 
          if (mc.player.getDistance(e) < 20.0F)
            this.distancecolor = TextFormatting.RED; 
          if (mc.player.getDistance(e) >= 20.0F && mc.player.getDistance(e) < 50.0F)
            this.distancecolor = TextFormatting.YELLOW; 
          if (mc.player.getDistance(e) >= 50.0F)
            this.distancecolor = TextFormatting.GREEN; 
          if (this.display.getValue().equalsIgnoreCase("Friend") && !Friends.isFriend(e.getName()))
            return; 
          if (this.display.getValue().equalsIgnoreCase("Enemy") && !Enemies.isEnemy(e.getName()))
            return; 
          if (this.sortUp.getValue()) {
            if (this.sortRight.getValue()) {
              drawStringWithShadow(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]", this.radarX.getValue() - getWidth(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]"), this.radarY.getValue() + this.playerCount * 10, -1);
            } else {
              drawStringWithShadow(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]", this.radarX.getValue(), this.radarY.getValue() + this.playerCount * 10, -1);
            } 
            this.playerCount++;
          } else {
            if (this.sortRight.getValue()) {
              drawStringWithShadow(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]", this.radarX.getValue() - getWidth(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]"), this.radarY.getValue() + this.playerCount * -10, -1);
            } else {
              drawStringWithShadow(TextFormatting.GRAY + "[" + this.healthcolor + (int)(((EntityPlayer)e).getHealth() + ((EntityPlayer)e).getAbsorptionAmount()) + TextFormatting.GRAY + "] " + this.friendcolor + e.getName() + TextFormatting.GRAY + " [" + this.distancecolor + (int)mc.player.getDistance(e) + TextFormatting.GRAY + "]", this.radarX.getValue(), this.radarY.getValue() + this.playerCount * -10, -1);
            } 
            this.playerCount++;
          } 
        });
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
