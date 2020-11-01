//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.HackHackRPC;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;

public class RPCModule extends Module {
  public RPCModule() {
    super("DiscordRPC", Module.Category.Misc);
    setDrawn(false);
  }
  
  public void onEnable() {
    HackHackRPC.init();
    if (mc.player != null)
      Command.sendClientMessage(ChatFormatting.WHITE + "Discord RPC " + ChatFormatting.GREEN + "started!"); 
  }
  
  public void onDisable() {
    HackHackRPC.shutdown();
    Command.sendClientMessage(ChatFormatting.WHITE + "Discord RPC " + ChatFormatting.RED + "shutdown!");
  }
}
