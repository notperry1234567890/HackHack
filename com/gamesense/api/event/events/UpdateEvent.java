package com.gamesense.api.event.events;

import com.gamesense.api.event.EventCancellable;
import com.gamesense.api.event.EventStageable;

public class UpdateEvent extends EventCancellable {
  private float yaw;
  
  private float pitch;
  
  private double x;
  
  private double y;
  
  private double z;
  
  private boolean onGround;
  
  public UpdateEvent(EventStageable.EventStage stage, float yaw, float pitch, double x, double y, double z, boolean onGround) {
    super(stage);
    this.yaw = yaw;
    this.pitch = pitch;
    this.x = x;
    this.y = y;
    this.z = z;
    this.onGround = onGround;
  }
  
  public void setYaw(float yaw) {
    this.yaw = yaw;
  }
  
  public void setPitch(float pitch) {
    this.pitch = pitch;
  }
  
  public void setX(double x) {
    this.x = x;
  }
  
  public void setY(double y) {
    this.y = y;
  }
  
  public void setZ(double z) {
    this.z = z;
  }
  
  public void setOnGround(boolean onGround) {
    this.onGround = onGround;
  }
  
  public boolean isOnGround() {
    return this.onGround;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public double getX() {
    return this.x;
  }
  
  public double getY() {
    return this.y;
  }
  
  public double getZ() {
    return this.z;
  }
}
