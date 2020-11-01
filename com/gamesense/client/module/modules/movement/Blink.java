//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class Blink extends Module {
  EntityOtherPlayerMP entity;
  
  private final Queue<Packet> packets;
  
  @EventHandler
  private Listener<PacketEvent.Send> packetSendListener;
  
  public Blink() {
    super("Blink", Module.Category.Movement);
    this.packets = new ConcurrentLinkedQueue<>();
    this.packetSendListener = new Listener(event -> {
          Packet packet = event.getPacket();
          if (packet instanceof net.minecraft.network.play.client.CPacketChatMessage || packet instanceof net.minecraft.network.play.client.CPacketConfirmTeleport || packet instanceof net.minecraft.network.play.client.CPacketKeepAlive || packet instanceof net.minecraft.network.play.client.CPacketTabComplete || packet instanceof net.minecraft.network.play.client.CPacketClientStatus)
            return; 
          this.packets.add(packet);
          event.cancel();
        }new java.util.function.Predicate[0]);
  }
  
  public void onEnable() {
    GameSenseMod.EVENT_BUS.subscribe(this);
    this.entity = new EntityOtherPlayerMP((World)mc.world, mc.getSession().getProfile());
    this.entity.copyLocationAndAnglesFrom((Entity)mc.player);
    this.entity.rotationYaw = mc.player.rotationYaw;
    this.entity.rotationYawHead = mc.player.rotationYawHead;
    mc.world.addEntityToWorld(666, (Entity)this.entity);
  }
  
  public void onDisable() {
    GameSenseMod.EVENT_BUS.unsubscribe(this);
    if (this.entity != null)
      mc.world.removeEntity((Entity)this.entity); 
    if (this.packets.size() > 0) {
      for (Packet packet : this.packets)
        mc.player.connection.sendPacket(packet); 
      this.packets.clear();
    } 
  }
  
  public String getHudInfo() {
    return "" + this.packets.size();
  }
}
