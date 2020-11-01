//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.Wrapper;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;

public class Homies extends Module {
  Setting.Boolean customFont;
  
  Setting.Integer xx;
  
  Setting.Integer yy;
  
  public static String friends;
  
  private String str;
  
  private Setting.Mode mode;
  
  public Homies() {
    super("Homies", Module.Category.HUD);
    setDrawn(false);
  }
  
  public void setup() {
    this.xx = registerInteger("X", "X", 100, 0, 1000);
    this.yy = registerInteger("Y", "Y", 100, 0, 1000);
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Homies");
    modes.add("Friends");
    this.mode = registerMode("Mode", "Mode", modes, "Homies");
  }
  
  public void onRender() {
    int y = 2;
    if (HUD.customFont.getValue()) {
      GameSenseMod.fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + this.mode.getValue(), this.xx.getValue(), (this.yy.getValue() - 10), 16777215);
    } else {
      (Wrapper.getMinecraft()).fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + this.mode.getValue(), this.xx.getValue(), (this.yy.getValue() - 10), 16777215);
    } 
    for (Object o : mc.world.getLoadedEntityList()) {
      if (o instanceof EntityPlayer && ((EntityPlayer)o).getName() != mc.player.getName() && Friends.isFriend(((EntityPlayer)o).getName())) {
        friends = ((EntityPlayer)o).getGameProfile().getName();
        this.str = " " + friends;
        if (HUD.customFont.getValue()) {
          GameSenseMod.fontRenderer.drawStringWithShadow(this.str, this.xx.getValue(), (y + this.yy.getValue()), 16755200);
        } else {
          (Wrapper.getMinecraft()).fontRenderer.drawStringWithShadow(this.str, this.xx.getValue(), (y + this.yy.getValue()), 16755200);
        } 
        y += 12;
      } 
    } 
  }
}
