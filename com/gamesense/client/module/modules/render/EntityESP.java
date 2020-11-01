//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.color.Rainbow;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.entity.Entity;

public class EntityESP extends Module {
  Setting.Mode renderMode;
  
  Setting.Boolean exp;
  
  Setting.Boolean epearls;
  
  Setting.Boolean items;
  
  Setting.Boolean orbs;
  
  Setting.Boolean crystals;
  
  int c;
  
  int c2;
  
  public EntityESP() {
    super("EntityESP", Module.Category.Render);
  }
  
  public void setup() {
    ArrayList<String> Modes = new ArrayList<>();
    Modes.add("Box");
    Modes.add("Outline");
    Modes.add("Glow");
    this.exp = registerBoolean("Exp Bottles", "ExpBottles", false);
    this.epearls = registerBoolean("Ender Pearls", "EnderPearls", false);
    this.crystals = registerBoolean("Crystals", "Crystals", false);
    this.items = registerBoolean("Items", "Items", false);
    this.orbs = registerBoolean("Exp Orbs", "ExpOrbs", false);
    this.renderMode = registerMode("Mode", "Mode", Modes, "Box");
  }
  
  public void onWorldRender(RenderEvent event) {
    ColorMain colorMain = (ColorMain)ModuleManager.getModuleByName("Colors");
    if (ColorMain.rainbow.getValue()) {
      this.c = Rainbow.getColorWithOpacity(50).getRGB();
    } else {
      this.c = (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 50)).getRGB();
    } 
    if (ColorMain.rainbow.getValue()) {
      this.c2 = Rainbow.getColorWithOpacity(255).getRGB();
    } else {
      this.c2 = (new Color(ColorMain.Red.getValue(), ColorMain.Green.getValue(), ColorMain.Blue.getValue(), 255)).getRGB();
    } 
    if (this.renderMode.getValue().equalsIgnoreCase("Box"))
      mc.world.loadedEntityList.stream()
        .filter(entity -> (entity != mc.player))
        .forEach(e -> {
            GameSenseTessellator.prepare(7);
            if (this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), this.c, 63); 
            if (this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), this.c, 63); 
            if (this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), this.c, 63); 
            if (this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), this.c, 63); 
            if (this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), this.c, 63); 
            GameSenseTessellator.release();
            GameSenseTessellator.prepareGL();
            if (this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            GameSenseTessellator.releaseGL();
          }); 
    if (this.renderMode.getValue().equalsIgnoreCase("Outline"))
      mc.world.loadedEntityList.stream()
        .filter(entity -> (entity != mc.player))
        .forEach(e -> {
            GameSenseTessellator.prepareGL();
            if (this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            if (this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), 1.0F, this.c2); 
            GameSenseTessellator.releaseGL();
          }); 
    if (this.renderMode.getValue().equalsIgnoreCase("Glow"))
      mc.world.loadedEntityList.stream()
        .filter(e -> (e != mc.player))
        .forEach(e -> {
            if (this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
              e.setGlowing(true); 
            if (this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
              e.setGlowing(true); 
            if (this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              e.setGlowing(true); 
            if (this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
              e.setGlowing(true); 
            if (this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
              e.setGlowing(true); 
          }); 
  }
  
  public void onUpdate() {
    mc.world.loadedEntityList.stream()
      .filter(e -> (e != mc.player))
      .forEach(e -> {
          if (!this.renderMode.getValue().equalsIgnoreCase("Glow")) {
            if (e instanceof net.minecraft.entity.item.EntityExpBottle)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderPearl)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityItem)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityXPOrb)
              e.setGlowing(false); 
          } 
          if (!this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
            e.setGlowing(false); 
          if (!this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
            e.setGlowing(false); 
          if (!this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
            e.setGlowing(false); 
          if (!this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
            e.setGlowing(false); 
          if (!this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
            e.setGlowing(false); 
        });
  }
  
  public void onDisable() {
    if (this.renderMode.getValue().equalsIgnoreCase("Glow"))
      mc.world.loadedEntityList.stream()
        .filter(e -> (e != mc.player))
        .forEach(e -> {
            if (e instanceof net.minecraft.entity.item.EntityExpBottle)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderPearl)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityItem)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityXPOrb)
              e.setGlowing(false); 
          }); 
  }
  
  public String getHudInfo() {
    String t = "";
    if (this.renderMode.getValue().equalsIgnoreCase("Box"))
      t = "[" + ChatFormatting.WHITE + "Box" + ChatFormatting.GRAY + "]"; 
    if (this.renderMode.getValue().equalsIgnoreCase("Outline"))
      t = "[" + ChatFormatting.WHITE + "Outline" + ChatFormatting.GRAY + "]"; 
    if (this.renderMode.getValue().equalsIgnoreCase("Glow"))
      t = "[" + ChatFormatting.WHITE + "Glow" + ChatFormatting.GRAY + "]"; 
    return t;
  }
}
