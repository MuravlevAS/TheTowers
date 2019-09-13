package ru.sgk.thetowers.game.data.teams;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.TroopProcess;
import ru.sgk.thetowers.game.data.towers.AbstractTower;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;

public class GameTeam
{
	/** Цвет команды */
    private GameTeamColor color;
    /** Количество денег у команды*/
    private int coins;
    /** Установленные башни*/
    private List<AbstractTower> placedTowers;
    /** Спавн мобов*/
    private Location troopSpawn;
    /** Точки их пути. Нужны для определения поворотов*/
    private List<Location> troopWay;
    /** Конечная точка пути мобов*/
    private Location troopsEnd;
    /** Область команды. То, где игроки в команде смогут находиться*/
    private GameTeamArea teamArea;
    /** Игроки*/
    private List<Player> players;
    /** Блоки, на которые можно ставить башни*/
    private List<Block> towerBlocks;
    /** Жизни команды*/
    private double health;

    public GameTeam(GameTeamColor color) {
        this.color = color;
        this.coins = 0;
        this.placedTowers = Lists.newArrayList();
        this.troopWay = Lists.newArrayList();
        this.teamArea = null;
        this.towerBlocks = Lists.newArrayList();
        this.players = Lists.newArrayList();
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

        TroopProcess.add(troop);
    }
    
    public double getHealth()
    {
    	return health;
    }
    /**
     * Отправить урон команде
     * @param damager моб, который нанёс урон
     */
    public void sendDamage(AbstractTroop damager)
    {
        health -= damager.getDamage();
    }
    /**
     * Отправить другой команде мобов
     * @param sender - тот, кто отправляет
     * @param teamToSend - тот, кому отправить 
     * @param troops - мобы, которых нужно отправить 
     */
    public void sendTroops(Player sender, GameTeam teamToSend, AbstractTroop... troops)
    {
        for (AbstractTroop t : troops)
        {
            if (coins > t.getCost()){
                t.setSpawnLocation(teamToSend.troopSpawn);
                teamToSend.troopWay.forEach(t::addWayPoint);
                t.setEndLocation(teamToSend.troopsEnd);
                t.setParentTeam(teamToSend);
                TroopProcess.add(t);
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
        if (obj == null || this == null) return false;

        if (obj.getClass() != this.getClass()) return false;
        
        GameTeam team = (GameTeam) obj;

        return team.color.equals(color);
    }

    @Override
    public String toString() {
        return Configurations.getLocaleString("teams."+color.toString());
    }
}
