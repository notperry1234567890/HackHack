//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AutoTotemplus extends Module {
  int totems;
  
  boolean moving;
  
  boolean returnI;
  
  Setting.Boolean soft;
  
  Setting.Mode OffhandItem;
  
  Setting.Integer TotemHealth;
  
  Setting.Boolean CrystalAura;
  
  Setting.Boolean BedAura;
  
  Setting.Boolean AuraGap;
  
  public AutoTotemplus() {
    super("AutoTotem+", Module.Category.Combat);
    this.moving = false;
    this.returnI = false;
  }
  
  public void setup() {
    this.soft = registerBoolean("Soft", "Soft", true);
    this.TotemHealth = registerInteger("Health", "health", 10, 1, 20);
    this.CrystalAura = registerBoolean("Crystal on CA", "crystalaura", false);
    this.BedAura = registerBoolean("Bed on BA", "bedaura", true);
    this.AuraGap = registerBoolean("Gap on Aura", "auraGap", true);
    ArrayList<String> modes = new ArrayList<>();
    modes.add("totem");
    modes.add("bed");
    modes.add("crystal");
    modes.add("golden apple");
    this.OffhandItem = registerMode("Item", "item", modes, "totem");
  }
  
  public void onUpdate() {
    Item item = getItem();
    if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer)
      return; 
    if (this.returnI) {
      int t = -1;
      for (int i = 0; i < 45; i++) {
        if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
          t = i;
          break;
        } 
      } 
      if (t == -1)
        return; 
      mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      this.returnI = false;
    } 
    this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == item)).mapToInt(ItemStack::getCount).sum();
    if (mc.player.getHeldItemOffhand().getItem() == item) {
      this.totems++;
    } else {
      if (this.soft.getValue() && !mc.player.getHeldItemOffhand().isEmpty())
        return; 
      if (this.moving) {
        mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
        this.moving = false;
        if (!mc.player.inventory.getItemStack().isEmpty())
          this.returnI = true; 
        return;
      } 
      if (mc.player.inventory.getItemStack().isEmpty()) {
        if (this.totems == 0)
          return; 
        int t = -1;
        for (int i = 0; i < 45; i++) {
          if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
            t = i;
            break;
          } 
        } 
        if (t == -1)
          return; 
        mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
        this.moving = true;
      } else if (!this.soft.getValue()) {
        int t = -1;
        for (int i = 0; i < 45; i++) {
          if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
            t = i;
            break;
          } 
        } 
        if (t == -1)
          return; 
        mc.playerController.windowClick(0, (t < 9) ? (t + 36) : t, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      } 
    } 
  }
  
  public String getHudInfo() {
    String t = "[" + ChatFormatting.WHITE + this.OffhandItem.getValue() + ChatFormatting.GRAY + "]";
    return t;
  }
  
  public Item getItem() {
    Item item = Items.TOTEM_OF_UNDYING;
    Item mainItem = null;
    boolean Crystal = false;
    boolean Bed = false;
    if (mc.player.getHealth() < this.TotemHealth.getValue()) {
      item = Items.TOTEM_OF_UNDYING;
    } else if (this.CrystalAura.getValue() && ModuleManager.isModuleEnabled("AutoCrystal")) {
      item = Items.END_CRYSTAL;
      Crystal = true;
    } else if (this.BedAura.getValue() && ModuleManager.isModuleEnabled("BedAura") && !Crystal) {
      item = Items.BED;
      Bed = true;
    } else if (this.AuraGap.getValue() && ModuleManager.isModuleEnabled("KillAura") && !Crystal && !Bed) {
      item = Items.GOLDEN_APPLE;
    } else if (this.OffhandItem.getValue() == "bed") {
      item = Items.BED;
      mainItem = Items.BED;
    } else if (this.OffhandItem.getValue() == "totem") {
      item = Items.TOTEM_OF_UNDYING;
      mainItem = Items.TOTEM_OF_UNDYING;
    } else if (this.OffhandItem.getValue() == "crystal") {
      item = Items.END_CRYSTAL;
      mainItem = Items.END_CRYSTAL;
    } else if (this.OffhandItem.getValue() == "golden apple") {
      item = Items.GOLDEN_APPLE;
      mainItem = Items.GOLDEN_APPLE;
    } 
    return item;
  }
}
