package ru.sgk.thetowers.game.data.arenas;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.block.Block;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.teams.GameTeamArea;

import java.util.ArrayList;
import java.util.List;

public class GameArena
{
    private List<GameTeam> teams;
    private int maxPlayers;
    private Location lobbyLocation;
    private String arenaName;
    private int teamSize;

    public GameArena(String arenaName){
        this();
        this.arenaName = arenaName;
    }

    public GameArena()
    {
        this.teamSize = 1;
        teams = new ArrayList<>();
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

    public void saveToConfig(){

        if (teamSize <= 0) teamSize = 1;
        GameArenas.getConfig().set("arenas." + arenaName + ".team-size", teamSize);
        if (lobbyLocation != null)
            GameArenas.getConfig().set("arenas." + arenaName + ".lobby-location", lobbyLocation);
        for (GameTeam team : teams)
        {
            String teamColor = team.getColor().toString();
            Location troopsSpawn = team.getTroopSpawn();
            Location troopsEnd = team.getTroopsEnd();
            List<Location> troopsWay = team.getTroopWay();
            List<Location> towerBlocks = Lists.newArrayList();
            if (team.getTowerBlocks()!=null)
            for (Block block : team.getTowerBlocks())
            {
                towerBlocks.add(block.getLocation());
            }
            GameTeamArea area = team.getArea();
            Location min = null, max = null, spawn = null;
            if (area !=null)
            {
                min = area.getMin();
                max = area.getMax();
                spawn = area.getSpawnLoc();
            }
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
        if (obj == null) return this == null;
        if (this == obj) return true;
        if (obj.getClass() == getClass()) return false;

        GameArena arena = (GameArena)obj;
        return arena.getArenaName().equalsIgnoreCase(this.arenaName);

    }
}
