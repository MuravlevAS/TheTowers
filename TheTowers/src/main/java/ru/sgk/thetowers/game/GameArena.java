package ru.sgk.thetowers.game;

import org.bukkit.Location;
import org.bukkit.block.Block;
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
            List<Block> towerBlocks = team.getTowerBlocks();
            GameTeamArea area = team.getArea();
            Location min = area.getMin();
            Location max = area.getMax();
            Location spawn = area.getSpawnLoc();

            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".min", min);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".max", max);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".spawn", spawn);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".troops-spawn", troopsSpawn);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".troops-end", troopsEnd);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".troops-way-points", troopsWay);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".troops-spawn", troopsSpawn);
            GameArenas.getConfig().set("arenas." + arenaName + ".teams." + teamColor + ".tower-blocks", towerBlocks);
        }
        GameArenas.saveConfig();
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
