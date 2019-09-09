package ru.sgk.thetowers.game.data.teams;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.TroopProcess;
import ru.sgk.thetowers.game.data.towers.AbstractTower;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;

import java.util.ArrayList;
import java.util.List;

public class GameTeam
{
    private GameTeamColor color;
    private int coins;
    private List<AbstractTower> placedTowers;
    private Location troopSpawn;
    private List<Location> troopWay;
    private Location troopsEnd;
    private GameTeamArea teamArea;
    private List<Player> players;
    private List<Block> towerBlocks;
    private double health;

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

    public void update() {

    }

    public void setArea(GameTeamArea area)
    {
        this.teamArea = area;
    }

    public void spawnTroop(AbstractTroop troop)
    {
        troop.setSpawnLocation(troopSpawn);
        troopWay.forEach(troop::addWayPoint);
        troop.setEndLocation(troopsEnd);

        TroopProcess.add(troop, this);
    }

    public void sendDamage(AbstractTroop damager)
    {
        health -= damager.getDamage();
    }

    public void sendTroops(Player sender, GameTeam teamToSend, AbstractTroop... troops)
    {
        for (AbstractTroop t : troops)
        {
            if (coins > t.getCost()){
                t.setSpawnLocation(teamToSend.troopSpawn);
                teamToSend.troopWay.forEach(t::addWayPoint);
                t.setEndLocation(teamToSend.troopsEnd);

                TroopProcess.add(t, teamToSend);
                sender.sendMessage(Configurations.getLocaleString("troop-sended").replaceAll("%team%", teamToSend.toString()));
            }

        }
    }


    public void setTroopsEnd(Location troopsEnd) {
        this.troopsEnd = troopsEnd;
    }

    public Location getTroopsEnd() {
        return troopsEnd;
    }

    public GameTeamArea getArea() {
        return teamArea;
    }

    public List<Block> getTowerBlocks() {
        return towerBlocks;
    }

    public void setTowerBlocks(List<Block> towerBlocks) {
        this.towerBlocks = towerBlocks;
    }

    public GameTeamColor getColor()
    {
        return color;
    }

    public int getCoins()
    {
        return coins;
    }

    public void setTroopWay(List<Location> troopWay) {
        this.troopWay = troopWay;
    }

    public List<AbstractTower> getPlacedTowers()
    {
        return placedTowers;
    }

    public Location getTroopSpawn()
    {
        return troopSpawn;
    }

    public List<Location> getTroopWay(){
        return troopWay;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void setTroopSpawn(Location troopSpawn) {
        this.troopSpawn = troopSpawn;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) players.add(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

    public boolean isInTroopEnd(Location loc)
    {
        return (loc.getBlockX() == troopsEnd.getBlockX() &&
                loc.getBlockY() == troopsEnd.getBlockY() &&
                loc.getBlockZ() == troopsEnd.getBlockZ() );
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null && getClass() != obj.getClass()) return false;
        GameTeam team = (GameTeam) obj;

        return team.color.equals(color);
    }

    @Override
    public String toString() {
        return Configurations.getLocaleString("teams."+color.toString());
    }
}
