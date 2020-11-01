//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.module.Module;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class FakePlayer extends Module {
  public FakePlayer() {
    super("FakePlayer", Module.Category.Misc);
  }
  
  public void onEnable() {
    if (mc.world == null)
      return; 
    EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)mc.world, new GameProfile(UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2"), "popbob"));
    fakePlayer.copyLocationAndAnglesFrom((Entity)mc.player);
    fakePlayer.rotationYawHead = mc.player.rotationYawHead;
    mc.world.addEntityToWorld(-100, (Entity)fakePlayer);
  }
  
  public void onDisable() {
    mc.world.removeEntityFromWorld(-100);
  }
}
