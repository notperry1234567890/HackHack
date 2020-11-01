//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;

public class DesyncAura extends Module {
  Setting.Double hitRange;
  
  Setting.Integer delay;
  
  Setting.Boolean switchTo32k;
  
  Setting.Boolean onlyUse32k;
  
  private int hasWaited;
  
  public DesyncAura() {
    super("DesyncAura", Module.Category.Combat);
    this.hasWaited = 0;
  }
  
  public void setup() {
    this.hitRange = registerDouble("Hit Range", "HitRange", 4.0D, 1.0D, 6.0D);
    this.delay = registerInteger("Delay", "Delay", 6, 0, 10);
    this.switchTo32k = registerBoolean("Switch To 32k", "SwitchTo32k", true);
    this.onlyUse32k = registerBoolean("Only 32k", "Only32k", true);
  }
  
  public void onUpdate() {
    if (isEnabled() && !mc.player.isDead && mc.world != null)
      if (this.hasWaited < this.delay.getValue()) {
        this.hasWaited++;
      } else {
        this.hasWaited = 0;
        Iterator<Entity> var1 = mc.world.loadedEntityList.iterator();
        while (true) {
          if (!var1.hasNext())
            return; 
          Entity entity = var1.next();
          if (entity instanceof EntityLivingBase && 
            entity != mc.player && 
            mc.player.getDistance(entity) <= this.hitRange.getValue() && (
            (EntityLivingBase)entity).getHealth() > 0.0F && 
            entity instanceof EntityPlayer && (
            !(entity instanceof EntityPlayer) || !Friends.isFriend(entity.getName())) && (
            checkSharpness(mc.player.getHeldItemMainhand()) || !this.onlyUse32k.getValue()))
            attack(entity); 
        } 
      }  
  }
  
  private boolean checkSharpness(ItemStack item) {
    if (item.getTagCompound() == null)
      return false; 
    NBTTagList enchants = (NBTTagList)item.getTagCompound().getTag("ench");
    if (enchants == null)
      return false; 
    for (int i = 0; i < enchants.tagCount(); i++) {
      NBTTagCompound enchant = enchants.getCompoundTagAt(i);
      if (enchant.getInteger("id") == 16) {
        int lvl = enchant.getInteger("lvl");
        if (lvl >= 42)
          return true; 
        break;
      } 
    } 
    return false;
  }
  
  public void attack(Entity e) {
    boolean holding32k = false;
    if (checkSharpness(mc.player.getHeldItemMainhand()))
      holding32k = true; 
    if (this.switchTo32k.getValue() && !holding32k) {
      int newSlot = -1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack != ItemStack.EMPTY && checkSharpness(stack)) {
          newSlot = i;
          break;
        } 
      } 
      if (newSlot != -1) {
        mc.player.inventory.currentItem = newSlot;
        holding32k = true;
      } 
    } 
    if (!this.onlyUse32k.getValue() || holding32k) {
      mc.playerController.attackEntity((EntityPlayer)mc.player, e);
      mc.player.swingArm(EnumHand.MAIN_HAND);
    } 
  }
}
