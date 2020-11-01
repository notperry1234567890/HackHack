//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends Component {
  private boolean hovered;
  
  private boolean binding;
  
  private final Buttons parent;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  public KeybindComponent(Buttons button, int offset) {
    this.parent = button;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void renderComponent() {
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, this.hovered ? (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB() : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB());
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB());
    FontUtils.drawKeyStringWithShadow(HUD.customFont.getValue(), this.binding ? "Key..." : ("Key: " + ChatFormatting.GRAY + Keyboard.getKeyName(this.parent.mod.getBind())), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = isMouseOnButton(mouseX, mouseY);
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open)
      this.binding = !this.binding; 
  }
  
  public void keyTyped(char typedChar, int key) {
    if (this.binding) {
      if (key == 211) {
        this.parent.mod.setBind(0);
      } else if (key == 1) {
        this.binding = false;
      } else {
        this.parent.mod.setBind(key);
      } 
      this.binding = false;
    } 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16);
  }
}
