//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class AutoReply extends Module {
  public AutoReply() {
    super("AutoReply", Module.Category.Misc);
    this.listener = new Listener(event -> {
          if (event.getMessage().getUnformattedText().contains("whispers: ") && !event.getMessage().getUnformattedText().startsWith(mc.player.getName())) {
            mc.player.sendChatMessage("/r " + reply);
          } else if (event.getMessage().getUnformattedText().contains("whispers: I don't speak to newfags!") && !event.getMessage().getUnformattedText().startsWith(mc.player.getName())) {
            return;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  private static String reply = "I don't speak to newfags!";
  
  @EventHandler
  private final Listener<ClientChatReceivedEvent> listener;
  
  public static String getReply() {
    return reply;
  }
  
  public static void setReply(String r) {
    reply = r;
  }
  
  public void onEnable() {
    GameSenseMod.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    GameSenseMod.EVENT_BUS.unsubscribe(this);
  }
}
