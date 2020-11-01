//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.mixin.mixins.IRenderManager;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.BlockInteractionHelper;
import com.gamesense.api.util.EntityUtil;
import com.gamesense.api.util.RenderUtil;
import com.gamesense.api.util.Timer;
import com.gamesense.client.module.Module;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class CrystalAura extends Module {
  Setting.Boolean autoSwitch;
  
  Setting.Double walls;
  
  Setting.Boolean place;
  
  Setting.Double range;
  
  Setting.Integer facePlace;
  
  Setting.Boolean spoofRotations;
  
  Setting.Double maxSelfDmg;
  
  Setting.Double minDmg;
  
  Setting.Double placeRange;
  
  Setting.Integer hitDelay;
  
  Setting.Boolean renderDamage;
  
  Setting.Boolean rainbow;
  
  Setting.Double enemyRange;
  
  Setting.Integer espR;
  
  Setting.Integer espG;
  
  Setting.Integer espB;
  
  Setting.Integer espOR;
  
  Setting.Integer espOG;
  
  Setting.Integer espOB;
  
  Setting.Integer outlineW;
  
  Setting.Boolean rotate;
  
  Setting.Integer espA;
  
  Setting.Boolean slabRender;
  
  static Setting.Mode mode;
  
  Setting.Mode daThing;
  
  public static EntityPlayer target2;
  
  BlockPos render;
  
  BlockPos pos;
  
  String damageString;
  
  Timer breakTimer;
  
  boolean mainhand;
  
  boolean offhand;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> packetReceiveListener;
  
  public CrystalAura() {
    super("CrystalAura", Module.Category.Combat);
    this.pos = null;
    this.breakTimer = new Timer();
    this.mainhand = false;
    this.offhand = false;
    this.packetReceiveListener = new Listener(event -> {
          if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
              for (Entity e : (Minecraft.getMinecraft()).world.loadedEntityList) {
                if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D)
                  e.setDead(); 
              }  
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    ArrayList<String> placeModes = new ArrayList<>();
    placeModes.add("Normal");
    placeModes.add("1.13");
    ArrayList<String> modes = new ArrayList<>();
    modes.add("BREAKPLACE");
    modes.add("PLACEBREAK");
    this.daThing = registerMode("huh", "huh", modes, "BREAKPLACE");
    mode = registerMode("PlaceMode", "PlaceMode", placeModes, "Normal");
    this.place = registerBoolean("Place", "Place", true);
    this.rotate = registerBoolean("Rotate", "Rotate", true);
    this.spoofRotations = registerBoolean("SpoofRotations", "SpoofRotations", true);
    this.autoSwitch = registerBoolean("AutoSwitch", "AutoSwitch", false);
    this.hitDelay = registerInteger("HitDelayMS", "HitDelayMS", 0, 0, 600);
    this.range = registerDouble("HitRange", "HitRange", 4.5D, 0.0D, 6.0D);
    this.walls = registerDouble("WallRange", "WallRange", 3.5D, 0.0D, 4.0D);
    this.enemyRange = registerDouble("EnemyRange", "EnemyRange", 13.0D, 5.0D, 15.0D);
    this.placeRange = registerDouble("PlaceRange", "PlaceRange", 4.5D, 0.0D, 6.0D);
    this.maxSelfDmg = registerDouble("MaxSelfDamage", "MaxSelfDamage", 8.0D, 0.0D, 36.0D);
    this.minDmg = registerDouble("MinDmg", "MinDmg", 4.0D, 0.0D, 20.0D);
    this.facePlace = registerInteger("FaceplaceHp", "FaceplaceHp", 8, 0, 36);
    this.espR = registerInteger("Red", "Red", 255, 0, 255);
    this.espG = registerInteger("Green", "Green", 0, 0, 255);
    this.espB = registerInteger("Blue", "Blue", 255, 0, 255);
    this.espOR = registerInteger("RedO", "RedO", 255, 0, 255);
    this.espOG = registerInteger("GreenO", "GreenO", 0, 0, 255);
    this.espOB = registerInteger("BlueO", "BlueO", 255, 0, 255);
    this.outlineW = registerInteger("OutlineWidth", "OutlineWidth", 2, 1, 10);
    this.espA = registerInteger("Alpha", "Alpha", 28, 0, 255);
    this.slabRender = registerBoolean("SlabRender", "SlabRender", true);
    this.rainbow = registerBoolean("Rainbow", "Rainbow", true);
    this.renderDamage = registerBoolean("RenderDamage", "RenderDamage", false);
  }
  
  public void onUpdate() {
    doDaTHING();
  }
  
  void doDaTHING() {
    switch (this.daThing.getValue()) {
      case "BREAKPLACE":
        daThing();
        gloop();
        break;
      case "PLACEBREAK":
        gloop();
        daThing();
        break;
    } 
  }
  
  void daThing() {
    EntityEnderCrystal crystal = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance(c)))).orElse(null);
    if (crystal != null && mc.player.getDistance((Entity)crystal) <= this.range.getValue() && 
      this.breakTimer.passedMs(this.hitDelay.getValue())) {
      mc.playerController.attackEntity((EntityPlayer)mc.player, (Entity)crystal);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      this.breakTimer.reset();
    } 
  }
  
  void gloop() {
    double dmg = 0.5D;
    this.mainhand = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL);
    this.offhand = (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL);
    List<EntityPlayer> entities = (List<EntityPlayer>)mc.world.playerEntities.stream().filter(entityPlayer -> (entityPlayer != mc.player && !Friends.isFriend(entityPlayer.getName()))).collect(Collectors.toList());
    for (EntityPlayer entity2 : entities) {
      if (entity2.getHealth() <= 0.0F || mc.player.getDistance((Entity)entity2) > this.enemyRange.getValue())
        continue; 
      for (BlockPos blockPos : possiblePlacePositions((float)this.placeRange.getValue(), true)) {
        double d = calcDmg(blockPos, entity2);
        double self = calcDmg(blockPos, (EntityPlayer)mc.player);
        if ((d < this.minDmg.getValue() && entity2.getHealth() + entity2.getAbsorptionAmount() > this.facePlace.getValue()) || this.maxSelfDmg.getValue() <= self || d <= dmg)
          continue; 
        dmg = d;
        this.pos = blockPos;
        target2 = entity2;
      } 
    } 
    if (dmg == 0.5D) {
      this.render = null;
      return;
    } 
    if (this.place.getValue() && (
      this.offhand || this.mainhand)) {
      this.render = this.pos;
      placeCrystalOnBlock(this.pos, this.offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
      this.damageString = String.valueOf(dmg);
    } 
  }
  
  public static void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
    RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5D, pos.getY() - 0.5D, pos.getZ() + 0.5D));
    EnumFacing facing = (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;
    mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0F, 0.0F, 0.0F));
  }
  
  public void onWorldRender(RenderEvent event) {
    if (this.render != null && target2 != null) {
      float[] hue = { (float)(System.currentTimeMillis() % 2520L) / 2520.0F };
      int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
      int r = rgb >> 16 & 0xFF;
      int g = rgb >> 8 & 0xFF;
      int b = rgb & 0xFF;
      AxisAlignedBB bb = new AxisAlignedBB(this.render.getX() - (mc.getRenderManager()).viewerPosX, this.render.getY() - (mc.getRenderManager()).viewerPosY + 1.0D, this.render.getZ() - (mc.getRenderManager()).viewerPosZ, (this.render.getX() + 1) - (mc.getRenderManager()).viewerPosX, this.render.getY() + (this.slabRender.getValue() ? 1.1D : 0.0D) - (mc.getRenderManager()).viewerPosY, (this.render.getZ() + 1) - (mc.getRenderManager()).viewerPosZ);
      if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(bb.minX + (mc.getRenderManager()).viewerPosX, bb.minY + (mc.getRenderManager()).viewerPosY, bb.minZ + (mc.getRenderManager()).viewerPosZ, bb.maxX + (mc.getRenderManager()).viewerPosX, bb.maxY + (mc.getRenderManager()).viewerPosY, bb.maxZ + (mc.getRenderManager()).viewerPosZ))) {
        RenderUtil.drawESP(bb, this.rainbow.getValue() ? r : this.espR.getValue(), this.rainbow.getValue() ? g : this.espG.getValue(), this.rainbow.getValue() ? b : this.espB.getValue(), this.espA.getValue());
        RenderUtil.drawESPOutline(bb, this.rainbow.getValue() ? r : this.espOR.getValue(), this.rainbow.getValue() ? g : this.espOG.getValue(), this.rainbow.getValue() ? b : this.espOB.getValue(), 255.0F, this.outlineW.getValue());
        if (this.renderDamage.getValue()) {
          double posX = this.render.getX() - ((IRenderManager)mc.getRenderManager()).getRenderPosX();
          double posY = this.render.getY() - ((IRenderManager)mc.getRenderManager()).getRenderPosY();
          double posZ = this.render.getZ() - ((IRenderManager)mc.getRenderManager()).getRenderPosZ();
          RenderUtil.renderTag(this.damageString, posX + 0.5D, posY - 0.3D, posZ + 0.5D, (new Color(255, 255, 255, 255)).getRGB());
        } 
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
      } 
    } 
  }
  
  public float calcDmg(BlockPos b, EntityPlayer target) {
    return EntityUtil.calculateDamage(b.getX() + 0.5D, (b.getY() + 1), b.getZ() + 0.5D, (Entity)target);
  }
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public static List<BlockPos> possiblePlacePositions(float placeRange, boolean specialEntityCheck) {
    NonNullList<BlockPos> positions = NonNullList.create();
    positions.addAll((Collection)BlockInteractionHelper.getSphere(getPlayerPos(), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, specialEntityCheck)).collect(Collectors.toList()));
    return (List<BlockPos>)positions;
  }
  
  public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck) {
    BlockPos boost = blockPos.add(0, 1, 0);
    BlockPos boost2 = blockPos.add(0, 2, 0);
    try {
      if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
        return false; 
      if (!mode.getValue().equalsIgnoreCase("1.13")) {
        if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR)
          return false; 
      } else if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
        return false;
      } 
      if (specialEntityCheck) {
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
          if (entity instanceof EntityEnderCrystal)
            continue; 
          return false;
        } 
        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
          if (entity instanceof EntityEnderCrystal)
            continue; 
          return false;
        } 
      } else {
        return (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
      } 
    } catch (Exception ignored) {
      return false;
    } 
    return true;
  }
  
  public void onDisable() {
    this.render = null;
    target2 = null;
  }
  
  public String getHudInfo() {
    if (target2 != null)
      return "§7[§a" + target2.getName() + "§7]"; 
    return "§7[§cNo target!§7]";
  }
}
