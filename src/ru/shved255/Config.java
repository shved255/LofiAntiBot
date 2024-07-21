package ru.shved255;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Config {

	private Main plugin;
	private FileConfiguration cfg;
	private String message;
	private String kick;
	private String success;
	private int timer;
	private String bossBar;
	private Boolean bossBarDisplay;
	private int hoursTime;
	private int giveCount;
	private Boolean lvl;
	@SuppressWarnings("unused")
	private List<String> commandsPlayer;
	private List<String> commandsServer;
	private String give;
	private String ungive;
	
	public Config(Main plugin) {
		this.plugin = plugin;
		File file = new File(plugin.getDataFolder() + File.separator, "config.yml"); 
	    this.cfg = YamlConfiguration.loadConfiguration(file);
	    message = cfg.getString("Message");
	    bossBar = cfg.getString("BossBar");
	    kick = cfg.getString("Kick");
	    success = cfg.getString("Success");
	    timer = cfg.getInt("Time");
	    bossBarDisplay = cfg.getBoolean("BossBarDisplay");
	    hoursTime = cfg.getInt("HoursLogin.Time");
	    commandsPlayer = cfg.getStringList("Commands.PlayerCommands");
	    commandsServer = cfg.getStringList("Commands.ServerCommands");
	    give = cfg.getString("Subject.Give");
	    ungive = cfg.getString("Subject.Ungive");
	    giveCount = cfg.getInt("Subject.GiveCount");
	    lvl = cfg.getBoolean("LevelEnable");
	}

	public FileConfiguration getConfig() {
		return this.cfg;
	}
	
	public static String ChatColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public String getMessage(String nick) {
		return ChatColor(this.message.replace("{TIME}", String.valueOf(plugin.getTimer(nick))));
	}
	
	public String getBossBar(String nick) {
		return ChatColor(this.bossBar.replace("{TIME}", String.valueOf(plugin.getTimer(nick))));
	}
	
	public int getTimer() {
		return this.timer;
	}
	
	public int getGiveCount() {
		return this.giveCount;
	}
	
	public Boolean getLvl() {
		return this.lvl;
	}
	
	public int getHoursTime() {
		return this.hoursTime;
	}
	
	public String getGive() {
		return this.give;
	}
	
	public String getUnGive() {
		return this.ungive;
	}
	
	public Boolean getBossBarDisplay() {
		return this.bossBarDisplay;
	}
	
	public String getKick() {
		return ChatColor(this.kick);
	}
	
	public String getSuccess() {
		return ChatColor(this.success);
	}
	
	public List<String> getCommandsPlayer(Player player) {
		List<String> result = new ArrayList<>(this.commandsServer);
		return result;
		  }
		  
	public List<String> getCommandsServer(Player player) {
	    String nick = player.getName();
	    List<String> result = new ArrayList<>();
	    for (String command : this.commandsServer) {
	        result.add(command.replace("(PLAYER)", nick));
	    }
	    return result;
	}
}
