package ru.sgk.thetowers.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

import ru.sgk.thetowers.utils.Logs;
/**
 * Конфиги
 * @author SteveGrKek
 */
public class Configurations 
{
	/** config.yml */
	private static FileConfiguration config = null;
	/** locale_[lang].yml */
	private static FileConfiguration locale = null;
	/** config.yml */
	private static FileConfiguration settings = null;
	private static String localeString;

	/**
	 * Переводит Строку вида {world}, {x}, {y}, {z} объект Location с соответствующими значениями 
	 * @param s - строка, которая будет переведена в локацию  
	 * @return - локацию с соответствующими значениями world, x, y, z; null, если строка s равна null или если при чтении строки возникла ошибка
	 */
	private static Location stringToLoc(String s)
	{
		if (s == null) return null;
		try
		{
			// Разделяем строку на массив.
			// Массив должен содержать 4 элемента
			// 1. world name 
			// 2. Координата x
			// 3. Координата y
			// 4. Координата z
			String[] locationFields = s.split(",");
			// Получаем мир из строки
			World world = Bukkit.getWorld(locationFields[0].replaceAll(" ", ""));
			// Выводим сообщение об ошибке, если мир не найден.
			if (world == null)
			{
				Logs.send("§cCannot convert string " + s + " to Location object.");
				Logs.send("World with name " + locationFields[0].replaceAll(" ", "") + " is not exist!");
				return null;
			}
			// получаем координаты 
			double x = Double.parseDouble(locationFields[1].replaceAll(" ", ""));
			double y = Double.parseDouble(locationFields[2].replaceAll(" ", ""));
			double z = Double.parseDouble(locationFields[3].replaceAll(" ", ""));
			// Создаём объект Location и возвращаем его. 
			return new Location(world, x, y, z);
		}
		catch(Exception e){
			Logs.send("§c&lCannot convert string \"" + s + "\" to Location object. Wrong format.");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Переводит локацию <i>loc</i> в строку вида <i>{world}, {x}, {y}, {z}</i>
	 * @param loc локация, которую нужно преобразовать в строку
	 * @return - строку с соотвутствующую описанию; null если локация равна null.
	 */
	private static String locToString(Location loc)
	{
		if (loc == null) return null;
		
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
	 * @return объект Location созданный при помощи new Location(worldname, x, y, z); null, если переменная в конфиге не найдена
	 */
	public static Location getLocation(FileConfiguration config, String path)
	{
		if (!config.contains(path)) return null;
		
		String s = config.getString(path);
		
		return stringToLoc(s);
	}
	
	/**
	 * Преобразует массив строк из конфига с помощью метода {@link #stringToLoc(String) stringToLock()} в массив с локациями
	 * @param config - конфиг из которого нужно получить список
	 * @param path - путь к списку в конфиге
	 * @return список из объектов Location; 
	 * @throws NullPointerException если не найдена строка в конфиге
	 */
	public static List<Location> getLocationList(FileConfiguration config, String path) throws NullPointerException
	{
		List<String> stringList = config.getStringList(path);
		List<Location> locationList = Lists.newArrayList();
		
		for (String str : stringList)
		{
			Location l = stringToLoc(str);
			// Если stringToLoc выдаёт ошибку, он выведет её в консоль.
			// Просто пропускаем элемент 
			if (l != null)
			locationList.add(l);
		}
		return locationList;
	}
	/**
	 * Сохраняет список локаций в конфиг
	 * @param config - конфиг, в который нужно сохранить список
	 * @param path - путь к списку в конфиге
	 * @param locList - список локаций, который надо сохранить
	 * @throws NullPointerException если какой-то из аргументво равен null 
	 */
	public static void saveLocationList(FileConfiguration config, String path, List<Location> locList) throws NullPointerException
	{
		// Если один их аргументов равен null, то выбрасываем исключение. 
		if (config == null || locList == null || path == null)
			throw new NullPointerException();
		List<String> stringList = Lists.newArrayList();
		for (Location loc : locList)
		{
			String s = locToString(loc);
			// Если locToString выдаёт ошибку, он выведет её в консоль.
			// Просто пропускаем элемент 
			if (s != null)
			stringList.add(s);
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
	/**
	 * Получает строку из файла с локализацией 
	 * @param path - путь в конфиге 
	 * @return
	 */
	public static String getLocaleString(String path){
		return getLocale().getString(path).replaceAll("%prefix%", getLocale().getString("prefix")).replaceAll("&", "§");
	}
	/**
	 * Поулчает список строк из файла с локализацией 
	 * @param path - путь в конфиге
	 */
	public static List<String> getLocaleStringList(String path){
		List<String> l1 = new ArrayList<>();
		List<String> l2 = getLocale().getStringList(path);
		for (String s : l2)
		{
			l1.add(s.replaceAll("%prefix%", getLocale().getString("prefix")).replaceAll("&", "§"));
		}
		return l1;
	}
	
	/**
	 * Загружает файл конфигурации config.yml для последующего его получения с помощью {@link #getConfig() getConfig()}
	 */
	public static void loadConfig()
	{
		config = Configuration.loadConfig("config.yml");
	}

	/**
	 * @throws NullPointerException if config was not loaded before
	 * @return config.yml
	 */
	public static FileConfiguration getConfig()
	{
		if (config == null) loadConfig(); 
		return config;
	}
	/**
	 * Перезагружает конфиг.
	 * По сути, просто вызывает метод {@link #loadConfig() loadConfig()}
	 */
	public static void relaodConfig()
	{
		loadConfig();
	}

	/**
	 * Сохраняет конфиг config.yml
	 * @throws NullPointerException если конфиг не был ранее загружен 
	 */
	public static void saveConfig() throws NullPointerException
	{
		if (config == null) throw new NullPointerException();
		Configuration.saveConfiguration(config, "config.yml");
	}

	/**
	 * Возвращает объект типа FileConfiguration соответствующий locale_[lang].yml<br>
	 * <b>lang</b> - язык, который указывается при загрузке локализаций методом {@link #loadLocale(String) loadLocale(lang)}
	 * @throws NullPointerException если локализация не была ранее загружена
	 */
	public static FileConfiguration getLocale()
	{
		if (locale == null) throw new NullPointerException("Locale file is null");
		return locale;
	}
	
	/**
	 * Загружает лоализацию с языком lang
	 * @param lang - язык (:P)
	 * @throws NullPointerException если не найден файл с именем <i>locale_[lang].yml</i>
	 */
	public static void loadLocale(String lang) throws NullPointerException
	{
		locale = Configuration.loadConfig("locale_"+lang+".yml");
		if (locale == null) throw new NullPointerException("Locale file is null");
		localeString = lang;
	}
	
	/**
	 * Сохраняет файл локализации
	 * @throws NullPointerException if messages was not loaded before
	 */
	public static void saveLocale(String lang)
	{
		if (locale == null) throw new NullPointerException();
		Configuration.saveConfiguration(locale, "locale_"+lang+".yml");
	}
	/**
	 * Загружает файл с настройками таверов и мобов
	 */
	public static void loadSettings()
	{
		settings = Configuration.loadConfig("settings.yml");
	}

	/**
	 * Получает настройки таверов и мобов. Если конфиг ещё не был загружен, он загружается с помощью {@link #loadSettings() loadSettings()}
	 */
	public static FileConfiguration getSettings()
	{
		if (settings == null) loadSettings();
		return settings;
	}

	/**
	 * Сохраняет настройки таверов и мобов в файл settings.yml
	 * @throws NullPointerException if settings was not loaded before
	 */
	public static void saveSettings()
	{
		if (settings == null) throw new NullPointerException();
		Configuration.saveConfiguration(settings, "settings.yml");
	}

	/**
	 * @return строку, соответствующую языку локализации, которая была ранее загружена методом {@link #loadLocale(String) loadLocale()}
	 */
	public static String getLangString() 
	{
		return localeString;
	}
}
