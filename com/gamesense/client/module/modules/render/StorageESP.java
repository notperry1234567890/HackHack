//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import java.awt.Color;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class StorageESP extends Module {
  Setting.Integer w;
  
  ConcurrentHashMap<TileEntity, String> chests;
  
  public StorageESP() {
    super("StorageESP", Module.Category.Render);
    this.chests = new ConcurrentHashMap<>();
  }
  
  public void setup() {
    this.w = registerInteger("Width", "Width", 2, 1, 10);
  }
  
  public void onUpdate() {
    mc.world.loadedTileEntityList.forEach(e -> this.chests.put(e, ""));
  }
  
  public void onWorldRender(RenderEvent event) {
    Color c1 = new Color(255, 255, 0, 255);
    Color c2 = new Color(180, 70, 200, 255);
    Color c3 = new Color(150, 150, 150, 255);
    Color c4 = new Color(255, 0, 0, 255);
    if (this.chests != null && this.chests.size() > 0) {
      GameSenseTessellator.prepareGL();
      this.chests.forEach((c, t) -> {
            if (mc.world.loadedTileEntityList.contains(c)) {
              if (c instanceof net.minecraft.tileentity.TileEntityChest)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), c1.getRGB()); 
              if (c instanceof net.minecraft.tileentity.TileEntityEnderChest)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), c2.getRGB()); 
              if (c instanceof net.minecraft.tileentity.TileEntityShulkerBox)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), c4.getRGB()); 
              if (c instanceof net.minecraft.tileentity.TileEntityDispenser || c instanceof net.minecraft.tileentity.TileEntityFurnace || c instanceof net.minecraft.tileentity.TileEntityHopper)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), c3.getRGB()); 
            } 
          });
      GameSenseTessellator.releaseGL();
    } 
  }
}
