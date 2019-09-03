package ru.sgk.thetowers.game;

import org.bukkit.Location;

public class GameTeamArea
{
    private Location min, max;
    private Location spawnLoc;

    public GameTeamArea(Location min, Location max) {
        this.min = min;
        this.max = max;
        flipLocations();
    }

    public GameTeamArea(Location min, Location max, Location spawnLoc) {
        this.min = min;
        this.max = max;
        flipLocations();
        setSpawnLoc(spawnLoc);
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
        if (    spawnLoc.getBlockX() >= min.getBlockX() &&
                spawnLoc.getBlockY() >= min.getBlockY() &&
                spawnLoc.getBlockZ() >= min.getBlockZ() &&
                spawnLoc.getBlockX() <= max.getBlockX() &&
                spawnLoc.getBlockY() <= max.getBlockY()-1 &&
                spawnLoc.getBlockZ() <= max.getBlockZ())
        this.spawnLoc = spawnLoc;
    }

    private void flipLocations() {
        if (min.getY() > max.getY()) {
            Location tmp = min;
            min = max;
            max = tmp;
        }
    }
}
