package ru.sgk.thetowers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import ru.sgk.thetowers.commands.MainTowersCommand;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.PlayerData;
import ru.sgk.thetowers.game.data.arenas.GameArenas;
import ru.sgk.thetowers.game.events.MainEvents;
import ru.sgk.thetowers.scoreboard.Board;
import ru.sgk.thetowers.utils.Logs;

/**
 * Main class of minecraft mini-game TheTowers <br>
 * To get object of this class use method getInstance()
 */
public class MainTowers extends JavaPlugin
{
	private static WorldEditPlugin wePlugin = null;
	private static MainTowers instance;
	
	public void onEnable()
	{
		Logs.send("Enabling plugin...");
		
		// make instance of this class.
		instance = this;
		
		if (getWorldEdit() == null)
		{
			Logs.send("§cCannot find WorldEdit. Please make sure this plugin is installed.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		// registering commands.
		getServer().getPluginCommand("towers").setExecutor(new MainTowersCommand());
		
		// loading yml files, players. Initialization of scoreboard
		loadAll();

		// registering events
		getServer().getPluginManager().registerEvents(new MainEvents(), this);
		
		Logs.send("§aPlugin has enabled.");
	}
	
	public void onDisable()
	{
		Logs.sendDebugMessage("Disabling plugin...");
		Logs.send("Plugin has disabled.");
		// TODO: остановка запущенных игр.
	}
	/**
	 * Перезагружает плагин
	 */
	public void reload()
	{
		// TODO: Остановка всех игр
		// TODO: Удалить все арены из памяти
		// TODO: Загрузить всё обратно
	}
	
	/**
	 * Загружает все yml файлы, все арены, игроков. Инициализирует скорборд. <br>
	 */
	public void loadAll()
	{
		Logs.send("Loading configuration");
		Configurations.loadConfig();
		Logs.send("§aConfig has loaded");

		Logs.init();

		Logs.send("Loading locales");
		Configurations.loadLocale(Configurations.getConfig().getString("lang"));
		Logs.send("§aLocales has loaded");

		Logs.send("Loading settings");
		Configurations.loadSettings();
		Logs.send("§aSettings has loaded");



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
	
	/**
	 * @return Объект класса WorldEditPlugin. Null, если WorldEdit не установлен на сервере.
	 */
	public WorldEditPlugin getWorldEdit(){
		if (wePlugin != null) return wePlugin;
		Plugin p = getServer().getPluginManager().getPlugin("WorldEdit");
		return (p instanceof WorldEditPlugin && p != null) ? wePlugin = (WorldEditPlugin) p : null;
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
