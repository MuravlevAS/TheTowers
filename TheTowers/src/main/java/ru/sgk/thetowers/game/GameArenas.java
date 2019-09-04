package ru.sgk.thetowers.game;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import ru.sgk.thetowers.data.Configuration;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

import java.util.ArrayList;
import java.util.List;


public class GameArenas
{
    private static FileConfiguration arenasConfig;
    private static List<GameArena> arenas = Lists.newArrayList();
    private static List<String> stringList = Lists.newArrayList();
    public static List<GameArena> getArenas()
    {
        return arenas;
    }

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

    public static GameArena getArena(String name)
    {
        for (GameArena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void loadArenas()
    {
        arenasConfig = Configuration.loadConfig("arenas.yml");
        ConfigurationSection arenas = arenasConfig.getConfigurationSection("arenas");
        Logs.sendDebugMessage("Loading arenas");
        if (arenas != null)
        {
            for (String name : arenas.getKeys(false))
            {

                Logs.sendDebugMessage("Loading arena " + name);
                GameArena arena = new GameArena(name);
                ConfigurationSection teams = arenas.getConfigurationSection(name + ".teams");
                if (teams != null)
                {
                    Logs.sendDebugMessage("Loading teams");
                    for (String team : teams.getKeys(false))
                    {
                        Logs.sendDebugMessage("Loading team " + team);
                        GameTeam gTeam = null;
                        try {
                            gTeam = new GameTeam(GameTeamColor.valueOf(team));
                        } catch (IllegalArgumentException e) {
                            Logs.send("§cCannot load team with name " + team + "! Such team cannot exist!");
                        }
                        if (gTeam == null) continue;
                        Location min = (Location) teams.get(team + ".min");
                        Location max = (Location) teams.get(team + ".max");
                        Location spawn = (Location) teams.get(team + ".spawn");
                        Location troopsSpawn = (Location) teams.get(team + ".troops-spawn");
                        Location troopsEnd = (Location) teams.get(team + ".troops-end");
                        List<Location> troopsWayPoints = (List<Location>) teams.getList(team + ".troops-way-points", null);
                        List<Block> tmpBlockList = (List<Block>) teams.getList(team + ".tower-blocks", null);
                        List<Block> towerBlocks = Lists.newArrayList();

                        if (tmpBlockList != null) {
                            for (Block block : tmpBlockList) {
                                if (block != null && !block.isEmpty())
                                    towerBlocks.add(block);
                            }
                        }
                        if (!towerBlocks.isEmpty())
                            gTeam.setTowerBlocks(towerBlocks);

                        GameTeamArea area;
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
                Location lobbyLocation = (Location) arenas.get(name + ".lobby-location");
                if (lobbyLocation != null)
                    arena.setLobbyLocation(lobbyLocation);
                GameArenas.arenas.add(arena);
                Logs.sendDebugMessage("Arena " + arena + " successfully loaded");
            }
        }
    }

    public static void saveArenas()
    {
        if (arenasConfig == null)
             arenasConfig = Configuration.loadConfig("arenas.yml");
        for (GameArena arena : GameArenas.arenas)
        {
            String arenaName = arena.getArenaName();
            int teamSize = arena.getTeamSize();
            Location lobbyLocation = arena.getLobbyLocation();

            if (teamSize == 0) teamSize = 1;
            arenasConfig.set("arenas." + arenaName + ".team-size", teamSize);
            if (lobbyLocation != null)
                arenasConfig.set("arenas." + arenaName + ".lobby-location", lobbyLocation);
            for (GameTeam team : arena.getTeams())
            {
                String teamColor = team.getColor().toString();
                Location troopsSpawn = team.getTroopSpawn();
                Location troopsEnd = team.getTroopsEnd();
                List<Location> troopsWay = team.getTroopWay();
                List<Block> towerBlocks = team.getTowerBlocks();
                GameTeamArea area = team.getArea();
                Location min = area.getMin();
                Location max = area.getMax();
                Location spawn = area.getSpawnLoc();

                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".min", min);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".max", max);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".spawn", spawn);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".troops-spawn", troopsSpawn);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".troops-end", troopsEnd);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".troops-way-points", troopsWay);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".troops-spawn", troopsSpawn);
                arenasConfig.set("arenas." + arenaName + ".teams." + teamColor + ".tower-blocks", towerBlocks);


            }
        }
        Configuration.saveConfiguration(arenasConfig, "arenas.yml");
    }

    public static List<String> toStringList()
    {
        return stringList;
    }

    public static GameArena createArena(String name)
    {
        GameArena arena = new GameArena(name);
        if (addArena(arena))
            return new GameArena(name);
        return getArena(name);
    }
}
