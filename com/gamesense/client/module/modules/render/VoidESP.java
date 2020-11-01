//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.module.Module;
import io.netty.util.internal.ConcurrentSet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class VoidESP extends Module {
  Setting.Boolean rainbow;
  
  Setting.Integer renderDistance;
  
  Setting.Integer activeYValue;
  
  Setting.Mode renderType;
  
  Setting.Mode renderMode;
  
  private ConcurrentSet<BlockPos> voidHoles;
  
  public VoidESP() {
    super("VoidESP", Module.Category.Render);
  }
  
  public void setup() {
    ArrayList<String> render = new ArrayList<>();
    render.add("Outline");
    render.add("Fill");
    render.add("Both");
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Box");
    modes.add("Flat");
    this.rainbow = registerBoolean("Rainbow", "Rainbow", false);
    this.renderDistance = registerInteger("Distance", "Distance", 10, 1, 40);
    this.activeYValue = registerInteger("Activate Y", "ActivateY", 20, 0, 256);
    this.renderType = registerMode("Render", "Render", render, "Both");
    this.renderMode = registerMode("Mode", "Mode", modes, "Flat");
  }
  
  public void onUpdate() {
    if (mc.player.dimension == 1)
      return; 
    if (mc.player.getPosition().getY() > this.activeYValue.getValue())
      return; 
    if (this.voidHoles == null) {
      this.voidHoles = new ConcurrentSet();
    } else {
      this.voidHoles.clear();
    } 
    List<BlockPos> blockPosList = BlockUtils.getCircle(getPlayerPos(), 0, this.renderDistance.getValue(), false);
    for (BlockPos blockPos : blockPosList) {
      if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK))
        continue; 
      if (isAnyBedrock(blockPos, Offsets.center))
        continue; 
      this.voidHoles.add(blockPos);
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (mc.player == null || this.voidHoles == null)
      return; 
    if (mc.player.getPosition().getY() > this.activeYValue.getValue())
      return; 
    if (this.voidHoles.isEmpty())
      return; 
    this.voidHoles.forEach(blockPos -> {
          GameSenseTessellator.prepare(7);
          if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
            drawBox(blockPos, 255, 255, 0);
          } else {
            drawFlat(blockPos, 255, 255, 0);
          } 
          GameSenseTessellator.release();
          GameSenseTessellator.prepare(7);
          drawOutline(blockPos, 1, 255, 255, 0);
          GameSenseTessellator.release();
        });
  }
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
    for (BlockPos pos : offset) {
      if (mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals(Blocks.BEDROCK))
        return true; 
    } 
    return false;
  }
  
  private static class Offsets {
    static final BlockPos[] center = new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
  }
  
  public void drawFlat(BlockPos blockPos, int r, int g, int b) {
    if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      Color c = Rainbow.getColor();
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.renderMode.getValue().equalsIgnoreCase("Flat")) {
        Color color;
        if (this.rainbow.getValue()) {
          color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 50);
        } else {
          color = new Color(r, g, b, 50);
        } 
        GameSenseTessellator.drawBox(blockPos, color.getRGB(), 1);
      } 
    } 
  }
  
  private void drawBox(BlockPos blockPos, int r, int g, int b) {
    if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      Color color, c = Rainbow.getColor();
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.rainbow.getValue()) {
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 50);
      } else {
        color = new Color(r, g, b, 50);
      } 
      GameSenseTessellator.drawBox(blockPos, color.getRGB(), 63);
    } 
  }
  
  public void drawOutline(BlockPos blockPos, int width, int r, int g, int b) {
    if (this.renderType.getValue().equalsIgnoreCase("Outline") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
      int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
      int r1 = rgb >> 16 & 0xFF;
      int g2 = rgb >> 8 & 0xFF;
      int b3 = rgb & 0xFF;
      hue[0] = hue[0] + 0.02F;
      if (this.renderMode.getValue().equalsIgnoreCase("Box"))
        if (this.rainbow.getValue()) {
          GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, width, r1, g2, b3, 255);
        } else {
          GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, width, r, g, b, 255);
        }  
      if (this.renderMode.getValue().equalsIgnoreCase("Flat"))
        if (this.rainbow.getValue()) {
          GameSenseTessellator.drawBoundingBoxBottom2(blockPos, width, r1, g2, b3, 255);
        } else {
          GameSenseTessellator.drawBoundingBoxBottom2(blockPos, width, r, g, b, 255);
        }  
    } 
  }
}
