package com.gamesense.api.event.events;

import com.gamesense.api.event.EventCancellable;
import net.minecraft.entity.Entity;

public class EntityRemovedEvent extends EventCancellable {
  private final Entity entity;
  
  public EntityRemovedEvent(Entity entity) {
    this.entity = entity;
  }
  
  public Entity get_entity() {
    return this.entity;
  }
}
