package com.gamesense.api.util;

public class TimerUtil {
  private long time = System.nanoTime() / 1000000L;
  
  public boolean reach(long time) {
    return (time() >= time);
  }
  
  public void reset() {
    this.time = System.nanoTime() / 1000000L;
  }
  
  public boolean sleep(long time) {
    if (time() >= time) {
      reset();
      return true;
    } 
    return false;
  }
  
  public short convertToMS(float perSecond) {
    return (short)(int)(1000.0F / perSecond);
  }
  
  public long time() {
    return System.nanoTime() / 1000000L - this.time;
  }
}
