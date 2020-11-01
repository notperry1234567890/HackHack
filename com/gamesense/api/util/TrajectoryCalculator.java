//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TrajectoryCalculator {
  public static ThrowingType getThrowType(EntityLivingBase entity) {
    if (entity.getHeldItem(EnumHand.MAIN_HAND).isEmpty())
      return ThrowingType.NONE; 
    ItemStack itemStack = entity.getHeldItem(EnumHand.MAIN_HAND);
    Item item = itemStack.getItem();
    if (item instanceof net.minecraft.item.ItemPotion) {
      if (itemStack.getItem() instanceof net.minecraft.item.ItemSplashPotion)
        return ThrowingType.POTION; 
    } else {
      if (item instanceof ItemBow && entity.isHandActive())
        return ThrowingType.BOW; 
      if (item instanceof net.minecraft.item.ItemExpBottle)
        return ThrowingType.EXPERIENCE; 
      if (item instanceof net.minecraft.item.ItemSnowball || item instanceof net.minecraft.item.ItemEgg || item instanceof net.minecraft.item.ItemEnderPearl)
        return ThrowingType.NORMAL; 
    } 
    return ThrowingType.NONE;
  }
  
  public enum ThrowingType {
    NONE, BOW, EXPERIENCE, POTION, NORMAL;
  }
  
  public static final class FlightPath {
    private EntityLivingBase shooter;
    
    public Vec3d position;
    
    private Vec3d motion;
    
    private float yaw;
    
    private float pitch;
    
    private AxisAlignedBB boundingBox;
    
    private boolean collided;
    
    private RayTraceResult target;
    
    private TrajectoryCalculator.ThrowingType throwingType;
    
    public FlightPath(EntityLivingBase entityLivingBase, TrajectoryCalculator.ThrowingType throwingType) {
      this.shooter = entityLivingBase;
      this.throwingType = throwingType;
      double[] ipos = TrajectoryCalculator.interpolate((Entity)this.shooter);
      setLocationAndAngles(ipos[0] + (Wrapper.getMinecraft().getRenderManager()).renderPosX, ipos[1] + this.shooter.getEyeHeight() + (Wrapper.getMinecraft().getRenderManager()).renderPosY, ipos[2] + (Wrapper.getMinecraft().getRenderManager()).renderPosZ, this.shooter.rotationYaw, this.shooter.rotationPitch);
      Vec3d startingOffset = new Vec3d((MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F), 0.1D, (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F));
      this.position = this.position.subtract(startingOffset);
      setPosition(this.position);
      this
        
        .motion = new Vec3d((-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F)), -MathHelper.sin(this.pitch / 180.0F * 3.1415927F), (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F)));
      setThrowableHeading(this.motion, getInitialVelocity());
    }
    
    public void onUpdate() {
      Vec3d prediction = this.position.add(this.motion);
      RayTraceResult blockCollision = this.shooter.getEntityWorld().rayTraceBlocks(this.position, prediction, false, true, false);
      if (blockCollision != null)
        prediction = blockCollision.hitVec; 
      onCollideWithEntity(prediction, blockCollision);
      if (this.target != null) {
        this.collided = true;
        setPosition(this.target.hitVec);
        return;
      } 
      if (this.position.y <= 0.0D) {
        this.collided = true;
        return;
      } 
      this.position = this.position.add(this.motion);
      float motionModifier = 0.99F;
      if (this.shooter.getEntityWorld().isMaterialInBB(this.boundingBox, Material.WATER))
        motionModifier = (this.throwingType == TrajectoryCalculator.ThrowingType.BOW) ? 0.6F : 0.8F; 
      this.motion = TrajectoryCalculator.mult(this.motion, motionModifier);
      this.motion = this.motion.subtract(0.0D, getGravityVelocity(), 0.0D);
      setPosition(this.position);
    }
    
    private void onCollideWithEntity(Vec3d prediction, RayTraceResult blockCollision) {
      Entity collidingEntity = null;
      double currentDistance = 0.0D;
      List<Entity> collisionEntities = this.shooter.world.getEntitiesWithinAABBExcludingEntity((Entity)this.shooter, this.boundingBox.expand(this.motion.x, this.motion.y, this.motion.z).expand(1.0D, 1.0D, 1.0D));
      for (Entity entity : collisionEntities) {
        if (!entity.canBeCollidedWith() && entity != this.shooter)
          continue; 
        float collisionSize = entity.getCollisionBorderSize();
        AxisAlignedBB expandedBox = entity.getEntityBoundingBox().expand(collisionSize, collisionSize, collisionSize);
        RayTraceResult objectPosition = expandedBox.calculateIntercept(this.position, prediction);
        if (objectPosition != null) {
          double distanceTo = this.position.distanceTo(objectPosition.hitVec);
          if (distanceTo < currentDistance || currentDistance == 0.0D) {
            collidingEntity = entity;
            currentDistance = distanceTo;
          } 
        } 
      } 
      if (collidingEntity != null) {
        this.target = new RayTraceResult(collidingEntity);
      } else {
        this.target = blockCollision;
      } 
    }
    
    private float getInitialVelocity() {
      ItemBow bow;
      int useDuration;
      float velocity;
      Item item = this.shooter.getHeldItem(EnumHand.MAIN_HAND).getItem();
      switch (this.throwingType) {
        case BOW:
          bow = (ItemBow)item;
          useDuration = bow.getMaxItemUseDuration(this.shooter.getHeldItem(EnumHand.MAIN_HAND)) - this.shooter.getItemInUseCount();
          velocity = useDuration / 20.0F;
          velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
          if (velocity > 1.0F)
            velocity = 1.0F; 
          return velocity * 2.0F * 1.5F;
        case POTION:
          return 0.5F;
        case EXPERIENCE:
          return 0.7F;
        case NORMAL:
          return 1.5F;
      } 
      return 1.5F;
    }
    
    private float getGravityVelocity() {
      switch (this.throwingType) {
        case BOW:
        case POTION:
          return 0.05F;
        case EXPERIENCE:
          return 0.07F;
        case NORMAL:
          return 0.03F;
      } 
      return 0.03F;
    }
    
    private void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
      this.position = new Vec3d(x, y, z);
      this.yaw = yaw;
      this.pitch = pitch;
    }
    
    private void setPosition(Vec3d position) {
      this.position = new Vec3d(position.x, position.y, position.z);
      double entitySize = ((this.throwingType == TrajectoryCalculator.ThrowingType.BOW) ? 0.5D : 0.25D) / 2.0D;
      this.boundingBox = new AxisAlignedBB(position.x - entitySize, position.y - entitySize, position.z - entitySize, position.x + entitySize, position.y + entitySize, position.z + entitySize);
    }
    
    private void setThrowableHeading(Vec3d motion, float velocity) {
      this.motion = TrajectoryCalculator.div(motion, (float)motion.length());
      this.motion = TrajectoryCalculator.mult(this.motion, velocity);
    }
    
    public boolean isCollided() {
      return this.collided;
    }
    
    public RayTraceResult getCollidingTarget() {
      return this.target;
    }
  }
  
  public static double[] interpolate(Entity entity) {
    double posX = interpolate(entity.posX, entity.lastTickPosX) - (Wrapper.getMinecraft()).renderManager.renderPosX;
    double posY = interpolate(entity.posY, entity.lastTickPosY) - (Wrapper.getMinecraft()).renderManager.renderPosY;
    double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - (Wrapper.getMinecraft()).renderManager.renderPosZ;
    return new double[] { posX, posY, posZ };
  }
  
  public static double interpolate(double now, double then) {
    return then + (now - then) * Wrapper.getMinecraft().getRenderPartialTicks();
  }
  
  public static Vec3d mult(Vec3d factor, float multiplier) {
    return new Vec3d(factor.x * multiplier, factor.y * multiplier, factor.z * multiplier);
  }
  
  public static Vec3d div(Vec3d factor, float divisor) {
    return new Vec3d(factor.x / divisor, factor.y / divisor, factor.z / divisor);
  }
}
