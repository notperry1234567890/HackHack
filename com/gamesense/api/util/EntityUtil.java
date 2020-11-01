//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util;

import com.gamesense.api.players.friends.Friends;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityUtil {
  public static final Vec3d[] antiDropOffsetList;
  
  public static final Vec3d[] platformOffsetList;
  
  public static final Vec3d[] legOffsetList;
  
  public static final Vec3d[] OffsetList;
  
  public static final Vec3d[] antiStepOffsetList;
  
  public static final Vec3d[] antiScaffoldOffsetList;
  
  public static final Minecraft mc = Minecraft.getMinecraft();
  
  public static void attackEntity(Entity entity, boolean packet, boolean swingArm) {
    if (packet) {
      mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
    } else {
      mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
    } 
    if (swingArm)
      mc.player.swingArm(EnumHand.MAIN_HAND); 
  }
  
  public static Vec3d interpolateEntity(Entity entity, float time) {
    return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, partialTicks));
  }
  
  public static Vec3d getInterpolatedRenderPos(Entity entity, float partialTicks) {
    return getInterpolatedPos(entity, partialTicks).subtract((mc.getRenderManager()).renderPosX, (mc.getRenderManager()).renderPosY, (mc.getRenderManager()).renderPosZ);
  }
  
  public static Vec3d getInterpolatedRenderPos(Vec3d vec) {
    return (new Vec3d(vec.x, vec.y, vec.z)).subtract((mc.getRenderManager()).renderPosX, (mc.getRenderManager()).renderPosY, (mc.getRenderManager()).renderPosZ);
  }
  
  public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
    float doubleExplosionSize = 12.0F;
    double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
    Vec3d vec3d = new Vec3d(posX, posY, posZ);
    double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
    double v = (1.0D - distancedsize) * blockDensity;
    float damage = (int)((v * v + v) / 2.0D * 7.0D * doubleExplosionSize + 1.0D);
    double finald = 1.0D;
    if (entity instanceof EntityLivingBase)
      finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.world, null, posX, posY, posZ, 6.0F, false, true)); 
    return (float)finald;
  }
  
  private static float getDamageMultiplied(float damage) {
    int diff = mc.world.getDifficulty().getId();
    return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
  }
  
  public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
    if (entity instanceof EntityPlayer) {
      EntityPlayer ep = (EntityPlayer)entity;
      DamageSource ds = DamageSource.causeExplosionDamage(explosion);
      damage = CombatRules.getDamageAfterAbsorb(damage, ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
      int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
      float f = MathHelper.clamp(k, 0.0F, 20.0F);
      damage *= 1.0F - f / 25.0F;
      if (entity.isPotionActive(Objects.<Potion>requireNonNull(Potion.getPotionById(11))))
        damage -= damage / 4.0F; 
      return damage;
    } 
    damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    return damage;
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
    return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
    return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
  }
  
  public static boolean stopSneaking(boolean isSneaking) {
    if (isSneaking && mc.player != null)
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING)); 
    return false;
  }
  
  public static BlockPos getPlayerPos(EntityPlayer player) {
    return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
  }
  
  public static boolean isMobAggressive(Entity entity) {
    if (entity instanceof EntityPigZombie) {
      if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry())
        return true; 
    } else {
      if (entity instanceof EntityWolf)
        return (((EntityWolf)entity).isAngry() && !mc.player.equals(((EntityWolf)entity).getOwner())); 
      if (entity instanceof EntityEnderman)
        return ((EntityEnderman)entity).isScreaming(); 
    } 
    return isHostileMob(entity);
  }
  
  public static boolean isPassive(Entity entity) {
    return ((!(entity instanceof EntityWolf) || !((EntityWolf)entity).isAngry()) && (entity instanceof net.minecraft.entity.EntityAgeable || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null)));
  }
  
  public static boolean isNeutralMob(Entity entity) {
    return (entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman);
  }
  
  public static boolean isHostileMob(Entity entity) {
    return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity));
  }
  
  public static boolean isInHole(Entity entity) {
    return isBlockValid(new BlockPos(entity.posX, entity.posY, entity.posZ));
  }
  
  public static boolean isBlockValid(BlockPos blockPos) {
    return (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos));
  }
  
  public static boolean isObbyHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos pos : array) {
      IBlockState touchingState = mc.world.getBlockState(pos);
      if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN)
        return false; 
    } 
    return true;
  }
  
  public static boolean isBedrockHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos pos : array) {
      IBlockState touchingState = mc.world.getBlockState(pos);
      if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK)
        return false; 
    } 
    return true;
  }
  
  public static boolean isBothHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos pos : array) {
      IBlockState touchingState = mc.world.getBlockState(pos);
      if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN))
        return false; 
    } 
    return true;
  }
  
  public static double getDst(Vec3d vec) {
    return mc.player.getPositionVector().distanceTo(vec);
  }
  
  public static boolean isDrivenByPlayer(Entity entityIn) {
    return (mc.player != null && entityIn != null && entityIn.equals(mc.player.getRidingEntity()));
  }
  
  public static boolean isPlayer(Entity entity) {
    return entity instanceof EntityPlayer;
  }
  
  public static List<Vec3d> getOffsetList(int y, boolean floor) {
    List<Vec3d> offsets = new ArrayList<>();
    offsets.add(new Vec3d(-1.0D, y, 0.0D));
    offsets.add(new Vec3d(1.0D, y, 0.0D));
    offsets.add(new Vec3d(0.0D, y, -1.0D));
    offsets.add(new Vec3d(0.0D, y, 1.0D));
    if (floor)
      offsets.add(new Vec3d(0.0D, (y - 1), 0.0D)); 
    return offsets;
  }
  
  public static Vec3d[] getOffsets(int y, boolean floor) {
    List<Vec3d> offsets = getOffsetList(y, floor);
    Vec3d[] array = new Vec3d[offsets.size()];
    return offsets.<Vec3d>toArray(array);
  }
  
  public static Vec3d[] getTrapOffsets(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
    List<Vec3d> offsets = getTrapOffsetsList(antiScaffold, antiStep, legs, platform, antiDrop);
    Vec3d[] array = new Vec3d[offsets.size()];
    return offsets.<Vec3d>toArray(array);
  }
  
  public static List<Vec3d> getTrapOffsetsList(boolean antiScaffold, boolean antiStep, boolean legs, boolean platform, boolean antiDrop) {
    List<Vec3d> offsets = new ArrayList<>(getOffsetList(1, false));
    offsets.add(new Vec3d(0.0D, 2.0D, 0.0D));
    if (antiScaffold)
      offsets.add(new Vec3d(0.0D, 3.0D, 0.0D)); 
    if (antiStep)
      offsets.addAll(getOffsetList(2, false)); 
    if (legs)
      offsets.addAll(getOffsetList(0, false)); 
    if (platform) {
      offsets.addAll(getOffsetList(-1, false));
      offsets.add(new Vec3d(0.0D, -1.0D, 0.0D));
    } 
    if (antiDrop)
      offsets.add(new Vec3d(0.0D, -2.0D, 0.0D)); 
    return offsets;
  }
  
  public static Vec3d[] getHeightOffsets(int min, int max) {
    List<Vec3d> offsets = new ArrayList<>();
    for (int i = min; i <= max; i++)
      offsets.add(new Vec3d(0.0D, i, 0.0D)); 
    Vec3d[] array = new Vec3d[offsets.size()];
    return offsets.<Vec3d>toArray(array);
  }
  
  public static boolean isLiving(Entity entity) {
    return entity instanceof EntityLivingBase;
  }
  
  public static boolean isAlive(Entity entity) {
    return (isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0F);
  }
  
  public static boolean isDead(Entity entity) {
    return !isAlive(entity);
  }
  
  public static float getHealth(Entity entity) {
    if (isLiving(entity)) {
      EntityLivingBase livingBase = (EntityLivingBase)entity;
      return livingBase.getHealth() + livingBase.getAbsorptionAmount();
    } 
    return 0.0F;
  }
  
  public static float getHealth(Entity entity, boolean absorption) {
    if (isLiving(entity)) {
      EntityLivingBase livingBase = (EntityLivingBase)entity;
      return livingBase.getHealth() + (absorption ? livingBase.getAbsorptionAmount() : 0.0F);
    } 
    return 0.0F;
  }
  
  public static boolean canEntityFeetBeSeen(Entity entityIn) {
    return (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posX + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null);
  }
  
  public static boolean isntValid(Entity entity, double range) {
    return (entity == null || isDead(entity) || entity.equals(mc.player) || (entity instanceof EntityPlayer && Friends.isFriend(entity.getName())) || mc.player.getDistanceSq(entity) > MathUtil.square(range));
  }
  
  public static boolean isValid(Entity entity, double range) {
    return !isntValid(entity, range);
  }
  
  public static double getMaxSpeed() {
    double maxModifier = 0.2873D;
    if (mc.player.isPotionActive(Objects.<Potion>requireNonNull(Potion.getPotionById(1))))
      maxModifier *= 1.0D + 0.2D * (((PotionEffect)Objects.<PotionEffect>requireNonNull(mc.player.getActivePotionEffect(Objects.<Potion>requireNonNull(Potion.getPotionById(1))))).getAmplifier() + 1); 
    return maxModifier;
  }
  
  public static Color getColor(Entity entity, int red, int green, int blue, int alpha, boolean colorFriends) {
    Color color = new Color(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
    if (entity instanceof EntityPlayer && 
      colorFriends && Friends.isFriend(entity.getName()))
      color = new Color(0.33333334F, 1.0F, 1.0F, alpha / 255.0F); 
    return color;
  }
  
  public static void mutliplyEntitySpeed(Entity entity, double multiplier) {
    if (entity != null) {
      entity.motionX *= multiplier;
      entity.motionZ *= multiplier;
    } 
  }
  
  public static boolean isEntityMoving(Entity entity) {
    if (entity == null)
      return false; 
    if (entity instanceof EntityPlayer)
      return (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()); 
    return (entity.motionX != 0.0D || entity.motionY != 0.0D || entity.motionZ != 0.0D);
  }
  
  public static double getEntitySpeed(Entity entity) {
    if (entity != null) {
      double distTraveledLastTickX = entity.posX - entity.prevPosX;
      double distTraveledLastTickZ = entity.posZ - entity.prevPosZ;
      double speed = MathHelper.sqrt(distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ);
      return speed * 20.0D;
    } 
    return 0.0D;
  }
  
  public static boolean holding32k(EntityPlayer player) {
    return is32k(player.getHeldItemMainhand());
  }
  
  public static boolean is32k(ItemStack stack) {
    if (stack == null)
      return false; 
    if (stack.getTagCompound() == null)
      return false; 
    NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
    if (enchants == null)
      return false; 
    int i = 0;
    while (i < enchants.tagCount()) {
      NBTTagCompound enchant = enchants.getCompoundTagAt(i);
      if (enchant.getInteger("id") == 16) {
        int lvl = enchant.getInteger("lvl");
        if (lvl >= 42)
          return true; 
        break;
      } 
      i++;
    } 
    return false;
  }
  
  public static boolean simpleIs32k(ItemStack stack) {
    return (EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack) >= 1000);
  }
  
  public static void moveEntityStrafe(double speed, Entity entity) {
    if (entity != null) {
      MovementInput movementInput = mc.player.movementInput;
      double forward = movementInput.moveForward;
      double strafe = movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0D && strafe == 0.0D) {
        entity.motionX = 0.0D;
        entity.motionZ = 0.0D;
      } else {
        if (forward != 0.0D) {
          if (strafe > 0.0D) {
            yaw += ((forward > 0.0D) ? -45 : 45);
          } else if (strafe < 0.0D) {
            yaw += ((forward > 0.0D) ? 45 : -45);
          } 
          strafe = 0.0D;
          if (forward > 0.0D) {
            forward = 1.0D;
          } else if (forward < 0.0D) {
            forward = -1.0D;
          } 
        } 
        entity.motionX = forward * speed * Math.cos(Math.toRadians((yaw + 90.0F))) + strafe * speed * Math.sin(Math.toRadians((yaw + 90.0F)));
        entity.motionZ = forward * speed * Math.sin(Math.toRadians((yaw + 90.0F))) - strafe * speed * Math.cos(Math.toRadians((yaw + 90.0F)));
      } 
    } 
  }
  
  public static boolean rayTraceHitCheck(Entity entity, boolean shouldCheck) {
    return (!shouldCheck || mc.player.canEntityBeSeen(entity));
  }
  
  public static boolean isMoving() {
    return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
  }
  
  public static EntityPlayer getClosestEnemy(double distance) {
    EntityPlayer closest = null;
    for (EntityPlayer player : mc.world.playerEntities) {
      if (isntValid((Entity)player, distance))
        continue; 
      if (closest == null) {
        closest = player;
        continue;
      } 
      if (mc.player.getDistanceSq((Entity)player) >= mc.player.getDistanceSq((Entity)closest))
        continue; 
      closest = player;
    } 
    return closest;
  }
  
  public static boolean checkCollide() {
    return (!mc.player.isSneaking() && (mc.player.getRidingEntity() == null || (mc.player.getRidingEntity()).fallDistance < 3.0F) && mc.player.fallDistance < 3.0F);
  }
  
  public static BlockPos getPlayerPosWithEntity() {
    return new BlockPos((mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posX : mc.player.posX, (mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posY : mc.player.posY, (mc.player.getRidingEntity() != null) ? (mc.player.getRidingEntity()).posZ : mc.player.posZ);
  }
  
  public static double[] forward(double speed) {
    float forward = mc.player.movementInput.moveForward;
    float side = mc.player.movementInput.moveStrafe;
    float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
    if (forward != 0.0F) {
      if (side > 0.0F) {
        yaw += ((forward > 0.0F) ? -45 : 45);
      } else if (side < 0.0F) {
        yaw += ((forward > 0.0F) ? 45 : -45);
      } 
      side = 0.0F;
      if (forward > 0.0F) {
        forward = 1.0F;
      } else if (forward < 0.0F) {
        forward = -1.0F;
      } 
    } 
    double sin = Math.sin(Math.toRadians((yaw + 90.0F)));
    double cos = Math.cos(Math.toRadians((yaw + 90.0F)));
    double posX = forward * speed * cos + side * speed * sin;
    double posZ = forward * speed * sin - side * speed * cos;
    return new double[] { posX, posZ };
  }
  
  public static boolean isAboveBlock(Entity entity, BlockPos blockPos) {
    return (entity.posY >= blockPos.getY());
  }
  
  static {
    antiDropOffsetList = new Vec3d[] { new Vec3d(0.0D, -2.0D, 0.0D) };
    platformOffsetList = new Vec3d[] { new Vec3d(0.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(1.0D, -1.0D, 0.0D) };
    legOffsetList = new Vec3d[] { new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D) };
    OffsetList = new Vec3d[] { new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 2.0D, 0.0D) };
    antiStepOffsetList = new Vec3d[] { new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(0.0D, 2.0D, -1.0D) };
    antiScaffoldOffsetList = new Vec3d[] { new Vec3d(0.0D, 3.0D, 0.0D) };
  }
}
