//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class AutoGG extends Module {
  public static AutoGG INSTANCE;
  
  public AutoGG() {
    super("AutoGG", Module.Category.Misc);
    this.targetedPlayers = null;
    this.index = -1;
    this.sendListener = new Listener(event -> {
          if (mc.player != null) {
            if (this.targetedPlayers == null)
              this.targetedPlayers = new ConcurrentHashMap<>(); 
            if (event.getPacket() instanceof CPacketUseEntity) {
              CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
              if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                Entity targetEntity = cPacketUseEntity.getEntityFromWorld((World)mc.world);
                if (targetEntity instanceof EntityPlayer)
                  addTargetedPlayer(targetEntity.getName()); 
              } 
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.livingDeathEventListener = new Listener(event -> {
          if (mc.player != null) {
            if (this.targetedPlayers == null)
              this.targetedPlayers = new ConcurrentHashMap<>(); 
            EntityLivingBase entity = event.getEntityLiving();
            if (entity != null && entity instanceof EntityPlayer) {
              EntityPlayer player = (EntityPlayer)entity;
              if (player.getHealth() <= 0.0F) {
                String name = player.getName();
                if (shouldAnnounce(name))
                  doAnnounce(name); 
              } 
            } 
          } 
        }new java.util.function.Predicate[0]);
    INSTANCE = this;
  }
  
  static List<String> AutoGgMessages = new ArrayList<>();
  
  private ConcurrentHashMap targetedPlayers;
  
  int index;
  
  @EventHandler
  private final Listener<PacketEvent.Send> sendListener;
  
  @EventHandler
  private final Listener<LivingDeathEvent> livingDeathEventListener;
  
  public void onEnable() {
    this.targetedPlayers = new ConcurrentHashMap<>();
    GameSenseMod.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    this.targetedPlayers = null;
    GameSenseMod.EVENT_BUS.unsubscribe(this);
  }
  
  public void onUpdate() {
    if (this.targetedPlayers == null)
      this.targetedPlayers = new ConcurrentHashMap<>(); 
    Iterator<Entity> var1 = mc.world.getLoadedEntityList().iterator();
    while (var1.hasNext()) {
      Entity entity = var1.next();
      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer)entity;
        if (player.getHealth() <= 0.0F) {
          String name = player.getName();
          if (shouldAnnounce(name)) {
            doAnnounce(name);
            break;
          } 
        } 
      } 
    } 
    this.targetedPlayers.forEach((namex, timeout) -> {
          if (((Integer)timeout).intValue() <= 0) {
            this.targetedPlayers.remove(namex);
          } else {
            this.targetedPlayers.put(namex, Integer.valueOf(((Integer)timeout).intValue() - 1));
          } 
        });
  }
  
  private boolean shouldAnnounce(String name) {
    return this.targetedPlayers.containsKey(name);
  }
  
  private void doAnnounce(String name) {
    String message;
    this.targetedPlayers.remove(name);
    if (this.index >= AutoGgMessages.size() - 1)
      this.index = -1; 
    this.index++;
    if (AutoGgMessages.size() > 0) {
      message = AutoGgMessages.get(this.index);
    } else {
      message = "gg, HackHack owns me and all!";
    } 
    String messageSanitized = message.replaceAll("à¸¢à¸‡", "").replace("{name}", name);
    if (messageSanitized.length() > 255)
      messageSanitized = messageSanitized.substring(0, 255); 
    mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
  }
  
  public void addTargetedPlayer(String name) {
    if (!Objects.equals(name, mc.player.getName())) {
      if (this.targetedPlayers == null)
        this.targetedPlayers = new ConcurrentHashMap<>(); 
      this.targetedPlayers.put(name, Integer.valueOf(20));
    } 
  }
  
  public static void addAutoGgMessage(String s) {
    AutoGgMessages.add(s);
  }
  
  public static List<String> getAutoGgMessages() {
    return AutoGgMessages;
  }
}
