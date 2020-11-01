//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.client.module.Module;
import net.minecraft.init.Items;

public class EXPFast extends Module {
  public EXPFast() {
    super("EXPFast", Module.Category.Combat);
  }
  
  public void onUpdate() {
    if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)
      mc.rightClickDelayTimer = 0; 
  }
}
