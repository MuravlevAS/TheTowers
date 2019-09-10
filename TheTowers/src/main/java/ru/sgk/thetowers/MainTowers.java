package ru.sgk.thetowers;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sgk.thetowers.commands.MainTowersCommand;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.GameArenas;
import ru.sgk.thetowers.game.data.PlayerData;
import ru.sgk.thetowers.game.events.MainEvents;
import ru.sgk.thetowers.scoreboard.Board;
import ru.sgk.thetowers.utils.Logs;

/**
 * Main class of minecraft mini-game TheTowers <br>
 * To get object of this class use method getInstance()
 */
public class MainTowers extends JavaPlugin
{
	public static WorldEditPlugin wePlugin = null;
	private static MainTowers instance;
	public void onEnable()
	{
		Logs.send("Enabling plugin...");
		instance = this;
		loadAll();
		Logs.send("§aPlugin has enabled.");
		ConfigurationSection sect = Configurations.getLocale().getConfigurationSection("commands.towers.arenas");
	}
	
	public void onDisable()
	{
		
		Logs.sendDebugMessage("Disabling plugin...");
		Logs.send("§aPlugin has disabled.");
//		GameArenas.saveArenas();
	}

	public void loadAll()
	{
		Logs.send("Loading configuration");
		Configurations.loadConfig();
		Logs.send("§aConfig has loaded");

		Logs.init();

		Logs.send("Loading locales");
		Configurations.loadLocale(Configurations.getConfig().getString("lang"));
		Logs.send("§aLocaless has loaded");

		Logs.send("Loading settings");
		Configurations.loadSettings();
		Logs.send("§aSettings has loaded");

		getServer().getPluginManager().registerEvents(new MainEvents(), this);
		getServer().getPluginCommand("towers").setExecutor(new MainTowersCommand());


		Logs.sendDebugMessage(Bukkit.getVersion());
		Logs.sendDebugMessage(Bukkit.getBukkitVersion());


		Logs.send("Loading scoreboard system");
		Board.initScoreboard();
		PlayerData.getDataList().clear();
		for (Player p : Bukkit.getOnlinePlayers())
		{
			PlayerData.add(p);
		}

		Logs.send("Loading arenas");
		GameArenas.loadArenas();
		Logs.send("§aArenas has loaded");
	}

	public WorldEditPlugin getWorldEdit(){
		if (wePlugin != null) return wePlugin;
		Plugin p = getServer().getPluginManager().getPlugin("WorldEdit");
		return (p instanceof WorldEditPlugin) ? wePlugin = (WorldEditPlugin) p : null;
	}
	
	/**
	 * Using for getting some non-static fields
	 * @return
	 */
	public static MainTowers getInstance() {
		return instance;
	}
	
	/**
	 * Method not using. Use Configuration class for getConfig() instead 
	 * @return null!
	 */
	@Override
	public FileConfiguration getConfig() {return null;}
	
	/**
	 * Method not using. Use Configuration class for saveConfig() instead
	 */
	@Override
	public void saveConfig() {}
	
	/**
	 * Method not using. Use Configuration class for reloadConfig() instead
	 */
	@Override
	public void reloadConfig() {}
}
