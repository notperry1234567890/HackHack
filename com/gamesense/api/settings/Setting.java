package com.gamesense.api.settings;

import com.gamesense.client.module.Module;
import java.util.List;

public class Setting {
  private final String name;
  
  private final String configname;
  
  private final Module parent;
  
  private final Module.Category category;
  
  private final Type type;
  
  public Setting(String name, String configname, Module parent, Module.Category category, Type type) {
    this.name = name;
    this.configname = configname;
    this.parent = parent;
    this.type = type;
    this.category = category;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getConfigName() {
    return this.configname;
  }
  
  public Module getParent() {
    return this.parent;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public Module.Category getCategory() {
    return this.category;
  }
  
  public enum Type {
    INT, DOUBLE, BOOLEAN, STRING, MODE;
  }
  
  public static class Integer extends Setting {
    private int value;
    
    private final int min;
    
    private final int max;
    
    public Integer(String name, String configname, Module parent, Module.Category category, int value, int min, int max) {
      super(name, configname, parent, category, Setting.Type.INT);
      this.value = value;
      this.min = min;
      this.max = max;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public void setValue(int value) {
      this.value = value;
    }
    
    public int getMin() {
      return this.min;
    }
    
    public int getMax() {
      return this.max;
    }
  }
  
  public static class Double extends Setting {
    private double value;
    
    private final double min;
    
    private final double max;
    
    public Double(String name, String configname, Module parent, Module.Category category, double value, double min, double max) {
      super(name, configname, parent, category, Setting.Type.DOUBLE);
      this.value = value;
      this.min = min;
      this.max = max;
    }
    
    public double getValue() {
      return this.value;
    }
    
    public void setValue(double value) {
      this.value = value;
    }
    
    public double getMin() {
      return this.min;
    }
    
    public double getMax() {
      return this.max;
    }
  }
  
  public static class Boolean extends Setting {
    private boolean value;
    
    public Boolean(String name, String configname, Module parent, Module.Category category, boolean value) {
      super(name, configname, parent, category, Setting.Type.BOOLEAN);
      this.value = value;
    }
    
    public boolean getValue() {
      return this.value;
    }
    
    public void setValue(boolean value) {
      this.value = value;
    }
  }
  
  public static class Mode extends Setting {
    private String value;
    
    private final List<String> modes;
    
    public Mode(String name, String configname, Module parent, Module.Category category, List<String> modes, String value) {
      super(name, configname, parent, category, Setting.Type.MODE);
      this.value = value;
      this.modes = modes;
    }
    
    public String getValue() {
      return this.value;
    }
    
    public void setValue(String value) {
      this.value = value;
    }
    
    public List<String> getModes() {
      return this.modes;
    }
  }
}
