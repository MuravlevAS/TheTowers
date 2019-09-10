package ru.sgk.thetowers.data;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import ru.sgk.thetowers.utils.Logs;

import java.util.ArrayList;
import java.util.List;

public class Configurations {
	private static FileConfiguration config = null;
	private static FileConfiguration locale = null;
	private static FileConfiguration settings = null;


	private static Location stringToLoc(String s)
	{
		String[] locationFields = s.split(",");

		World world = Bukkit.getWorld(locationFields[0].replaceAll(" ", ""));

		double x = Double.parseDouble(locationFields[1].replaceAll(" ", ""));
		double y = Double.parseDouble(locationFields[2].replaceAll(" ", ""));
		double z = Double.parseDouble(locationFields[3].replaceAll(" ", ""));

		return new Location(world, x, y, z);
	}
	private static String locToString(Location loc)
	{
		World world = loc.getWorld();
		String worldString = world.getName();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		return worldString	.concat(", ").concat(String.valueOf(x))
				.concat(", ").concat(String.valueOf(y))
				.concat(", ").concat(String.valueOf(z));
	}

	/**
	 * Загружает объект Location из строки вида "<i>{worldname}, {x}, {y}, {z}</i>"
	 * Например: <i>world, 0.0, 0.0, 0.0</i>
	 *
	 * @param config конфиг, из которого загружаем
	 * @param path путь в конфиге, откуда загружать локацию
	 * @return объект Location созданный при помощи new Location(worldname, x, y, z)
	 */
	public static Location getLocation(FileConfiguration config, String path)
	{
		if (!config.contains(path)) return null;
		String s = config.getString(path);
		return stringToLoc(config.getString(path));
	}
	public static List<Location> getLocationList(FileConfiguration config, String path)
	{
		List<String> stringList = config.getStringList(path);
		List<Location> locationList = Lists.newArrayList();
		for (String str : stringList)
		{
			locationList.add(stringToLoc(str));
		}
		return locationList;
	}

	public static void saveLocationList(FileConfiguration config, String path, List<Location> locList)
	{
		List<String> stringList = Lists.newArrayList();
		for (Location loc : locList)
		{
			stringList.add(locToString(loc));
		}
		config.set(path, stringList);
	}

	/**
	 * Созраняет объект Location в виде строки "<i>{worldname}, {x}, {y}, {z}</i>"
	 * @param config конфиг, в который сохраняем
	 * @param path путь в конфиге, куда сохранять
	 * @param loc локация, которую сохраняем
	 */
	public static void saveLocationAsString(FileConfiguration config, String path, Location loc)
	{
		String saveString = locToString(loc);
		Logs.sendDebugMessage("saving location ".concat(saveString));
		config.set(path, saveString);
	}

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
		if (locale == null) throw new NullPointerException("Locale file is null");
		return locale;
	}
	
	public static void loadLocale(String lang)
	{
		locale = Configuration.loadConfig("locale_"+lang+".yml");
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
		if (locale == null) throw new NullPointerException();
		Configuration.saveConfiguration(locale, "locale_"+lang+".yml");
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
