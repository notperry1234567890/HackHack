//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class AutoTotem extends Module {
  int totems;
  
  boolean moving;
  
  boolean returnI;
  
  Setting.Boolean soft;
  
  public AutoTotem() {
    super("AutoTotem", Module.Category.Combat);
    this.moving = false;
    this.returnI = false;
  }
  
  public void setup() {
    this.soft = registerBoolean("Soft", "Soft", true);
  }
  
  public void onUpdate() {
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
    this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
    if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
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
          if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
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
    String t = "[" + ChatFormatting.WHITE + this.totems + ChatFormatting.GRAY + "]";
    return t;
  }
}
