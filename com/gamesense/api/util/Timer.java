package com.gamesense.api.util;

public class Timer {
  private long time = -1L;
  
  public boolean passed(long ms) {
    return (getTime(System.nanoTime() - this.time) >= ms);
  }
  
  public void reset() {
    this.time = System.nanoTime();
  }
  
  public long getTime(long time) {
    return time / 1000000L;
  }
  
  public boolean passedMs(long ms) {
    return (getMs(System.nanoTime() - this.time) >= ms);
  }
  
  public long getMs(long time) {
    return time / 1000000L;
  }
}
