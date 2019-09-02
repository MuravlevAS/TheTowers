package ru.sgk.thetowers.game;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class GameArena
{
    private List<GameTeam> teams;
    private int maxPlayers;
    private Location lobbyLocation;

    public GameArena()
    {
        teams = new ArrayList<>();
    }

    public GameArena(int maxPlayers)
    {
        this();
        for (int i = 0; i < (Math.min(maxPlayers, 12)); i++) {
            addTeam(new GameTeam(GameTeamColor.values()[i]));
        } 
    }

    public void removeTeam(GameTeam team)
    {
        if (teams.remove(team))
            this.maxPlayers--;
    }


    public void addTeam(GameTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
            this.maxPlayers++;
        }
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }
}
