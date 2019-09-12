package ru.sgk.thetowers.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.utils.Logs;

import java.io.*;

public class Configuration {
	
	/**
	 * Загрузка конфига из файла <i>filename</i>.<br>
	 * Если файла нет в папке плагина, то конфиг будет загружен из файла внутри jar файла плагина.
	 * @param filename - имя файла, который должен быть загружен.
	 * @return - объект FileConfiguration, соответствующий файлу <i>filename</i> в папке плагина или jar файле; null если файл нигде не найден
	 */
	public synchronized static FileConfiguration loadConfig(String filename)
	{
		Logs.sendDebugMessage("load " + filename);
		// Getting file from plugin's folder. 
		File file = new File(MainTowers.getInstance().getDataFolder() + "/"+ filename);
		
		// Config that will returned
		FileConfiguration config = null;

		try(
			// Loading file from plugin's jar file as InputStreams
			InputStream in = MainTowers.class.getResourceAsStream("/" + filename);
			Reader reader = new InputStreamReader(in))
		{

			// Loading config
			config = YamlConfiguration.loadConfiguration(file);
			// Set defaults to the config
			config.setDefaults(YamlConfiguration.loadConfiguration(reader));
			config.options().copyDefaults(true);
			// Save just loaded configurations into file.
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
	
	/**
	 * Сохранение конфига <i>config</i> в папку плагина в файл с именем <i>filename</i> 
	 * @param config - конфиг, который нужно сохранить
	 * @param filename - имя файла, в который сохранится конфиг
	 */
	public synchronized static void saveConfiguration(FileConfiguration config, String filename)
	{
		// Создание файла с именем filename в папке плагина
		File file = new File(MainTowers.getInstance().getDataFolder() + "/"+ filename);
		try {
			// сохранение конфига в этот файл
			config.save(file);
		} catch (IOException e) {
			Logs.sendDebugMessage("§cError has occured with saving " + filename+": " + e.getMessage());
			e.printStackTrace();
		}
	}
}
