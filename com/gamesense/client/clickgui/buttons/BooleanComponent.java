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
import java.awt.Color;
import net.minecraft.client.gui.Gui;

public class BooleanComponent extends Component {
  private boolean hovered;
  
  private final Setting.Boolean op;
  
  private final Buttons parent;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  public BooleanComponent(Setting.Boolean option, Buttons button, int offset) {
    this.op = option;
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
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? (this.op.getValue() ? ClickGUI.color : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB()) : (this.op.getValue() ? ClickGUI.color : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB()));
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.op.getName(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = isMouseOnButton(mouseX, mouseY);
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open)
      this.op.setValue(!this.op.getValue()); 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16);
  }
}
