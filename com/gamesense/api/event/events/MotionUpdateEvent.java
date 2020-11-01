package com.gamesense.api.event.events;

import com.gamesense.api.event.EventCancellable;

public class MotionUpdateEvent extends EventCancellable {
  public int stage;
  
  public MotionUpdateEvent(int stage) {
    this.stage = stage;
  }
}
