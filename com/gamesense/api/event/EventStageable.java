package com.gamesense.api.event;

public class EventStageable {
  private EventStage stage;
  
  public EventStageable() {}
  
  public EventStageable(EventStage stage) {
    this.stage = stage;
  }
  
  public EventStage getStage() {
    return this.stage;
  }
  
  public enum EventStage {
    PRE, POST;
  }
}
