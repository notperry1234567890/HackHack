//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;

public class HackHackRPC {
  private static final String ClientId = "759194829980303450";
  
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  private static final DiscordRPC rpc = DiscordRPC.INSTANCE;
  
  public static DiscordRichPresence presence = new DiscordRichPresence();
  
  private static String details;
  
  private static String state;
  
  public static void init() {
    DiscordEventHandlers handlers = new DiscordEventHandlers();
    handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
    rpc.Discord_Initialize("759194829980303450", handlers, true, "");
    presence.startTimestamp = System.currentTimeMillis() / 1000L;
    presence.details = "HackHack0.9.4";
    presence.state = "discord.gg/UbtWKMd";
    presence.largeImageKey = "large";
    presence.largeImageText = "HackHack 0.9.4";
    presence.smallImageKey = "small";
    presence.smallImageText = mc.getSession().getUsername();
    rpc.Discord_UpdatePresence(presence);
    (new Thread(() -> {
          while (!Thread.currentThread().isInterrupted()) {
            try {
              rpc.Discord_RunCallbacks();
              details = "HackHack0.9.4";
              state = "discord.gg/UbtWKMd";
              if (mc.isIntegratedServerRunning()) {
                details = "Singleplayer";
              } else if (mc.getCurrentServerData() != null) {
                if (!(mc.getCurrentServerData()).serverIP.equals(""))
                  details = (mc.getCurrentServerData()).serverIP; 
              } else {
                details = "Main Menu";
              } 
              if (!details.equals(presence.details) || !state.equals(presence.state))
                presence.startTimestamp = System.currentTimeMillis() / 1000L; 
              presence.details = details;
              presence.state = state;
              rpc.Discord_UpdatePresence(presence);
            } catch (Exception e2) {
              e2.printStackTrace();
            } 
            try {
              Thread.sleep(5000L);
            } catch (InterruptedException e3) {
              e3.printStackTrace();
            } 
          } 
        }"Discord-RPC-Callback-Handler")).start();
  }
  
  public static void shutdown() {
    rpc.Discord_Shutdown();
  }
}
