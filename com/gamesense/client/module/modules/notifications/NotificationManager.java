//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.notifications;

import com.gamesense.client.GameSenseMod;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NotificationManager {
  private ArrayList<Notification> notifications = new ArrayList<>();
  
  public ArrayList<Notification> getNotifications() {
    return this.notifications;
  }
  
  public void addNotification(String text, long duration) {
    getNotifications().add(new Notification(text, duration));
  }
  
  public void renderNotifications() {
    ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
    float neededY = (scaledResolution.getScaledHeight() - 12);
    int i = 0;
    GameSenseMod.getInstance();
    for (; i < GameSenseMod.notificationManager.getNotifications().size(); i++) {
      GameSenseMod.getInstance();
      ((Notification)GameSenseMod.notificationManager.getNotifications().get(i)).renderNotification(neededY -= 30.0F);
    } 
  }
}
