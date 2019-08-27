package ru.sgk.thetowers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

/**
 * Main class of minecraft mini-game TheTowers <br>
 * To get object of this class use method getInstance()
 */
public class MainTowers extends JavaPlugin
{
	private static MainTowers instance;
	
	public void onEnable()
	{
		instance = this;

		Logs.send("Enabling plugin...");
		Logs.send("Loading configuration");
		Configurations.loadConfig();
		Logs.send("§aConfig loaded");
		
		Logs.send("Loading messages");
		Configurations.loadMessages(Configurations.getConfig().getString("lang"));
		Logs.send("§aMessages loaded");
		
		
		Logs.init();
		Logs.send("§aPlugin has enabled.");
	}
	
	public void onDisable()
	{
		Logs.sendDebugMessage("Disabling plugin...");
		Logs.send("§aPlugin has disabled.");
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
