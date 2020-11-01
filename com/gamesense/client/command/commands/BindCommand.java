package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
  public String[] getAlias() {
    return new String[] { "bind", "b" };
  }
  
  public String getSyntax() {
    return "bind <Module> <Key>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    int key = Keyboard.getKeyIndex(args[1].toUpperCase());
    ModuleManager.getModules().forEach(m -> {
          if (args[0].equalsIgnoreCase(m.getName())) {
            m.setBind(key);
            Command.sendClientMessage(args[0] + " bound to " + args[1].toUpperCase());
          } 
        });
  }
}
