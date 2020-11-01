//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.RenderUtil;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class HoleESP extends Module {
  public static Setting.Integer rangeS;
  
  Setting.Integer redb;
  
  Setting.Integer greenb;
  
  Setting.Integer blueb;
  
  Setting.Integer redo;
  
  Setting.Integer greeno;
  
  Setting.Integer blueo;
  
  Setting.Integer outlineW;
  
  Setting.Boolean rainbow;
  
  Setting.Boolean hideOwn;
  
  Setting.Boolean flatOwn;
  
  Setting.Mode mode;
  
  Setting.Mode type;
  
  private final BlockPos[] surroundOffset;
  
  private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
  
  public HoleESP() {
    super("HoleESP", Module.Category.Render);
    this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
  }
  
  public void setup() {
    rangeS = registerInteger("Range", "Range", 5, 1, 20);
    this.rainbow = registerBoolean("Rainbow", "Rainbow", false);
    this.hideOwn = registerBoolean("Hide Own", "HideOwn", false);
    this.flatOwn = registerBoolean("Flat Own", "FlatOwn", false);
    this.redb = registerInteger("Red Brock", "RedBrock", 255, 0, 255);
    this.greenb = registerInteger("Green Brock", "GreenBrock", 255, 0, 255);
    this.blueb = registerInteger("Blue Brock", "BlueBrock", 255, 0, 255);
    this.redo = registerInteger("Red Obi", "Red Obi", 255, 0, 255);
    this.greeno = registerInteger("Green Obi", "Green Obi", 255, 0, 255);
    this.blueo = registerInteger("Blue Obi", "Blue Obi", 255, 0, 255);
    this.outlineW = registerInteger("OutlineW", "OutlineW", 2, 1, 12);
    ArrayList<String> render = new ArrayList<>();
    render.add("Outline");
    render.add("Fill");
    render.add("Both");
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Air");
    modes.add("Ground");
    modes.add("Flat");
    modes.add("Slab");
    this.type = registerMode("Render", "Render", render, "Both");
    this.mode = registerMode("Mode", "Mode", modes, "Air");
  }
  
  public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
    List<BlockPos> circleblocks = new ArrayList<>();
    int cx = loc.getX();
    int cy = loc.getY();
    int cz = loc.getZ();
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
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public void onUpdate() {
    if (this.safeHoles == null) {
      this.safeHoles = new ConcurrentHashMap<>();
    } else {
      this.safeHoles.clear();
    } 
    int range = (int)Math.ceil(rangeS.getValue());
    List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);
    for (BlockPos pos : blockPosList) {
      if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
        continue; 
      if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR))
        continue; 
      if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR))
        continue; 
      if (this.hideOwn.getValue() && pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)))
        continue; 
      boolean isSafe = true;
      boolean isBedrock = true;
      for (BlockPos offset : this.surroundOffset) {
        Block block = mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
        if (block != Blocks.BEDROCK)
          isBedrock = false; 
        if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
          isSafe = false;
          break;
        } 
      } 
      if (isSafe)
        this.safeHoles.put(pos, Boolean.valueOf(isBedrock)); 
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (mc.player == null || this.safeHoles == null)
      return; 
    if (this.safeHoles.isEmpty())
      return; 
    GameSenseTessellator.prepare(7);
    if (this.mode.getValue().equalsIgnoreCase("Air"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawBox(blockPos, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawBox(blockPos, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    if (this.mode.getValue().equalsIgnoreCase("Ground"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawBox2(blockPos, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawBox2(blockPos, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    if (this.mode.getValue().equalsIgnoreCase("Flat"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawFlat(blockPos, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawFlat(blockPos, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    if (this.mode.getValue().equalsIgnoreCase("Slab"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawSlab(blockPos, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawSlab(blockPos, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    GameSenseTessellator.release();
    GameSenseTessellator.prepare(7);
    if (this.mode.getValue().equalsIgnoreCase("Air"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawOutline(blockPos, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawOutline(blockPos, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    if (this.mode.getValue().equalsIgnoreCase("Ground"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawOutline(blockPos, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawOutline(blockPos, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    if (this.mode.getValue().equalsIgnoreCase("Flat"))
      this.safeHoles.forEach((blockPos, isBedrock) -> {
            if (isBedrock.booleanValue()) {
              drawOutline(blockPos, 1, this.redb.getValue(), this.greenb.getValue(), this.blueb.getValue());
            } else {
              drawOutline(blockPos, 1, this.redo.getValue(), this.greeno.getValue(), this.blueo.getValue());
            } 
          }); 
    GameSenseTessellator.release();
  }
  
  private void drawBox(BlockPos blockPos, int r, int g, int b) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      Color color, c = Rainbow.getColor();
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.rainbow.getValue()) {
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 50);
      } else {
        color = new Color(r, g, b, 50);
      } 
      if (this.mode.getValue().equalsIgnoreCase("Air"))
        if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
          GameSenseTessellator.drawBox(blockPos, color.getRGB(), 1);
        } else {
          GameSenseTessellator.drawBox(blockPos, color.getRGB(), 63);
        }  
    } 
  }
  
  public void drawBox2(BlockPos blockPos, int r, int g, int b) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      Color color, c = Rainbow.getColor();
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.rainbow.getValue()) {
        color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 50);
      } else {
        color = new Color(r, g, b, 50);
      } 
      if (this.mode.getValue().equalsIgnoreCase("Ground"))
        GameSenseTessellator.drawBox2(blockPos, color.getRGB(), 63); 
    } 
  }
  
  public void drawFlat(BlockPos blockPos, int r, int g, int b) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      Color c = Rainbow.getColor();
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.mode.getValue().equalsIgnoreCase("Flat")) {
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
  
  public void drawSlab(BlockPos blockPos, int r, int g, int b) {
    AxisAlignedBB bb = new AxisAlignedBB(blockPos.getX() - (mc.getRenderManager()).viewerPosX, blockPos.getY() + 0.1D - (mc.getRenderManager()).viewerPosY, blockPos.getZ() - (mc.getRenderManager()).viewerPosZ, (blockPos.getX() + 1) - (mc.getRenderManager()).viewerPosX, blockPos.getY() - (mc.getRenderManager()).viewerPosY, (blockPos.getZ() + 1) - (mc.getRenderManager()).viewerPosZ);
    if (RenderUtil.isInViewFrustrum(new AxisAlignedBB(bb.minX + (mc.getRenderManager()).viewerPosX, bb.minY + (mc.getRenderManager()).viewerPosY, bb.minZ + (mc.getRenderManager()).viewerPosZ, bb.maxX + (mc.getRenderManager()).viewerPosX, bb.maxY + (mc.getRenderManager()).viewerPosY, bb.maxZ + (mc.getRenderManager()).viewerPosZ))) {
      RenderUtil.drawESP(bb, r, g, b, 50.0F);
      if (this.type.getValue().equalsIgnoreCase("Outline") || this.type.getValue().equalsIgnoreCase("Both"))
        RenderUtil.drawESPOutline(bb, r, g, b, 255.0F, 1.0F); 
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    } 
  }
  
  public void drawOutline(BlockPos blockPos, int width, int r, int g, int b) {
    if (this.type.getValue().equalsIgnoreCase("Outline") || this.type.getValue().equalsIgnoreCase("Both")) {
      float[] hue = { (float)(System.currentTimeMillis() % 11520L) / 11520.0F };
      int rgb = Color.HSBtoRGB(hue[0], 1.0F, 1.0F);
      int r1 = rgb >> 16 & 0xFF;
      int g2 = rgb >> 8 & 0xFF;
      int b3 = rgb & 0xFF;
      hue[0] = hue[0] + 0.02F;
      if (this.mode.getValue().equalsIgnoreCase("Air"))
        if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
          if (this.rainbow.getValue()) {
            GameSenseTessellator.drawBoundingBoxBottom2(blockPos, this.outlineW.getValue(), r1, g2, b3, 255);
          } else {
            GameSenseTessellator.drawBoundingBoxBottom2(blockPos, this.outlineW.getValue(), r, g, b, 255);
          } 
        } else if (this.rainbow.getValue()) {
          GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, this.outlineW.getValue(), r1, g2, b3, 255);
        } else {
          GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, this.outlineW.getValue(), r, g, b, 255);
        }  
      if (this.mode.getValue().equalsIgnoreCase("Flat"))
        if (this.rainbow.getValue()) {
          GameSenseTessellator.drawBoundingBoxBottom2(blockPos, this.outlineW.getValue(), r1, g2, b3, 255);
        } else {
          GameSenseTessellator.drawBoundingBoxBottom2(blockPos, this.outlineW.getValue(), r, g, b, 255);
        }  
      if (this.mode.getValue().equalsIgnoreCase("Ground"))
        if (this.rainbow.getValue()) {
          GameSenseTessellator.drawBoundingBoxBlockPos2(blockPos, this.outlineW.getValue(), r1, g2, b3, 255);
        } else {
          GameSenseTessellator.drawBoundingBoxBlockPos2(blockPos, this.outlineW.getValue(), r, g, b, 255);
        }  
    } 
  }
  
  public String getHudInfo() {
    String t = "";
    t = "[" + ChatFormatting.WHITE + this.mode.getValue() + ", " + this.type.getValue() + ChatFormatting.GRAY + "]";
    return t;
  }
}
