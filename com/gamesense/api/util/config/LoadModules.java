package com.gamesense.api.util.config;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.GameSenseMod;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LoadModules {
  public LoadModules() {
    loadCombat();
    loadExploits();
    loadHud();
    loadMisc();
    loadMovement();
    loadRender();
  }
  
  public void loadCombat() {
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Combat.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
  
  public void loadExploits() {
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Exploits)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Exploits)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Exploits.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Exploits)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
  
  public void loadHud() {
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.HUD)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.HUD)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Hud.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.HUD)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
  
  public void loadMisc() {
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Misc.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
  
  public void loadMovement() {
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Movement.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
  
  public void loadRender() {
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "Value.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Render)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndModConfig(configname, mm);
            if (mod instanceof Setting.Integer) {
              ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
              continue;
            } 
            if (mod instanceof Setting.Double)
              ((Setting.Double)mod).setValue(Double.parseDouble(isOn)); 
          } 
        } 
      } 
      br.close();
    } catch (Exception var13) {
      var13.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "Boolean.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Render)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
          } 
        } 
      } 
      br.close();
    } catch (Exception var12) {
      var12.printStackTrace();
    } 
    try {
      File file = new File(SaveConfiguration.Render.getAbsolutePath(), "String.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(Module.Category.Render)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (GameSenseMod.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            ((Setting.Mode)mod).setValue(isOn);
          } 
        } 
      } 
      br.close();
    } catch (Exception var11) {
      var11.printStackTrace();
    } 
  }
}
