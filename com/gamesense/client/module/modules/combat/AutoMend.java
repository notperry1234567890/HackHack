//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class AutoMend extends Module {
  int delay;
  
  Setting.Integer ThrowDelay;
  
  private final BlockPos[] surroundOffset;
  
  public AutoMend() {
    super("AutoMend", Module.Category.Combat);
    this.delay = 0;
    this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
  }
  
  public void setup() {
    this.ThrowDelay = registerInteger("Throw Delay", "throwDelay", 2, 1, 10);
  }
  
  public void onUpdate() {
    int ArmorDurability = getArmorDurability();
    boolean safe = isSafe();
    boolean AutoCrystal = ModuleManager.isModuleEnabled("AutoCrystal");
    BlockPos q = mc.player.getPosition();
    if (!AutoCrystal && mc.player.isSneaking() && safe && 0 < ArmorDurability) {
      this.delay++;
      if (this.delay % this.ThrowDelay.getValue() == 0) {
        mc.player.inventory.currentItem = findExpInHotbar();
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0F, 90.0F, true));
        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
      } 
    } else {
      this.delay = 0;
    } 
    super.onUpdate();
  }
  
  private int findExpInHotbar() {
    int slot = 0;
    for (int i = 0; i < 9; i++) {
      if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
        slot = i;
        break;
      } 
    } 
    return slot;
  }
  
  private boolean isSafe() {
    boolean safe = true;
    BlockPos playerPos = getPlayerPos();
    for (BlockPos offset : this.surroundOffset) {
      Block block = mc.world.getBlockState(playerPos.add((Vec3i)offset)).getBlock();
      if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
        safe = false;
        break;
      } 
    } 
    return safe;
  }
  
  private static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  private int getArmorDurability() {
    int TotalDurability = 0;
    for (ItemStack itemStack : mc.player.inventory.armorInventory)
      TotalDurability += itemStack.getItemDamage(); 
    return TotalDurability;
  }
}
