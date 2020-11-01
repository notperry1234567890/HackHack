//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.gui.Gui;

public class ModeComponent extends Component {
  private boolean hovered;
  
  private final Buttons parent;
  
  private final Setting.Mode set;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  private final Module mod;
  
  private int modeIndex;
  
  public ModeComponent(Setting.Mode set, Buttons button, Module mod, int offset) {
    this.set = set;
    this.parent = button;
    this.mod = mod;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
    this.modeIndex = 0;
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void renderComponent() {
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB() : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = isMouseOnButton(mouseX, mouseY);
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
      int maxIndex = this.set.getModes().size() - 1;
      this.modeIndex++;
      if (this.modeIndex > maxIndex)
        this.modeIndex = 0; 
      this.set.setValue(this.set.getModes().get(this.modeIndex));
    } 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16);
  }
}
