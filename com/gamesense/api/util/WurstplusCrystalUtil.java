//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WurstplusCrystalUtil {
  static final Minecraft mc = Minecraft.getMinecraft();
  
  public static List<BlockPos> possiblePlacePositions(float placeRange, boolean thirteen, boolean specialEntityCheck) {
    NonNullList<BlockPos> positions = NonNullList.create();
    positions.addAll((Collection)getSphere(getPlayerPos((EntityPlayer)mc.player), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> canPlaceCrystal(pos, thirteen, specialEntityCheck)).collect(Collectors.toList()));
    return (List<BlockPos>)positions;
  }
  
  public static BlockPos getPlayerPos(EntityPlayer player) {
    return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
  }
  
  public static List<BlockPos> getSphere(BlockPos pos, float r, int h, boolean hollow, boolean sphere, int plus_y) {
    List<BlockPos> circleblocks = new ArrayList<>();
    int cx = pos.getX();
    int cy = pos.getY();
    int cz = pos.getZ();
    for (int x = cx - (int)r; x <= cx + r; x++) {
      for (int z = cz - (int)r; z <= cz + r; ) {
        int y = sphere ? (cy - (int)r) : cy;
        for (;; z++) {
          if (y < (sphere ? (cy + r) : (cy + h))) {
            double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
            if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
              BlockPos l = new BlockPos(x, y + plus_y, z);
              circleblocks.add(l);
            } 
            y++;
            continue;
          } 
        } 
      } 
    } 
    return circleblocks;
  }
  
  public static boolean canPlaceCrystal(BlockPos blockPos, boolean thirteen, boolean specialEntityCheck) {
    BlockPos boost = blockPos.add(0, 1, 0);
    BlockPos boost2 = blockPos.add(0, 2, 0);
    BlockPos final_boost = blockPos.add(0, 3, 0);
    try {
      if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
        return false; 
      if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || (mc.world.getBlockState(boost2).getBlock() != Blocks.AIR && !thirteen))
        return false; 
      if (!specialEntityCheck)
        return (mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty()); 
      for (Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
        if (!(entity instanceof EntityEnderCrystal))
          return false; 
      } 
      for (Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2))) {
        if (!(entity instanceof EntityEnderCrystal))
          return false; 
      } 
      for (Object entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(final_boost))) {
        if (entity instanceof EntityEnderCrystal)
          return false; 
      } 
    } catch (Exception ignored) {
      return false;
    } 
    return true;
  }
  
  public static boolean canPlaceCrystal(BlockPos pos) {
    Block block = mc.world.getBlockState(pos).getBlock();
    if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
      Block floor = mc.world.getBlockState(pos.add(0, 1, 0)).getBlock();
      Block ceil = mc.world.getBlockState(pos.add(0, 2, 0)).getBlock();
      if (floor == Blocks.AIR && ceil == Blocks.AIR)
        if (mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 1, 0))).isEmpty() && mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos.add(0, 2, 0))).isEmpty())
          return true;  
    } 
    return false;
  }
  
  public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
    if (entity == mc.player)
      if (mc.player.capabilities.isCreativeMode)
        return 0.0F;  
    float doubleExplosionSize = 12.0F;
    double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0D;
    Vec3d vec3d = new Vec3d(posX, posY, posZ);
    double blockDensity = 0.0D;
    try {
      blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
    } catch (Exception exception) {}
    double v = (1.0D - distancedsize) * blockDensity;
    float damage = (int)((v * v + v) / 2.0D * 7.0D * 12.0D + 1.0D);
    double finald = 1.0D;
    if (entity instanceof EntityLivingBase)
      finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.world, null, posX, posY, posZ, 6.0F, false, true)); 
    return (float)finald;
  }
  
  public static float getBlastReduction(EntityLivingBase entity, float damageI, Explosion explosion) {
    float damage = damageI;
    if (entity instanceof EntityPlayer) {
      EntityPlayer ep = (EntityPlayer)entity;
      DamageSource ds = DamageSource.causeExplosionDamage(explosion);
      damage = CombatRules.getDamageAfterAbsorb(damage, ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
      int k = 0;
      try {
        k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
      } catch (Exception exception) {}
      float f = MathHelper.clamp(k, 0.0F, 20.0F);
      damage *= 1.0F - f / 25.0F;
      if (entity.isPotionActive(MobEffects.RESISTANCE))
        damage -= damage / 4.0F; 
      damage = Math.max(damage, 0.0F);
      return damage;
    } 
    damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    return damage;
  }
  
  public static float getDamageMultiplied(float damage) {
    int diff = mc.world.getDifficulty().getId();
    return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
  }
  
  public static float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
    return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
  }
}
