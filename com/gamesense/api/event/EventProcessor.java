//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.event;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.PlayerJoinEvent;
import com.gamesense.api.event.events.PlayerLeaveEvent;
import com.gamesense.api.event.events.Render3DEvent;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.command.Command;
import com.gamesense.client.command.CommandManager;
import com.gamesense.client.macro.Macro;
import com.gamesense.client.module.ModuleManager;
import com.google.common.collect.Maps;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class EventProcessor {
  public static EventProcessor INSTANCE;
  
  Minecraft mc = Minecraft.getMinecraft();
  
  CommandManager commandManager = new CommandManager();
  
  float hue = 0.0F;
  
  Color c;
  
  int rgb;
  
  int speed = 2;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> receiveListener;
  
  private final Map<String, String> uuidNameCache;
  
  public int getRgb() {
    return this.rgb;
  }
  
  public Color getC() {
    return this.c;
  }
  
  public void setRainbowSpeed(int s) {
    this.speed = s;
  }
  
  public int getRainbowSpeed() {
    return this.speed;
  }
  
  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
    int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
    int red = rgb >> 16 & 0xFF;
    int green = rgb >> 8 & 0xFF;
    int blue = rgb & 0xFF;
    hue[0] = hue[0] + 0.02F;
    this.c = new Color(red, green, blue);
    if (this.mc.player != null)
      ModuleManager.onUpdate(); 
  }
  
  @SubscribeEvent
  public void onWorldRender(RenderWorldLastEvent event) {
    if (event.isCanceled())
      return; 
    ModuleManager.onWorldRender(event);
  }
  
  @SubscribeEvent
  public void onRender3D(Render3DEvent event) {
    if (event.isCanceled())
      return; 
    ModuleManager.onRender3D(event);
  }
  
  @SubscribeEvent
  public void onRender(RenderGameOverlayEvent.Post event) {
    GameSenseMod.EVENT_BUS.post(event);
    if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
      ModuleManager.onRender(); 
  }
  
  @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
  public void onKeyInput(InputEvent.KeyInputEvent event) {
    if (Keyboard.getEventKeyState()) {
      if (Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == 0)
        return; 
      ModuleManager.onBind(Keyboard.getEventKey());
      (GameSenseMod.getInstance()).macroManager.getMacros().forEach(m -> {
            if (m.getKey() == Keyboard.getEventKey())
              m.onMacro(); 
          });
    } 
  }
  
  @SubscribeEvent
  public void onMouseInput(InputEvent.MouseInputEvent event) {
    if (Mouse.getEventButtonState())
      GameSenseMod.EVENT_BUS.post(event); 
  }
  
  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public void onChatSent(ClientChatEvent event) {
    if (event.getMessage().startsWith(Command.getPrefix())) {
      event.setCanceled(true);
      try {
        this.mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
        this.commandManager.callCommand(event.getMessage().substring(1));
      } catch (Exception e) {
        e.printStackTrace();
        Command.sendClientMessage(ChatFormatting.DARK_RED + "Error: " + e.getMessage());
      } 
    } 
  }
  
  @SubscribeEvent
  public void onRenderScreen(RenderGameOverlayEvent.Text event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onChatReceived(ClientChatReceivedEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onRenderBlockOverlay(RenderBlockOverlayEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onLivingDamage(LivingDamageEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onLivingEntityUseItemFinish(LivingEntityUseItemEvent.Finish event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onInputUpdate(InputUpdateEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onPlayerPush(PlayerSPPushOutOfBlocksEvent event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onWorldUnload(WorldEvent.Unload event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  @SubscribeEvent
  public void onWorldLoad(WorldEvent.Load event) {
    GameSenseMod.EVENT_BUS.post(event);
  }
  
  public EventProcessor() {
    this.receiveListener = new Listener(event -> {
          if (event.getPacket() instanceof SPacketPlayerListItem) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem)event.getPacket();
            if (packet.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER)
              for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                if (playerData.getProfile().getId() != this.mc.session.getProfile().getId())
                  (new Thread(())).start(); 
              }  
            if (packet.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER)
              for (SPacketPlayerListItem.AddPlayerData playerData : packet.getEntries()) {
                if (playerData.getProfile().getId() != this.mc.session.getProfile().getId())
                  (new Thread(())).start(); 
              }  
          } 
        }new java.util.function.Predicate[0]);
    this.uuidNameCache = Maps.newConcurrentMap();
    INSTANCE = this;
  }
  
  public String resolveName(String uuid) {
    uuid = uuid.replace("-", "");
    if (this.uuidNameCache.containsKey(uuid))
      return this.uuidNameCache.get(uuid); 
    String url = "https://api.mojang.com/user/profiles/" + uuid + "/names";
    try {
      String nameJson = IOUtils.toString(new URL(url));
      if (nameJson != null && nameJson.length() > 0) {
        JSONArray jsonArray = (JSONArray)JSONValue.parseWithException(nameJson);
        if (jsonArray != null) {
          JSONObject latestName = (JSONObject)jsonArray.get(jsonArray.size() - 1);
          if (latestName != null)
            return latestName.get("name").toString(); 
        } 
      } 
    } catch (IOException|org.json.simple.parser.ParseException e) {
      e.printStackTrace();
    } 
    return null;
  }
  
  public void init() {
    GameSenseMod.EVENT_BUS.subscribe(this);
    MinecraftForge.EVENT_BUS.register(this);
  }
}
