//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.notifications;

import com.gamesense.api.util.RenderUtil;
import com.gamesense.api.util.TimerUtil;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
  private final TimerUtil timer;
  
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  private float x;
  
  private float oldX;
  
  private float y;
  
  private float oldY;
  
  private float width;
  
  private final String text;
  
  private final long stayTime;
  
  private boolean done;
  
  private float stayBar;
  
  Notification(String text, long stayTime) {
    this.x = ((new ScaledResolution(mc)).getScaledWidth() - 2);
    this.y = ((new ScaledResolution(mc)).getScaledHeight() - 2);
    this.text = text;
    this.stayTime = stayTime;
    this.timer = new TimerUtil();
    this.timer.reset();
    this.stayBar = (float)stayTime;
    this.done = false;
  }
  
  public void renderNotification(float prevY) {
    HUD hud = (HUD)ModuleManager.getModuleByName("HUD");
    float xSpeed = this.width / (Minecraft.getDebugFPS() / 4);
    float ySpeed = ((new ScaledResolution(mc)).getScaledHeight() - prevY) / (Minecraft.getDebugFPS() / 8);
    if (this.width != RenderUtil.getTextWidth(this.text, true) + 4.0F)
      this.width = RenderUtil.getTextWidth(this.text, true) + 4.0F; 
    if (this.done) {
      this.oldX = this.x;
      this.x += xSpeed;
      this.y += ySpeed;
    } 
    if (!done() && !this.done) {
      this.timer.reset();
      this.oldX = this.x;
      if (this.x <= ((new ScaledResolution(mc)).getScaledWidth() - 2) - this.width + xSpeed) {
        this.x = ((new ScaledResolution(mc)).getScaledWidth() - 2) - this.width;
      } else {
        this.x -= xSpeed;
      } 
    } else if (this.timer.reach(this.stayTime)) {
      this.done = true;
    } 
    if (this.x < ((new ScaledResolution(mc)).getScaledWidth() - 2) - this.width) {
      this.oldX = this.x;
      this.x += xSpeed;
    } 
    if (this.y != prevY) {
      if (this.y > prevY + ySpeed) {
        this.y -= ySpeed;
      } else {
        this.y = prevY;
      } 
    } else if (this.y < prevY) {
      this.oldY = this.y;
      this.y += ySpeed;
    } 
    if (done() && !this.done)
      this.stayBar = (float)this.timer.time(); 
    float finishedX = this.oldX + this.x - this.oldX;
    float finishedY = this.oldY + this.y - this.oldY;
    RenderUtil.drawRect(finishedX, finishedY, this.width, 27.0F, (new Color(21, 21, 21, 200)).getRGB());
    RenderUtil.drawText(this.text, finishedX + 2.0F, finishedY + 10.0F, -1, HUD.customFont.getValue());
    if (done())
      RenderUtil.drawRect(finishedX, finishedY + 26.0F, (this.width - 1.0F) / (float)this.stayTime * this.stayBar, 1.0F, (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 200)).getRGB()); 
    if (delete()) {
      GameSenseMod.getInstance();
      GameSenseMod.notificationManager.getNotifications().remove(this);
    } 
  }
  
  public boolean done() {
    return (this.x <= ((new ScaledResolution(mc)).getScaledWidth() - 2) - this.width);
  }
  
  public boolean delete() {
    return (this.x >= ((new ScaledResolution(mc)).getScaledWidth() - 2) && this.done);
  }
}
