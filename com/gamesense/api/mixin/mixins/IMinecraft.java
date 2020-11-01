package com.gamesense.api.mixin.mixins;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;

public interface IMinecraft {
  Timer getTimer();
  
  void setSession(Session paramSession);
  
  Session getSession();
  
  void setRightClickDelayTimer(int paramInt);
  
  void clickMouse();
  
  ServerData getCurrentServerData();
}
