package ru.sgk.thetowers.data;

import org.bukkit.configuration.file.FileConfiguration;

public class Configurations {
	private static FileConfiguration config = null;
	private static FileConfiguration messages = null;
	
	public static void loadConfig()
	{
		config = Configuration.loadConfig("config.yml");
	}

	public static FileConfiguration getConfig()
	{
		if (config == null) loadConfig(); 
		return config;
	}
	
	public static void relaodConfig()
	{
		loadConfig();
	}
	
	public static void saveConfig()
	{
		Configuration.saveConfiguration(config, "config.yml");
	}
	
	public static FileConfiguration getMessages()
	{
		if (messages == null) loadMessages(config.getString("locale"));
		return messages;
	}
	
	public static void loadMessages(String lang)
	{
		messages = Configuration.loadConfig("messages_"+lang+".yml");
	}
	
	public static void reloadMessages(String lang)
	{
		loadMessages(lang);
	}
	
	public static void saveMessages(String lang)
	{
		Configuration.saveConfiguration(messages, "messages_"+lang+".yml");
	}
}
