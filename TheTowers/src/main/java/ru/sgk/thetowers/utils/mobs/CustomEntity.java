package ru.sgk.thetowers.utils.mobs;

import net.minecraft.server.v1_14_R1.*;

public class CustomEntity extends Entity {

    public CustomEntity(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initDatawatcher() {

    }

    @Override
    protected void a(NBTTagCompound nbtTagCompound) {

    }

    @Override
    protected void b(NBTTagCompound nbtTagCompound) {

    }

    @Override
    public Packet<?> N() {
        return null;
    }
}
