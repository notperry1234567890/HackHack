//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

public class BedAura extends Module {
  Setting.Integer range;
  
  Setting.Boolean announceUsage;
  
  Setting.Integer placedelay;
  
  private int playerHotbarSlot;
  
  private int lastHotbarSlot;
  
  private EntityPlayer closestTarget;
  
  private String lastTickTargetName;
  
  private int bedSlot;
  
  private BlockPos placeTarget;
  
  private float rotVar;
  
  private int blocksPlaced;
  
  private double diffXZ;
  
  private boolean firstRun;
  
  private boolean nowTop;
  
  public BedAura() {
    super("BedAura", Module.Category.Combat);
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
    this.bedSlot = -1;
    this.nowTop = false;
  }
  
  public void setup() {
    this.range = registerInteger("Range", "Range", 7, 0, 9);
    this.placedelay = registerInteger("Place Delay", "PlaceDelay", 15, 8, 20);
    this.announceUsage = registerBoolean("Announce Usage", "Announce Usage", true);
  }
  
  public void onEnable() {
    if (mc.player == null) {
      toggle();
      return;
    } 
    MinecraftForge.EVENT_BUS.register(this);
    this.firstRun = true;
    this.blocksPlaced = 0;
    this.playerHotbarSlot = mc.player.inventory.currentItem;
    this.lastHotbarSlot = -1;
  }
  
