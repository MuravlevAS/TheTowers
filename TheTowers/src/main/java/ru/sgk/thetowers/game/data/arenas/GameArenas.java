package ru.sgk.thetowers.game.data.arenas;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.common.collect.Lists;

import ru.sgk.thetowers.data.Configuration;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.teams.GameTeamArea;
import ru.sgk.thetowers.game.data.teams.GameTeamColor;
import ru.sgk.thetowers.utils.Logs;


public class GameArenas
{
	/** Файл конфигурации арен. */
    private static FileConfiguration arenasConfig;
    /** Список зарегестрированных арен. */
    private static List<GameArena> arenas = Lists.newArrayList();
    /** Список зарегестрированных арен в строковом виде. */
    private static List<String> stringList = Lists.newArrayList();

    /**
     * Добавляет арену в список 
     * @param arena - арена, которую нужно добавить 
     * @return true, если арена была добавлена, в противном случае false
     */
    public static boolean addArena(GameArena arena)
    {
        if (!arenas.contains(arena))
        {
            arenas.add(arena);
            stringList.add(arena.getArenaName());
            return true;
        }
        return false;
    }

    /**
     * @return Арену соответствующую имени name; null, если такой арены нет.
     */
    public static GameArena getArena(String name)
    {
        for (GameArena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    /**
     * Загружает арены из файла arenas.yml
     */
    public static void loadArenas()
    {
    	// Загружаем конфиг
        loadConfig();
        // Получаем секцию в конфиге с аренами
        ConfigurationSection arenas = arenasConfig.getConfigurationSection("arenas");
        Logs.sendDebugMessage("Loading arenas");
        if (arenas != null)
        {
        	// Проходимся по аренам из конфига
            for (String name : arenas.getKeys(false))
            {
                Logs.sendDebugMessage("Loading arena " + name);
                GameArena arena = createArena(name);
                // Получаем из конфиге список команд в арене.
                ConfigurationSection teams = arenas.getConfigurationSection(name + ".teams");
                if (teams != null)
                {
                    Logs.sendDebugMessage("Loading teams");
                    for (String team : teams.getKeys(false))
                    {
                        Logs.sendDebugMessage("Loading team " + team);
                        GameTeam gTeam = null;
                        try 
                        {
                            gTeam = new GameTeam(GameTeamColor.valueOf(team));
                        }
                        catch (IllegalArgumentException e) 
                        {
                            Logs.send("§cCannot load team with name " + team + "! Such team cannot exist!");
                            continue;
                        }
                        // Получаем все значения команды из конфига
                        Location min = Configurations.getLocation(arenasConfig, teams.getCurrentPath() + "." + team + ".min");
                        Location max = Configurations.getLocation(arenasConfig ,teams.getCurrentPath() + "." + team + ".max");
                        Location spawn = Configurations.getLocation(arenasConfig, teams.getCurrentPath() + "." + team + ".spawn");
                        Location troopsSpawn = Configurations.getLocation(arenasConfig, teams.getCurrentPath() + "." + team + ".troops-spawn");
                        Location troopsEnd = Configurations.getLocation(arenasConfig, teams.getCurrentPath() + "." + team + ".troops-end");
                        List<Location> troopsWayPoints = Configurations.getLocationList(arenasConfig, teams.getCurrentPath() + "." + team + ".troops-way-points");
                        List<Location> blockLocationList = Configurations.getLocationList(arenasConfig,teams.getCurrentPath() + "." +team + ".tower-blocks");
                        List<Block> towerBlocks = Lists.newArrayList();
                        // Преобразуем локации в блоки.
                        if (blockLocationList != null) {
                            for (Location blockLocation : blockLocationList) {
                            	// Если блок есть, добавляем его в список 
                                if (blockLocation.getBlock() != null && !blockLocation.getBlock().isEmpty())
                                    towerBlocks.add(blockLocation.getBlock());
                            }
                        }
                        
                        if (!towerBlocks.isEmpty())
                            gTeam.setTowerBlocks(towerBlocks);
                        // Получаем область команды.
                        GameTeamArea area;
                        // Если координаты не равны null, создаем область.
                        if (min != null && max != null) {
                            if (spawn == null)
                                area = new GameTeamArea(min, max);
                            else
                                area = new GameTeamArea(min, max, spawn);
                            gTeam.setArea(area);
                        }
                        
                        if (troopsSpawn != null)
                            gTeam.setTroopSpawn(troopsSpawn);

                        if (troopsEnd != null) {
                            gTeam.setTroopsEnd(troopsEnd);
                        }
                        if (troopsWayPoints != null)
                            gTeam.setTroopWay(troopsWayPoints);
                        
                        arena.addTeam(gTeam);

                        Logs.sendDebugMessage("Team " + team + " successfully loaded");
                    }
                }
                // Устанавливаем локацию лобби из конфига в арену. 
                Location lobbyLocation = Configurations.getLocation(arenasConfig, arenas.getCurrentPath() + "." + name + ".lobby-location");
                if (lobbyLocation != null)
                    arena.setLobbyLocation(lobbyLocation);
                
                Logs.sendDebugMessage("Arena " + name + " successfully loaded");
            }
        }
    }
    /** 
     * Сохраняет все арены в конфиг.
     */
    public static void saveArenas()
    {
    	// Если конфиг равен null, создаём новый.
        if (arenasConfig == null)
             arenasConfig = Configuration.loadConfig("arenas.yml");
        
        for (GameArena arena : GameArenas.arenas)
        {
            arena.saveToConfig();
        }
        // Сохраняем конфиг.
        saveConfig();
    }

    public static List<String> toStringList()
    {
        return stringList;
    }

    /**
     * Удаляет арену с именем <b>name</b> <br>
     * После создания арены обязательно вызвать  saveToConfig();
     * @param name - название арены, которую нужно удалить
     * @return объект только что созданной арены
     */
    public static GameArena createArena(String name)
    {
        GameArena arena = new GameArena(name);
        // Если арена уже существует, возвращаем арену из списка.
        if (addArena(arena))
            return new GameArena(name);
        return getArena(name);
    }

    /**
     * Удаляет арену с именем <b>arena</b> <br>
     * @param arena - название арены, которую нужно удалить
     * @return объект удалённой арены
     */
    public static GameArena removeArena(String arena)
    {
        for (int i = 0; i < arenas.size(); i++)
        {
            if (arenas.get(i).getArenaName().equalsIgnoreCase(arena)){
                return arenas.remove(i);
            }
        }
        return null;
    }

    public static List<GameArena> getArenas()
    {
        return arenas;
    }
    /**
     * Загружаем конфиг с аренами
     */
    private static void loadConfig(){
        arenasConfig = Configuration.loadConfig("arenas.yml");
    }
    
    /**
     * @return Объект FileConfiguration, соответствующий файлу arenas.yml 
     */
    public static FileConfiguration getConfig()
    {
        if (arenasConfig == null) loadConfig();
        return arenasConfig;
    }

    public static void reloadConfig()
    {
        loadConfig();
    }
    /**
     * Сохраняет файл конфигурации arenas.yml
     */
    public static void saveConfig()
    {
        if (arenasConfig != null)
            Configuration.saveConfiguration(arenasConfig, "arenas.yml");
    }
}
