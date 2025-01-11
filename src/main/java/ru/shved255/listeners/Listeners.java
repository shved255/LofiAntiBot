package ru.shved255.listeners;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import ru.shved255.Main;
import ru.shved255.utils.Players;

public class Listeners implements Listener {
	
	private Main plugin;
	private Player player;
	
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(plugin.getCraftingPlayers().containsKey(player)) {
			plugin.getCraftingPlayers().remove(player);
			plugin.getBossBar().remove(player);
			plugin.getSuccess().put(player, 60);
			plugin.removePlayer(player);
			String ungive = plugin.config().getUnGive();
			String give = plugin.config().getGive();
			for(int i = 0; i < player.getInventory().getSize(); i++) {
				ItemStack item = player.getInventory().getItem(i);
				if(item != null && (item.getType() == Material.getMaterial(ungive) || item.getType() == Material.getMaterial(give))) {
					player.getInventory().setItem(i, null);
				}
				Boolean Lvl = plugin.config().getLvl();
				if(Lvl) {
					player.setLevel(0);
				}	    
			}
		}		
		player.updateInventory();
	}
	    @EventHandler
	    public void onPlayerMove(PlayerMoveEvent event) {
	    	Player player = event.getPlayer();
	    	if(plugin.getCraftingPlayers().containsKey(player)) {
	    		event.setCancelled(true);
	    	}	
	    }
	    	
	    @EventHandler
	    public void onDropItem(PlayerDropItemEvent event) {
	    	Player player = event.getPlayer();
	    	if(plugin.getCraftingPlayers().containsKey(player)) {
	    		event.setCancelled(true);
	    	}	
	    }
	    	
	    @EventHandler
	    public void onChat(AsyncPlayerChatEvent event) {
	    	Player player = event.getPlayer();
	    	if(plugin.getCraftingPlayers().containsKey(player)) {
	    		event.setCancelled(true);
	    	}	
	    }
	    	
	 	@EventHandler
	 	public void onCommand(PlayerCommandPreprocessEvent event) {
	 	   	Player player = event.getPlayer();
	 	   	if(plugin.getCraftingPlayers().containsKey(player)) {
	 	   		event.setCancelled(true);
	 	   	}	 	 
	 	}
	 	   	
	 	@EventHandler
	 	public void onBlockPlace(BlockPlaceEvent event) {
	 	    Player player = event.getPlayer();
	 	    if (plugin.getCraftingPlayers().containsKey(player)) {
	 	        event.setCancelled(true);
	 	    }
	 	}

	 	@EventHandler
	 	public void onBlockBreak(BlockBreakEvent event) {
	 	    Player player = event.getPlayer();
	 	    if (plugin.getCraftingPlayers().containsKey(player)) {
	 	        event.setCancelled(true);
	 	    }
	 	}

	 	public Player getPlayer() {
	 	    return this.player;
	 	}

	 	@EventHandler
	 	public void onPlayerJoin(PlayerJoinEvent event) {
	 	    Player player = event.getPlayer();
	 	    String nick = player.getName();	 	   
	 	    if(nick == null) return;
	 	    Players base = this.plugin.getBase();	 	  
	 	    if(!base.needVerifed(player)) {
	 	        Bukkit.getScheduler().runTaskLater(plugin, () -> {
	 	            List<String> commandsPlayer = this.plugin.config().getCommandsPlayer(player);
	 	            List<String> commandsServer = this.plugin.config().getCommandsServer(player);	 	           
	 	            for (String command : commandsPlayer) {
	 	                Bukkit.dispatchCommand((CommandSender) player, command);
	 	            }
	 	            for (String command : commandsServer) {
	 	                Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), command);
	 	            }
	 	        }, 20 * 1);
	 	    }
	 	    if(base.needVerifed(player)) {
	 	        int timer1 = this.plugin.config().getTimer();
	 	        plugin.getTimers().put(nick, timer1);
	 	        plugin.getCraftingPlayers().put(player, timer1);
	 	        plugin.addPlayer(player);
	 	        String ungive = plugin.config().getUnGive();
	 	        String give = plugin.config().getGive();
	 	        int givecount = plugin.config().getGiveCount();
	 	        if(plugin.getCraftingPlayers().containsKey(player)) {
	 	            player.getInventory().addItem(new ItemStack(Material.getMaterial(give), givecount));
	 	        }
	 	        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
	 	            if(plugin.getCraftingPlayers().containsKey(player)) {
	 	                plugin.getTimers().put(nick, plugin.getTimers().get(nick) - 1);
	 	                plugin.getCraftingPlayers().put(player, plugin.getTimers().get(nick) - 1);
	 	                String Message = this.plugin.config().getMessage(nick);
	 	                player.sendMessage(Message);
	 	                int level = player.getLevel();
	 	                if(level >= 0) {
	 	                    player.setLevel(level - 1);
	 	                }
	 	                Boolean Lvl = plugin.config().getLvl();
	 	                if(Lvl) {
	 	                    int lvl = plugin.getTimer(nick);
	 	                    player.setLevel(lvl);
	 	                }
	 	            }
	 	        }, 5, 20 * 1);
	 	        Boolean BossBarDisplay = plugin.config().getBossBarDisplay();
	 	        if(BossBarDisplay) {
	 	            if(plugin.getCraftingPlayers().containsKey(player)) {
	 	                String styleString = plugin.config().getBarStyle();
	 	                String colorString = plugin.config().getBarColor();
	 	                BarColor barColor = BarColor.valueOf(colorString.toUpperCase());
	 	                BarStyle barStyle = BarStyle.valueOf(styleString.toUpperCase());
	 	                BossBar bar = Bukkit.getServer().createBossBar(plugin.config().getBossBar(nick), barColor, barStyle); 	              
	 	                plugin.getBossBar().put(player, bar);
	 	                bar.addPlayer(player);
	 	                bar.setVisible(true);
	 	                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
	 	                    bar.setProgress((double) plugin.getTimer(nick) / timer1);
	 	                    bar.setTitle(plugin.config().getBossBar(nick));
	 	                }, 5, 20 * 1);
	 	                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
	 	                    if (plugin.getSuccess().containsKey(player)) {
	 	                        bar.removeAll();
	 	                        plugin.getSuccess().remove(player);
	 	                    }
	 	                }, 5, 20 * 1);
	 	            }
	 	            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
	 	                if(plugin.getCraftingPlayers().containsKey(player)) {
	 	                    if(player.getInventory().contains(Material.getMaterial(ungive))) {
	 	                        plugin.getCraftingPlayers().remove(player);
	 	                        plugin.getSuccess().put(player, 60);
	 	                        for(int i = 0; i < player.getInventory().getSize(); i++) {
	 	                            ItemStack item = player.getInventory().getItem(i);
	 	                            if(item != null && (item.getType() == Material.getMaterial(ungive) || item.getType() == Material.getMaterial(give))) {
	 	                                player.getInventory().setItem(i, null);
	 	                                base.setVerifed(player);
	 	                                plugin.getBossBar().remove(player);
	 	                                player.updateInventory();
	 	                                Boolean Lvl = plugin.config().getLvl();
	 	                                if(Lvl) {
	 	                                    player.setLevel(0);
	 	                                }
	 	                            }
	 	                        }
	 	                        String Success = plugin.config().getSuccess();
	 	                        player.sendMessage(Success);
	 	                        player.closeInventory();
	 	                    }
	 	                }
	 	            }, 5, 1 * 1);
	 	        }
	 	        List<Integer> list = Arrays.asList();
	 	        plugin.addTaskIds(nick, list);
	 	        Bukkit.getScheduler().runTaskLater(plugin, () -> {
	 	            if(plugin.getCraftingPlayers().containsKey(player)) {
	 	                plugin.getCraftingPlayers().remove(player);
	 	                plugin.getBossBar().remove(player);
	 	                plugin.getSuccess().put(player, 60);
	 	                String kick1 = plugin.config().getKick();
	 	                for(int i = 0; i < player.getInventory().getSize(); i++) {
	 	                    ItemStack item = player.getInventory().getItem(i);
	 	                    if(item != null && (item.getType() == Material.getMaterial(ungive) || item.getType() == Material.getMaterial(give))) {
	 	                        player.getInventory().setItem(i, null);
	 	                    }
	 	                }
	 	                player.updateInventory();
	 	                if(player != null) {
	 	                    plugin.removePlayer(player);
	 	                    player.kickPlayer(kick1);
	 	                }
	 	            }
	 	        }, timer1 * 20);
	 	    }
	 	}
}