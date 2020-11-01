//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.frame;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.clickgui.buttons.BooleanComponent;
import com.gamesense.client.clickgui.buttons.DoubleComponent;
import com.gamesense.client.clickgui.buttons.IntegerComponent;
import com.gamesense.client.clickgui.buttons.KeybindComponent;
import com.gamesense.client.clickgui.buttons.ModeComponent;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Buttons extends Component {
  public Module mod;
  
  public Frames parent;
  
  public int offset;
  
  private boolean isHovered;
  
  private final ArrayList<Component> subcomponents;
  
  public boolean open;
  
  private final int height;
  
  private static final ResourceLocation opengui = new ResourceLocation("minecraft:opengui.png");
  
  private static final ResourceLocation closedgui = new ResourceLocation("minecraft:closedgui.png");
  
  public Buttons(Module mod, Frames parent, int offset) {
    this.mod = mod;
    this.parent = parent;
    this.offset = offset;
    this.subcomponents = new ArrayList<>();
    this.open = false;
    this.height = 16;
    int opY = offset + 16;
    if ((GameSenseMod.getInstance()).settingsManager.getSettingsForMod(mod) != null && !(GameSenseMod.getInstance()).settingsManager.getSettingsForMod(mod).isEmpty())
      for (Setting s : (GameSenseMod.getInstance()).settingsManager.getSettingsForMod(mod)) {
        switch (s.getType()) {
          case MODE:
            this.subcomponents.add(new ModeComponent((Setting.Mode)s, this, mod, opY));
            opY += 16;
          case BOOLEAN:
            this.subcomponents.add(new BooleanComponent((Setting.Boolean)s, this, opY));
            opY += 16;
          case DOUBLE:
            this.subcomponents.add(new DoubleComponent((Setting.Double)s, this, opY));
            opY += 16;
          case INT:
            this.subcomponents.add(new IntegerComponent((Setting.Integer)s, this, opY));
            opY += 16;
        } 
      }  
    this.subcomponents.add(new KeybindComponent(this, opY));
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
    int opY = this.offset + 16;
    for (Component comp : this.subcomponents) {
      comp.setOff(opY);
      opY += 16;
    } 
  }
  
  public void renderComponent() {
    if (ColorMain.rainbow.getValue()) {
      ClickGUI.color = Rainbow.getColorWithOpacity(ClickGuiModule.opacity.getValue()).getRGB();
    } else {
      ClickGUI.color = (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), ClickGuiModule.opacity.getValue())).getRGB();
    } 
    Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset + 1, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + 16 + this.offset, this.isHovered ? (this.mod.isEnabled() ? ClickGUI.color : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).darker().darker().getRGB()) : (this.mod.isEnabled() ? ClickGUI.color : (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB()));
    Gui.drawRect(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + 1, (new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50)).getRGB());
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.mod.getName(), this.parent.getX() + 2, this.parent.getY() + this.offset + 2 + 2, -1);
    if (this.subcomponents.size() > 1)
      if (ClickGuiModule.icon.getValue().equalsIgnoreCase("Image")) {
        FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.open ? "" : "", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, -1);
        if (this.open) {
          drawOpenRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
        } else {
          drawClosedRender(this.parent.getX() + this.parent.getWidth() - 13, this.parent.getY() + this.offset + 2 + 2);
        } 
      } else {
        FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.open ? "-" : "+", this.parent.getX() + this.parent.getWidth() - 10, this.parent.getY() + this.offset + 2 + 2, -1);
      }  
    if (this.open && !this.subcomponents.isEmpty())
      for (Component comp : this.subcomponents)
        comp.renderComponent();  
  }
  
  public int getHeight() {
    if (this.open)
      return 16 * (this.subcomponents.size() + 1); 
    return 16;
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.isHovered = isMouseOnButton(mouseX, mouseY);
    if (!this.subcomponents.isEmpty())
      for (Component comp : this.subcomponents)
        comp.updateComponent(mouseX, mouseY);  
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0)
      this.mod.toggle(); 
    if (isMouseOnButton(mouseX, mouseY) && button == 1) {
      this.open = !this.open;
      this.parent.refresh();
    } 
    for (Component comp : this.subcomponents)
      comp.mouseClicked(mouseX, mouseY, button); 
  }
  
  public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    for (Component comp : this.subcomponents)
      comp.mouseReleased(mouseX, mouseY, mouseButton); 
  }
  
  public void keyTyped(char typedChar, int key) {
    for (Component comp : this.subcomponents)
      comp.keyTyped(typedChar, key); 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset);
  }
  
  public void drawOpenRender(int x, int y) {
    GlStateManager.enableAlpha();
    this.mc.getTextureManager().bindTexture(opengui);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    Gui.drawScaledCustomSizeModalRect(x, y, 0.0F, 0.0F, 256, 256, 10, 10, 256.0F, 256.0F);
    GL11.glPopMatrix();
    GlStateManager.disableAlpha();
    GlStateManager.clear(256);
  }
  
  public void drawClosedRender(int x, int y) {
    GlStateManager.enableAlpha();
    this.mc.getTextureManager().bindTexture(closedgui);
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    GL11.glPushMatrix();
    Gui.drawScaledCustomSizeModalRect(x, y, 0.0F, 0.0F, 256, 256, 10, 10, 256.0F, 256.0F);
    GL11.glPopMatrix();
    GlStateManager.disableAlpha();
    GlStateManager.clear(256);
  }
}
