package ru.sgk.thetowers.game;

import org.bukkit.Location;

public class GameTeamArea
{
    private Location min, max;
    private Location spawnLoc;

    public GameTeamArea(Location min, Location max) {
        this.min = min;
        this.max = max;
    }

    public GameTeamArea(Location min, Location max, Location spawnLoc) {
        this.min = min;
        this.max = max;
        this.spawnLoc = spawnLoc;
    }

    public Location getMin() {
        return min;
    }

    public void setMin(Location min) {
        this.min = min;
    }

    public Location getMax() {
        return max;
    }

    public void setMax(Location max) {
        this.max = max;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public void setSpawnLoc(Location spawnLoc) {
        this.spawnLoc = spawnLoc;
    }
}
