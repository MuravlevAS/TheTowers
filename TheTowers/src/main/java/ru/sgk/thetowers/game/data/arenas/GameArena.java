package ru.sgk.thetowers.game.data.arenas;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import com.google.common.collect.Lists;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.teams.GameTeamArea;

public class GameArena
{
	/** Команды, которые есть в арене */
    private List<GameTeam> teams;
    /** Максимум игроков, которые могут зайти в игру*/ 
    private int maxPlayers;
    /** Локация лобби */
    private Location lobbyLocation;
    /** Название арены */
    private String arenaName;
    /** Количество игроков в команде */
    private int teamSize;

    public GameArena(String arenaName){
        this();
        this.arenaName = arenaName;
    }

    public GameArena()
    {
        this.teamSize = 1;
        teams = Lists.newArrayList();
    }
    
    public void removeTeam(GameTeam team)
    {
        if (teams.remove(team))
            this.maxPlayers-=teamSize;
    }
    
    public void addTeam(GameTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
            this.maxPlayers+=teamSize;
        }
    }
    
    public List<GameTeam> getTeams() {
        return teams;
    }
    /**
     * Сохраняет арену в конфиг.
     * После выполнения этого метода, сохраните конфигурацию, используя метод {@link #GameArenas.saveConfig() GameArenas.saveConfig()} 
     */
    public void saveToConfig(){

        if (teamSize <= 0) teamSize = 1;
        // Установка в конфиге имени арены 
        GameArenas.getConfig().set("arenas." + arenaName + ".team-size", teamSize);
        if (lobbyLocation != null)
            GameArenas.getConfig().set("arenas." + arenaName + ".lobby-location", lobbyLocation);
        // Проходим по имеющимся командам.
        for (GameTeam team : teams)
        {
        	// Получаем все переменные из команды
            String teamColor = team.getColor().toString();
            Location troopsSpawn = team.getTroopSpawn();
            Location troopsEnd = team.getTroopsEnd();
            List<Location> troopsWay = team.getTroopWay();
            List<Location> towerBlocks = Lists.newArrayList();
            // Преобразуем блоки для таверов в массив их локациями
            if (team.getTowerBlocks()!=null)
            {
	            for (Block block : team.getTowerBlocks())
	            {
	                towerBlocks.add(block.getLocation());
	            }
            }
            // Получаем область команды
            GameTeamArea area = team.getArea();
            Location min = null, max = null, spawn = null;
            if (area !=null)
            {
                min = area.getMin();
                max = area.getMax();
                spawn = area.getSpawnLoc();
            }
            // Устанавливаем значения в конфиге.
            if (min != null)
                Configurations.saveLocationAsString (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".min", min);
            if (max != null)
                Configurations.saveLocationAsString (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".max", max);
            if (spawn != null)
                Configurations.saveLocationAsString (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".spawn", spawn);
            if (troopsSpawn != null)
                Configurations.saveLocationAsString (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".troops-spawn", troopsSpawn);
            if (troopsEnd != null)
                Configurations.saveLocationAsString (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".troops-end", troopsEnd);
            if (troopsWay != null)
                Configurations.saveLocationList     (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".troops-way-points", troopsWay);
            if (towerBlocks != null)
                Configurations.saveLocationList     (GameArenas.getConfig(), "arenas." + arenaName + ".teams." + teamColor + ".tower-blocks", towerBlocks);
        }
    }


    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
        this.maxPlayers = teams.size()*this.teamSize;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (obj.getClass().equals(getClass())) return false;

        GameArena arena = (GameArena)obj;
        return arena.getArenaName().equalsIgnoreCase(this.arenaName);

    }
}
