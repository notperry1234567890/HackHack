//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WurstplusRotationUtil {
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  private static float yaw;
  
  private static float pitch;
  
  public static void updateRotations() {
    yaw = mc.player.rotationYaw;
    pitch = mc.player.rotationPitch;
  }
  
  public static void restoreRotations() {
    mc.player.rotationYaw = yaw;
    mc.player.rotationYawHead = yaw;
    mc.player.rotationPitch = pitch;
  }
  
  public static void setPlayerRotations(float yaw, float pitch) {
    mc.player.rotationYaw = yaw;
    mc.player.rotationYawHead = yaw;
    mc.player.rotationPitch = pitch;
  }
  
  public void setPlayerYaw(float yaw) {
    mc.player.rotationYaw = yaw;
    mc.player.rotationYawHead = yaw;
  }
  
  public void lookAtPos(BlockPos pos) {
    float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F)));
    this;
    setPlayerRotations(angle[0], angle[1]);
  }
  
  public void lookAtVec3d(Vec3d vec3d) {
    float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d(vec3d.x, vec3d.y, vec3d.z));
    this;
    setPlayerRotations(angle[0], angle[1]);
  }
  
  public void lookAtVec3d(double x, double y, double z) {
    Vec3d vec3d = new Vec3d(x, y, z);
    lookAtVec3d(vec3d);
  }
  
  public void lookAtEntity(Entity entity) {
    float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionEyes(mc.getRenderPartialTicks()));
    this;
    setPlayerRotations(angle[0], angle[1]);
  }
  
  public void setPlayerPitch(float pitch) {
    mc.player.rotationPitch = pitch;
  }
  
  public float getYaw() {
    return yaw;
  }
  
  public void setYaw(float yaw) {
    WurstplusRotationUtil.yaw = yaw;
  }
  
  public float getPitch() {
    return pitch;
  }
  
  public void setPitch(float pitch) {
    WurstplusRotationUtil.pitch = pitch;
  }
  
  public int getDirection4D() {
    return getDirection4D();
  }
  
  public String getDirection4D(boolean northRed) {
    return getDirection4D(northRed);
  }
}
