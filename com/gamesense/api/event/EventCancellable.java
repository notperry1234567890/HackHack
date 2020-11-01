package com.gamesense.api.event;

public class EventCancellable extends EventStageable {
  private boolean canceled;
  
  public EventCancellable() {}
  
  public EventCancellable(EventStageable.EventStage stage) {
    super(stage);
  }
  
  public boolean isCanceled() {
    return this.canceled;
  }
  
  public void setCanceled(boolean canceled) {
    this.canceled = canceled;
  }
}
