package ru.shved255;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ru.shved255.Main;
import ru.shved255.listeners.Listeners;
import ru.shved255.utils.Config;
import ru.shved255.utils.Players;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends JavaPlugin {
	
	private static Main inst;
	private Config config;
    private Map<Player, Integer> craftingPlayers = new ConcurrentHashMap<>();
	private Map<String, Integer> timers = new ConcurrentHashMap<>();
	private Map<Player, BossBar> bossbar = new ConcurrentHashMap<>();
	private Map<Player, Integer> success = new ConcurrentHashMap<>();
	private List<String> current = new LinkedList<>();
	private Map<String, List<Integer>> ids = new ConcurrentHashMap<>();
	private Players base;

	@Override
	public void onLoad() {
    	(inst = this).saveDefaultConfig();
	}	
	
    @Override
    public void onEnable() {
    	config = new Config(this);
    	File cfg = new File(getDataFolder() + File.separator + "config.yml"); {
    	if(!cfg.exists()) {
    		saveDefaultConfig();
    	}
    	File playersFile = new File(getDataFolder() + File.separator + "players.yml"); {
    	    if (!playersFile.exists())
    	      saveResource("players.yml", false); 
    		}
    	}
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        this.base = new Players(this);
        System.out.println("|------------------------------------------------------------------|");
        System.out.println("|                                                                  |");
        System.out.println("|      LofiAntiBot: Плагин был включен! :)                         |");
        System.out.println("|      Плагин был сделан Shved255 | Discord: Shved255              |");
        System.out.println("|------------------------------------------------------------------|");
    }
    
    @Override
    public void onDisable() {
        System.out.println("|------------------------------------------------------------------|");
        System.out.println("|                                                                  |");
        System.out.println("|      LofiAntiBot: Плагин был выключен! :(                        |");
        System.out.println("|                                                                  |");
        System.out.println("|------------------------------------------------------------------|");
    }
	
    public static Main getInstance() {
    	return inst;
    }
    
    public Config config() {
    	return this.config;
    }
    
    public int getTimer(String nick) {
    	if(this.timers.containsKey(nick)) {
    		return this.timers.get(nick);
    	} else throw new IllegalStateException("nick not in map.");
    }
    
    public Map<Player, Integer> getCraftingPlayers() {
    	return this.craftingPlayers;
    }
    
    public Map<String, Integer> getTimers() {
    	return this.timers;
    }
    
    public Map<Player, BossBar> getBossBar() {
    	return this.bossbar;
    }
    
    public Map<Player, Integer> getSuccess() {
    	return this.success;
    }
    
    public List<String> getCurrent() {
    	return this.current;
    }
    
    public boolean containsCurrent(Player player) {
    	return this.current.contains(player.getName());
    }
    
    public void addPlayer(Player player) {
    	this.current.add(player.getName());
    }
    
    public void removePlayer(Player player) {
    	this.current.remove(player.getName());
    }
    
    public void addTaskIds(String nick, List<Integer> ids) {
    	this.ids.put(nick, ids);
    }
    
    public Players getBase() {
      return this.base;
    }
   
}