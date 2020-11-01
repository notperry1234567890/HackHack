//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.TransformSideFirstPersonEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
  Setting.Double xr;
  
  Setting.Double yr;
  
  Setting.Double zr;
  
  Setting.Double xl;
  
  Setting.Double yl;
  
  Setting.Double zl;
  
  @EventHandler
  private Listener<TransformSideFirstPersonEvent> eventListener;
  
  public ViewModel() {
    super("ViewModel", Module.Category.Render);
    this.eventListener = new Listener(event -> {
          if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(this.xr.getValue(), this.yr.getValue(), this.zr.getValue());
          } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(this.xl.getValue(), this.yl.getValue(), this.zl.getValue());
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.xl = registerDouble("Left X", "LeftX", 0.0D, -2.0D, 2.0D);
    this.yl = registerDouble("Left Y", "LeftY", 0.2D, -2.0D, 2.0D);
    this.zl = registerDouble("Left Z", "LeftZ", -1.2D, -2.0D, 2.0D);
    this.xr = registerDouble("Right X", "RightX", 0.0D, -2.0D, 2.0D);
    this.yr = registerDouble("Right Y", "RightY", 0.2D, -2.0D, 2.0D);
    this.zr = registerDouble("Right Z", "RightZ", -1.2D, -2.0D, 2.0D);
  }
  
  public void onEnable() {
    GameSenseMod.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    GameSenseMod.EVENT_BUS.unsubscribe(this);
  }
}
