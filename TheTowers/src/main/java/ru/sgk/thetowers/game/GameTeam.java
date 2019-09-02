package ru.sgk.thetowers.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import ru.sgk.thetowers.game.data.towers.AbstractTower;

import java.util.ArrayList;
import java.util.List;

public class GameTeam
{
    private GameTeamColor color;
    private int coins;
    private List<AbstractTower> placedTowers;
    private Location troopSpawn;
    private List<Location> troopWay;
    private Location troopEnd;
    private GameTeamArea teamArea;
    private List<Player> players;

    public GameTeam(GameTeamColor color) {
        this.color = color;

        this.coins = 0;
        this.placedTowers = new ArrayList<>();
        this.troopWay = new ArrayList<>();
        this.teamArea = null;
    }

    public GameTeam(GameTeamColor color, GameTeamArea area){
        this(color);
        this.teamArea = area;
    }

    public GameTeamColor getColor()
    {
        return color;
    }

    public int getCoins()
    {
        return coins;
    }

    public List<AbstractTower> getPlacedTowers()
    {
        return placedTowers;
    }

    public Location getTroopSpawn()
    {
        return troopSpawn;
    }

    public List<Location> getTroopWay()
    {
        return troopWay;
    }

    public Location getTroopEnd()
    {
        return troopEnd;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void setTroopSpawn(Location troopSpawn) {
        this.troopSpawn = troopSpawn;
    }

    public void setTroopEnd(Location troopEnd) {
        this.troopEnd = troopEnd;
    }

    public void addPlaeyr(Player player) {
        if (!players.contains(player)) players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null && getClass() != obj.getClass()) return false;
        GameTeam team = (GameTeam) obj;

        return team.color.equals(color);
    }
}
