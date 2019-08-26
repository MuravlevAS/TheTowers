package ru.sgk.thetowers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
		Logs.setLoggger(getLogger());
		Logs.sendDebugMessage("Enabling plugin...");
		Logs.send("§aPlugin has enabled.");
	}
	
	public void onDisable()
	{
		Logs.sendDebugMessage("Disabling plugin...");
		Logs.send("§aPlugin has disabled.");
	}
	
	public static MainTowers getInstance() {
		return instance;
	}
	@Override
	public FileConfiguration getConfig()
	{
		return null;
	}
	
	@Override
	public void saveConfig() {}
	@Override
	public void reloadConfig() {}
}
