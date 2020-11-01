package com.gamesense.api.util.config;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

public class SaveModules {
  public void saveModules() {
    saveCombat();
    saveExploits();
    saveHud();
    saveMisc();
    saveMovement();
    saveRender();
  }
  
  public void saveCombat() {
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Combat).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Combat).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Combat).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public void saveExploits() {
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Exploits).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Exploits).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Exploits).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public void saveHud() {
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.HUD).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.HUD).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.HUD).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public void saveMisc() {
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Misc).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Misc).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Misc).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public void saveMovement() {
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Movement).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Movement).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Movement).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public void saveRender() {
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "Value.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Render).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.DOUBLE)
          out.write(i.getConfigName() + ":" + ((Setting.Double)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
        if (i.getType() == Setting.Type.INT)
          out.write(i.getConfigName() + ":" + ((Setting.Integer)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "Boolean.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Render).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.BOOLEAN)
          out.write(i.getConfigName() + ":" + ((Setting.Boolean)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "String.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> var3 = (GameSenseMod.getInstance()).settingsManager.getSettingsByCategory(Module.Category.Render).iterator();
      while (var3.hasNext()) {
        Setting i = var3.next();
        if (i.getType() == Setting.Type.MODE)
          out.write(i.getConfigName() + ":" + ((Setting.Mode)i).getValue() + ":" + i.getParent().getName() + "\r\n"); 
      } 
      out.close();
    } catch (Exception exception) {}
  }
}
