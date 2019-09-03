package ru.sgk.thetowers.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Configurations {
	private static FileConfiguration config = null;
	private static FileConfiguration messages = null;
	private static FileConfiguration settings = null;

	public static String getLocaleString(String path){
		return ChatColor.translateAlternateColorCodes('&', getLocale().getString(path)).replaceAll("%prefix%", getLocale().getString("prefix").replaceAll("&", "§"));
	}
	public static List<String> getLocaleStringList(String path){
		List<String> l1 = new ArrayList<>();
		List<String> l2 = getLocale().getStringList(path);
		for (String s : l2)
		{
			l1.add(s.replaceAll("&", "§").replaceAll("%prefix%", getLocale().getString("prefix").replaceAll("&", "§")));
		}
		return l1;
	}
	public static void loadConfig()
	{
		config = Configuration.loadConfig("config.yml");
	}

	/**
	 * @throws NullPointerException if config was not loaded before
	 * @return
	 */
	public static FileConfiguration getConfig()
	{
		if (config == null) throw new NullPointerException("Configuration file is null"); 
		return config;
	}

	public static void relaodConfig()
	{
		loadConfig();
	}

	/**
	 * @throws NullPointerException if config was not loaded before
	 */
	public static void saveConfig()
	{
		if (config == null) throw new NullPointerException();
		Configuration.saveConfiguration(config, "config.yml");
	}

	/**
	 * @throws NullPointerException if messages was not loaded before
	 */
	public static FileConfiguration getLocale()
	{
		if (messages == null) throw new NullPointerException("Message file is null"); 
		return messages;
	}
	
	public static void loadLocale(String lang)
	{
		messages = Configuration.loadConfig("messages_"+lang+".yml");
	}
	
	public static void reloadLocale(String lang)
	{
		loadLocale(lang);
	}

	/**
	 * @throws NullPointerException if messages was not loaded before
	 */
	public static void saveLocale(String lang)
	{
		if (messages == null) throw new NullPointerException();
		Configuration.saveConfiguration(messages, "messages_"+lang+".yml");
	}
	
	public static void loadSettings()
	{
		settings = Configuration.loadConfig("settings.yml");
	}
	
	public static void relaodSettings()
	{
		loadSettings();
	}

	/**
	 * @throws NullPointerException if settings was not loaded before
	 */
	public static FileConfiguration getSettings()
	{
		if (settings == null) throw new NullPointerException();
		return settings;
	}

	/**
	 * @throws NullPointerException if settings was not loaded before
	 */
	public static void saveSettings()
	{
		if (settings == null) throw new NullPointerException();
		Configuration.saveConfiguration(settings, "settings.yml");
	}
}
