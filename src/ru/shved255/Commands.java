package ru.shved255;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
  private Main plugin;
  
  public Commands(Main plugin) {
    this.plugin = plugin;
  }
  
  public void commands(Player player) {
	    List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
	    List<String> commandsServer = this.plugin.config().getCommandsServer(player);
	    for (String command : commandsPlayer)
	      Bukkit.dispatchCommand((CommandSender)player, command); 
	    for (String command : commandsServer)
	      Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), command); 
  }
}
