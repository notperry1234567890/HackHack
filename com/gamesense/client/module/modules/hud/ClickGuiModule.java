//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.misc.Announcer;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiModule extends Module {
  public ClickGuiModule INSTANCE;
  
  public Setting.Boolean customFont;
  
  public static Setting.Integer opacity;
  
  public static Setting.Integer scrollSpeed;
  
  public static Setting.Mode icon;
  
  public ClickGuiModule() {
    super("ClickGUI", Module.Category.HUD);
    setBind(24);
    setDrawn(false);
    this.INSTANCE = this;
  }
  
  public void setup() {
    ArrayList<String> icons = new ArrayList<>();
    icons.add("Font");
    icons.add("Image");
    opacity = registerInteger("Opacity", "Opacity", 200, 50, 255);
    scrollSpeed = registerInteger("Scroll Speed", "Scroll Speed", 10, 1, 20);
    icon = registerMode("Icon", "Icons", icons, "Image");
  }
  
  public void onEnable() {
    mc.displayGuiScreen((GuiScreen)(GameSenseMod.getInstance()).clickGUI);
    if (((Announcer)ModuleManager.getModuleByName("Announcer")).clickGui.getValue() && ModuleManager.isModuleEnabled("Announcer") && mc.player != null)
      if (((Announcer)ModuleManager.getModuleByName("Announcer")).clientSide.getValue()) {
        Command.sendClientMessage(Announcer.guiMessage);
      } else {
        mc.player.sendChatMessage(Announcer.guiMessage);
      }  
    disable();
  }
  
  private void drawStringWithShadow(String text, int x, int y, int color) {
    if (this.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(text, x, y, color);
    } else {
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
    } 
  }
}
