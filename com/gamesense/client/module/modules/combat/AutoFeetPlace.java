//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.BlockInteractionHelper;
import com.gamesense.api.util.Wrapper;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoFeetPlace extends Module {
  private Setting.Boolean triggerable;
  
  private Setting.Integer timeoutTicks;
  
  private Setting.Integer blocksPerTick;
  
  private Setting.Boolean rotate;
  
  private Setting.Boolean hybrid;
  
  private Setting.Boolean announceUsage;
  
  private final Vec3d[] surroundTargets;
  
  private int playerHotbarSlot;
  
  private int lastHotbarSlot;
  
  private int offsetStep;
  
  private int totalTickRuns;
  
  private boolean isSneaking;
  
  private boolean flag;
  
  private int yHeight;
  
  public AutoFeetPlace() {
    super("Surround", Module.Category.Combat);
    this.surroundTargets = new Vec3d[] { new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 0.0D) };
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
    this.offsetStep = 0;
    this.totalTickRuns = 0;
    this.isSneaking = false;
    this.flag = false;
  }
  
  public void setup() {
    this.triggerable = registerBoolean("Triggerable", "Triggerable", true);
    this.timeoutTicks = registerInteger("TimeoutTicks", "TimeoutTicks", 20, 1, 100);
    this.blocksPerTick = registerInteger("Blocks per Tick", "BlocksperTick", 4, 1, 9);
    this.rotate = registerBoolean("Rotate", "Rotate", true);
    this.hybrid = registerBoolean("Hybrid", "Hybrid", true);
    this.announceUsage = registerBoolean("Announce Usage", "AnnounceUsage", true);
  }
  
  protected void onEnable() {
    this.flag = false;
    if (mc.player == null) {
      disable();
      return;
    } 
    this.playerHotbarSlot = (Wrapper.getPlayer()).inventory.currentItem;
    this.lastHotbarSlot = -1;
    this.yHeight = (int)Math.round(mc.player.posY);
    if (this.announceUsage.getValue())
      Command.sendClientMessage("§aSurround turned ON!"); 
  }
  
  protected void onDisable() {
    if (mc.player == null)
      return; 
    if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1)
      (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot; 
    if (this.isSneaking) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      this.isSneaking = false;
    } 
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
    if (this.announceUsage.getValue())
      Command.sendClientMessage("§cSurround turned OFF!"); 
  }
  
  public void onUpdate() {
    if (mc.player == null)
      return; 
    if (this.hybrid.getValue() && 
      (int)Math.round(mc.player.posY) != this.yHeight)
      disable(); 
    if (this.triggerable.getValue() && this.totalTickRuns >= this.timeoutTicks.getValue()) {
      this.totalTickRuns = 0;
      disable();
      return;
    } 
    int blocksPlaced = 0;
    while (blocksPlaced < this.blocksPerTick.getValue()) {
      if (this.offsetStep >= this.surroundTargets.length) {
        this.offsetStep = 0;
        break;
      } 
      BlockPos offsetPos = new BlockPos(this.surroundTargets[this.offsetStep]);
      BlockPos targetPos = (new BlockPos(mc.player.getPositionVector())).add(offsetPos.x, offsetPos.y, offsetPos.z);
      boolean shouldTryToPlace = true;
      if (!Wrapper.getWorld().getBlockState(targetPos).getMaterial().isReplaceable())
        shouldTryToPlace = false; 
      for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(targetPos))) {
        if (entity instanceof net.minecraft.entity.item.EntityItem || entity instanceof net.minecraft.entity.item.EntityXPOrb)
          continue; 
        shouldTryToPlace = false;
      } 
      if (shouldTryToPlace && placeBlock(targetPos))
        blocksPlaced++; 
      this.offsetStep++;
    } 
    if (blocksPlaced > 0 && this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
      (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot;
      this.lastHotbarSlot = this.playerHotbarSlot;
    } 
    this.totalTickRuns++;
  }
  
  private boolean placeBlock(BlockPos pos) {
    if (!mc.world.getBlockState(pos).getMaterial().isReplaceable())
      return false; 
    if (!BlockInteractionHelper.checkForNeighbours(pos))
      return false; 
    EnumFacing[] arrayOfEnumFacing;
    int i;
    byte b;
    for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) {
      EnumFacing side = arrayOfEnumFacing[b];
      BlockPos neighbor = pos.offset(side);
      EnumFacing side2 = side.getOpposite();
      if (!BlockInteractionHelper.canBeClicked(neighbor)) {
        b++;
        continue;
      } 
      int obiSlot = findObiInHotbar();
      if (obiSlot == -1) {
        disable();
        return false;
      } 
      if (this.lastHotbarSlot != obiSlot) {
        (Wrapper.getPlayer()).inventory.currentItem = obiSlot;
        this.lastHotbarSlot = obiSlot;
      } 
      Block neighborPos;
      if (BlockInteractionHelper.blackList.contains(neighborPos = mc.world.getBlockState(neighbor).getBlock())) {
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
        this.isSneaking = true;
      } 
      Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
      if (this.rotate.getValue())
        BlockInteractionHelper.faceVectorPacketInstant(hitVec); 
      mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      return true;
    } 
    return false;
  }
  
  private int findObiInHotbar() {
    int slot = -1;
    for (int i = 0; i < 9; ) {
      ItemStack stack = (Wrapper.getPlayer()).inventory.getStackInSlot(i);
      Block block;
      if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock) || !(block = ((ItemBlock)stack.getItem()).getBlock() instanceof net.minecraft.block.BlockObsidian)) {
        i++;
        continue;
      } 
      slot = i;
    } 
    return slot;
  }
}
