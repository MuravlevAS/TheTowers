package ru.sgk.thetowers.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.utils.Logs;

public class Configuration {
	public static FileConfiguration loadConfig(String filename)
	{
		Logs.sendDebugMessage("load " + filename);
		
		File file = new File(MainTowers.getInstance().getDataFolder() + "/"+ filename);
		FileConfiguration config = null;
		
		try(
			InputStream in = MainTowers.class.getResourceAsStream("/" + filename);
			Reader reader = new InputStreamReader(in))
		{
			// loading config
			config = YamlConfiguration.loadConfiguration(file);
			// set defaults to the config
			config.setDefaults(YamlConfiguration.loadConfiguration(reader));
			config.options().copyDefaults(true);

			saveConfiguration(config, filename);
			Logs.sendDebugMessage(filename + " has loaded");
		}
		catch (IOException e) 
		{
			Logs.sendDebugMessage("§cError has occured with loading " + filename+": " + e.getMessage());
			e.printStackTrace();
		}
		return config;
	}
	public static void saveConfiguration(FileConfiguration config, String filename)
	{
		File file = new File(MainTowers.getInstance().getDataFolder() + "/"+ filename);
		try {
			config.save(file);
		} catch (IOException e) {
			Logs.sendDebugMessage("§cError has occured with saving " + filename+": " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
}
