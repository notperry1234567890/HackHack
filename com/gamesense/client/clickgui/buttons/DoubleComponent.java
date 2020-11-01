//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.client.gui.Gui;

public class DoubleComponent extends Component {
  private boolean hovered;
  
  private final Setting.Double set;
  
  private final Buttons parent;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  private boolean dragging;
  
  private double renderWidth;
  
  public DoubleComponent(Setting.Double value, Buttons button, int offset) {
    this.dragging = false;
    this.set = value;
    this.parent = button;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
  }
  
  public void renderComponent() {
    if (ColorMain.rainbow.getValue()) {
      ClickGUI.color = Rainbow.getColorWithOpacity(ClickGuiModule.opacity.getValue()).getRGB();
    } else {
      ClickGUI.color = (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), ClickGuiModule.opacity.getValue())).getRGB();
    } 
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB() : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    int drag = (int)(this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth());
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 16, this.hovered ? ClickGUI.color : ClickGUI.color);
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = (isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY));
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
    double diff = Math.min(100, Math.max(0, mouseX - this.x));
    double min = this.set.getMin();
    double max = this.set.getMax();
    this.renderWidth = 100.0D * (this.set.getValue() - min) / (max - min);
    if (this.dragging)
      if (diff == 0.0D) {
        this.set.setValue(this.set.getMin());
      } else {
        double newValue = roundToPlace(diff / 100.0D * (max - min) + min, 2);
        this.set.setValue(newValue);
      }  
  }
  
  private static double roundToPlace(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open)
      this.dragging = true; 
    if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open)
      this.dragging = true; 
  }
  
  public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    this.dragging = false;
  }
  
  public boolean isMouseOnButtonD(int x, int y) {
    return (x > this.x && x < this.x + this.parent.parent.getWidth() / 2 + 1 && y > this.y && y < this.y + 16);
  }
  
  public boolean isMouseOnButtonI(int x, int y) {
    return (x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 16);
  }
}
