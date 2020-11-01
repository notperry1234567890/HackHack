//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.InventoryUtil;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lwjgl.input.Mouse;

public class MCP extends Module {
  Setting.Mode mode;
  
  Setting.Boolean stopRotation;
  
  Setting.Integer rotation;
  
  boolean clicked;
  
  public MCP() {
    super("MCP", Module.Category.Combat);
  }
  
  public void setup() {
    ArrayList<String> modes = new ArrayList<>();
    modes.add("MIDDLECLICK");
    modes.add("TOGGLE");
    this.mode = registerMode("Mode", "Mode", modes, "MIDDLECLICK");
    this.stopRotation = registerBoolean("Rotation", "Rotation", true);
    this.rotation = registerInteger("Delay", "Delay", 10, 0, 100);
    this.clicked = false;
  }
  
  public void onEnable() {
    if (!this.mode.getValue().equalsIgnoreCase("TOGGLE")) {
      throwPearl();
      disable();
    } 
  }
  
  public void onUpdate() {
    if (this.mode.getValue().equalsIgnoreCase("MIDDLECLICK"))
      if (Mouse.isButtonDown(2)) {
        if (!this.clicked)
          throwPearl(); 
        this.clicked = true;
      } else {
        this.clicked = false;
      }  
  }
  
  private void throwPearl() {
    int pearlSlot = InventoryUtil.findHotbarBlock(ItemEnderPearl.class);
    boolean offhand = (mc.player.getHeldItemOffhand().getItem() == Items.ENDER_PEARL);
    if (pearlSlot != -1 || offhand) {
      int oldslot = mc.player.inventory.currentItem;
      if (!offhand)
        InventoryUtil.switchToHotbarSlot(pearlSlot, false); 
      mc.playerController.processRightClick((EntityPlayer)mc.player, (World)mc.world, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
      if (!offhand)
        InventoryUtil.switchToHotbarSlot(oldslot, false); 
    } 
  }
}