  public void onDisable() {
    if (mc.player == null)
      return; 
    MinecraftForge.EVENT_BUS.unregister(this);
    if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1)
      mc.player.inventory.currentItem = this.playerHotbarSlot; 
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
    if (this.announceUsage.getValue())
      Command.sendClientMessage(ChatFormatting.RED + "Bedaura Disabled!"); 
    this.blocksPlaced = 0;
  }
  
  public void onUpdate() {
    if (mc.player == null)
      return; 
    if (mc.player.dimension == 0) {
      Command.sendClientMessage("You are in the overworld!");
      toggle();
    } 
    try {
      findClosestTarget();
    } catch (NullPointerException nullPointerException) {}
    if (this.closestTarget == null && mc.player.dimension != 0 && 
      this.firstRun) {
      this.firstRun = false;
      if (this.announceUsage.getValue())
        Command.sendClientMessage(ChatFormatting.GREEN + "Bedaura Enabled! " + TextFormatting.WHITE + "waiting for target."); 
    } 
    if (this.firstRun && this.closestTarget != null && mc.player.dimension != 0) {
      this.firstRun = false;
      this.lastTickTargetName = this.closestTarget.getName();
      if (this.announceUsage.getValue())
        Command.sendClientMessage(ChatFormatting.GREEN + "Bedaura Enabled! " + TextFormatting.WHITE + "target: " + TextFormatting.WHITE + this.lastTickTargetName); 
    } 
    if (this.closestTarget != null && this.lastTickTargetName != null && 
      !this.lastTickTargetName.equals(this.closestTarget.getName())) {
      this.lastTickTargetName = this.closestTarget.getName();
      if (this.announceUsage.getValue())
        Command.sendClientMessage(TextFormatting.GREEN + "Bedaura" + TextFormatting.WHITE + " New target: " + ChatFormatting.WHITE.toString() + this.lastTickTargetName); 
    } 
    try {
      this.diffXZ = mc.player.getPositionVector().distanceTo(this.closestTarget.getPositionVector());
    } catch (NullPointerException nullPointerException) {}
    try {
      if (this.closestTarget != null) {
        this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0D, 1.0D, 0.0D));
        this.nowTop = false;
        this.rotVar = 90.0F;
        BlockPos block1 = this.placeTarget;
        if (!canPlaceBed(block1)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0D, 1.0D, 0.0D));
          this.rotVar = -90.0F;
          this.nowTop = false;
        } 
        BlockPos block2 = this.placeTarget;
        if (!canPlaceBed(block2)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0D, 1.0D, 1.0D));
          this.rotVar = 180.0F;
          this.nowTop = false;
        } 
        BlockPos block3 = this.placeTarget;
        if (!canPlaceBed(block3)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0D, 1.0D, -1.0D));
          this.rotVar = 0.0F;
          this.nowTop = false;
        } 
        BlockPos block4 = this.placeTarget;
        if (!canPlaceBed(block4)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0D, 2.0D, -1.0D));
          this.rotVar = 0.0F;
          this.nowTop = true;
        } 
        BlockPos blockt1 = this.placeTarget;
        if (this.nowTop && !canPlaceBed(blockt1)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(-1.0D, 2.0D, 0.0D));
          this.rotVar = -90.0F;
        } 
        BlockPos blockt2 = this.placeTarget;
        if (this.nowTop && !canPlaceBed(blockt2)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(0.0D, 2.0D, 1.0D));
          this.rotVar = 180.0F;
        } 
        BlockPos blockt3 = this.placeTarget;
        if (this.nowTop && !canPlaceBed(blockt3)) {
          this.placeTarget = new BlockPos(this.closestTarget.getPositionVector().add(1.0D, 2.0D, 0.0D));
          this.rotVar = 90.0F;
        } 
      } 
      mc.world.loadedTileEntityList.stream()
        .filter(e -> e instanceof net.minecraft.tileentity.TileEntityBed)
        .filter(e -> (mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()) <= this.range.getValue()))
        .sorted(Comparator.comparing(e -> Double.valueOf(mc.player.getDistance(e.getPos().getX(), e.getPos().getY(), e.getPos().getZ()))))
        .forEach(bed -> {
            if (mc.player.dimension != 0)
              mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(bed.getPos(), EnumFacing.UP, EnumHand.OFF_HAND, 0.0F, 0.0F, 0.0F)); 
          });
      if (mc.player.ticksExisted % this.placedelay.getValue() == 0 && this.closestTarget != null) {
        findBeds();
        mc.player.ticksExisted++;
        doDaMagic();
      } 
    } catch (NullPointerException npe) {
      npe.printStackTrace();
    } 
  }
  
  private void doDaMagic() {
    if (this.diffXZ <= this.range.getValue()) {
      for (int i = 0; i < 9 && 
        this.bedSlot == -1; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack.getItem() instanceof net.minecraft.item.ItemBed) {
          this.bedSlot = i;
          if (i != -1)
            mc.player.inventory.currentItem = this.bedSlot; 
          break;
        } 
      } 
      this.bedSlot = -1;
      if (this.blocksPlaced == 0 && mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() instanceof net.minecraft.item.ItemBed) {
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.rotVar, 0.0F, mc.player.onGround));
        placeBlock(new BlockPos((Vec3i)this.placeTarget), EnumFacing.DOWN);
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        this.blocksPlaced = 1;
        this.nowTop = false;
      } 
      this.blocksPlaced = 0;
    } 
  }
  
  private void findBeds() {
    if ((mc.currentScreen == null || !(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer)) && 
      mc.player.inventory.getStackInSlot(0).getItem() != Items.BED)
      for (int i = 9; i < 36; i++) {
        if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
          mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, (EntityPlayer)mc.player);
          break;
        } 
      }  
  }
  
  private boolean canPlaceBed(BlockPos pos) {
    return ((mc.world.getBlockState(pos).getBlock() == Blocks.AIR || mc.world.getBlockState(pos).getBlock() == Blocks.BED) && mc.world
      .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty());
  }
  
  private void findClosestTarget() {
    List<EntityPlayer> playerList = mc.world.playerEntities;
    this.closestTarget = null;
    for (EntityPlayer target : playerList) {
      if (target == mc.player)
        continue; 
      if (Friends.isFriend(target.getName()))
        continue; 
      if (!isLiving((Entity)target))
        continue; 
      if (target.getHealth() <= 0.0F)
        continue; 
      if (this.closestTarget == null) {
        this.closestTarget = target;
        continue;
      } 
      if (mc.player.getDistance((Entity)target) < mc.player.getDistance((Entity)this.closestTarget))
        this.closestTarget = target; 
    } 
  }
  
  private void placeBlock(BlockPos pos, EnumFacing side) {
    BlockPos neighbour = pos.offset(side);
    EnumFacing opposite = side.getOpposite();
    Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    mc.player.swingArm(EnumHand.MAIN_HAND);
  }
  
  public static boolean isLiving(Entity e) {
    return e instanceof net.minecraft.entity.EntityLivingBase;
  }
}
