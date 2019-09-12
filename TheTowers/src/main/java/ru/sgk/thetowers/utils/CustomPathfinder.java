package ru.sgk.thetowers.utils;

import net.minecraft.server.v1_14_R1.EntityInsentient;
import net.minecraft.server.v1_14_R1.Navigation;
import net.minecraft.server.v1_14_R1.PathEntity;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import org.bukkit.Location;

public class CustomPathfinder extends PathfinderGoal {

    private double speed;
    private Location loc;
    private EntityInsentient entity;

    public CustomPathfinder(EntityInsentient entity, Location loc, double speed)
    {
        this.entity = entity;
        this.loc = loc;
        this.speed = speed;
    }
    @Override
    public boolean a()
    {
        return true;
    }

    @Override
    public void c()
    {
        PathEntity pathEntity = entity.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(),0);

        this.entity.getNavigation().a(loc.getX(), loc.getY(), loc.getZ(), speed);

//        entity.targetSelector.a(PathfinderGoal.Type.MOVE);
    }

    @Override
    public boolean b()
    {
        return true;
    }
}
