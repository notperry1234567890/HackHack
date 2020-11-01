//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.TotemPopEvent;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.HashMap;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;

public class TotemCounter extends Module {
  private HashMap<String, Integer> popList;
  
  @EventHandler
  public Listener<TotemPopEvent> totemPopEvent;
  
  @EventHandler
  public Listener<PacketEvent.Receive> totemPopListener;
  
  public TotemCounter() {
    super("PopCounter", Module.Category.Misc);
    this.popList = new HashMap<>();
    this.totemPopEvent = new Listener(event -> {
          if (this.popList == null)
            this.popList = new HashMap<>(); 
          if (this.popList.get(event.getEntity().getName()) == null) {
            this.popList.put(event.getEntity().getName(), Integer.valueOf(1));
            Command.sendClientMessage(ChatFormatting.DARK_AQUA + event.getEntity().getName() + ChatFormatting.DARK_RED + " popped " + ChatFormatting.GOLD + '\001' + ChatFormatting.GOLD + " totem!");
            GameSenseMod.notificationManager.addNotification(event.getEntity().getName() + " popped " + '\001' + " totem!", 3000L);
          } else if (this.popList.get(event.getEntity().getName()) != null) {
            int popCounter = ((Integer)this.popList.get(event.getEntity().getName())).intValue();
            int newPopCounter = ++popCounter;
            this.popList.put(event.getEntity().getName(), Integer.valueOf(newPopCounter));
            Command.sendClientMessage(ChatFormatting.DARK_AQUA + event.getEntity().getName() + ChatFormatting.DARK_RED + " popped " + ChatFormatting.GOLD + newPopCounter + ChatFormatting.GOLD + " totems!");
            GameSenseMod.notificationManager.addNotification(event.getEntity().getName() + " popped " + newPopCounter + " totems!", 3000L);
          } 
        }new java.util.function.Predicate[0]);
    this.totemPopListener = new Listener(event -> {
          if (mc.world == null || mc.player == null)
            return; 
          if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35) {
              Entity entity = packet.getEntity((World)mc.world);
              GameSenseMod.EVENT_BUS.post(new TotemPopEvent(entity));
            } 
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void onUpdate() {
    for (EntityPlayer player : mc.world.playerEntities) {
      if (player.getHealth() <= 0.0F && this.popList.containsKey(player.getName())) {
        Command.sendClientMessage(ChatFormatting.DARK_AQUA + player.getName() + ChatFormatting.DARK_RED + " died after popping " + ChatFormatting.GOLD + this.popList.get(player.getName()) + ChatFormatting.GOLD + " totems!");
        GameSenseMod.notificationManager.addNotification(player.getName() + " died after popping " + this.popList.get(player.getName()) + " totems!", 3000L);
        this.popList.remove(player.getName(), this.popList.get(player.getName()));
      } 
    } 
  }
  
  public void onEnable() {
    GameSenseMod.EVENT_BUS.subscribe(this);
    this.popList = new HashMap<>();
  }
}
