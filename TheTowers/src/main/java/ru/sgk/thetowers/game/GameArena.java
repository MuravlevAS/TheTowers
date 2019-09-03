package ru.sgk.thetowers.game;

import org.bukkit.Location;

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

    public void saveToConfig() {

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
}
