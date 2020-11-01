//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.util.HueCycler;
import com.gamesense.api.util.TrajectoryCalculator;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.api.util.world.GeometryMasks;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Trajectories extends Module {
  ArrayList<Vec3d> positions;
  
  HueCycler cycler;
  
  public Trajectories() {
    super("Trajectories", Module.Category.Render);
    this.positions = new ArrayList<>();
    this.cycler = new HueCycler(100);
  }
  
  public void onWorldRender(RenderEvent event) {
    try {
      mc.world.loadedEntityList.stream()
        .filter(entity -> entity instanceof EntityLivingBase)
        .map(entity -> (EntityLivingBase)entity)
        .forEach(entity -> {
            this.positions.clear();
            TrajectoryCalculator.ThrowingType tt = TrajectoryCalculator.getThrowType(entity);
            if (tt == TrajectoryCalculator.ThrowingType.NONE)
              return; 
            TrajectoryCalculator.FlightPath flightPath = new TrajectoryCalculator.FlightPath(entity, tt);
            while (!flightPath.isCollided()) {
              flightPath.onUpdate();
              this.positions.add(flightPath.position);
            } 
            BlockPos hit = null;
            if (flightPath.getCollidingTarget() != null)
              hit = flightPath.getCollidingTarget().getBlockPos(); 
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            if (hit != null) {
              GameSenseTessellator.prepare(7);
              GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.3F);
              GameSenseTessellator.drawBox(hit, 872415231, ((Integer)GeometryMasks.FACEMAP.get((flightPath.getCollidingTarget()).sideHit)).intValue());
              GameSenseTessellator.release();
            } 
            if (this.positions.isEmpty())
              return; 
            GL11.glDisable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glLineWidth(2.0F);
            if (hit != null) {
              GL11.glColor3f(1.0F, 1.0F, 1.0F);
            } else {
              this.cycler.setNext();
            } 
            GL11.glBegin(1);
            Vec3d a = this.positions.get(0);
            GL11.glVertex3d(a.x - (mc.getRenderManager()).renderPosX, a.y - (mc.getRenderManager()).renderPosY, a.z - (mc.getRenderManager()).renderPosZ);
            for (Vec3d v : this.positions) {
              GL11.glVertex3d(v.x - (mc.getRenderManager()).renderPosX, v.y - (mc.getRenderManager()).renderPosY, v.z - (mc.getRenderManager()).renderPosZ);
              GL11.glVertex3d(v.x - (mc.getRenderManager()).renderPosX, v.y - (mc.getRenderManager()).renderPosY, v.z - (mc.getRenderManager()).renderPosZ);
              if (hit == null)
                this.cycler.setNext(); 
            } 
            GL11.glEnd();
            GL11.glEnable(3042);
            GL11.glEnable(3553);
            this.cycler.reset();
          });
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
