package ru.sgk.thetowers.game.data.troops;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetEvent;
import ru.sgk.thetowers.utils.Logs;
import ru.sgk.thetowers.utils.mobs.CustomPathFinder;

import java.util.List;

public abstract class AbstractTroop
{

	public static List<AbstractTroop> allSpawnedTroops = Lists.newArrayList();
	protected ConfigurationSection configSection;
	private String title;
	private double damage;
	private double speed;
	private double cost;
	private double health;
	private double killed;
	private LivingEntity entity;
	private EntityType mobType;
	private Location spawnLocation;
	private Location endLocation;
	private List<Location> wayPoints;
	private Entity entityTemplate;
	private boolean isKilled;
	private boolean invisible = false;
	private int currentWayPoint;
    private Location movingTo;

	public AbstractTroop() {
		currentWayPoint = 0;
	}

	public void spawn(Location loc) throws NullPointerException {
		entity = (LivingEntity) loc.getWorld().spawnEntity(loc, mobType);

		entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setHealth(health);
		try {
			((EntityInsentient)((CraftEntity)entity).getHandle()).setSlot(EnumItemSlot.HEAD, new ItemStack(Blocks.STONE_BUTTON));

		}catch (Exception e) {}

		try {
			entity.getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(speed);
		}catch (Exception e) {}
		entity.setCollidable(false);
		entity.setInvulnerable(true);
//		EntityLiving living = (EntityLiving) entity;
//		living.setSlot(EnumItemSlot.HEAD, new ItemStack(Blocks.STONE_BUTTON));
	}

	public void despawn(){
		if (entity != null)
			entity.remove();
	}

	public void kill(){
		if (entity != null){
			isKilled = true;
			entity.damage(entity.getHealth());

		}
	}

	public void sendDamage(double damage){
		if (entity != null){
			if (damage >= entity.getHealth())
				isKilled = true;
			else isKilled = false;
			entity.damage(damage);
		}
	}

	public void move(Location loc)
	{
		// TODO: move mob
		Location eLoc = entity.getLocation();
		double x = loc.getX() - eLoc.getX();
		double y = loc.getY() - eLoc.getY();
		double z = loc.getZ() - eLoc.getZ();
//		target.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, true, false));
//		EntityLiving living = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(),loc.getY(), loc.getZ());
		PathEntity pathEntity = ((EntityInsentient)((CraftEntity)entity).getHandle()).getNavigation().a(loc.getX(), loc.getY(), loc.getZ(),0);

//		new CustomPathFinder((EntityInsentient)((CraftEntity)entity).getHandle(), loc, speed).c();

		EntityInsentient insentient = ((EntityInsentient)((CraftEntity)entity).getHandle());
		insentient.getNavigation().a(pathEntity, 1D);

//		insentient.goalSelector.a(new CustomPathFinder(insentient, loc, 1F));
//		new CustomPathFinder(insentient, loc, 1F).c();
//		insentient.targetSelector.a(0, new CustomPathFinder(insentient, loc, 1000f));
		insentient.targetSelector.a(PathfinderGoal.Type.MOVE);
		Logs.sendDebugMessage("mob moved from " + eLoc.getX() + " " + eLoc.getY() + " " + eLoc.getZ());
		Logs.sendDebugMessage("mob moved to " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
		Logs.sendDebugMessage("direction is " + x + " " + y + " " + z);
        movingTo = loc;
	}

	public void moveNext()
	{
		if (wayPoints.size() >= currentWayPoint)
		{
			if (movingTo == null || entity.getLocation().equals(movingTo))
			{
                move(wayPoints.get(currentWayPoint - 1));
                currentWayPoint++;
            }
		}
		else {
            move(endLocation);
        }
	}

	public void addWayPoint(Location loc)
    {
        wayPoints.add(loc);
    }

    /**
     * Удаляет ближайшую точку на расстоянии <b>range</b> блоков от локации <b>loc</b>
     */
    public Location removeWayPoint(Location loc, int range)
    {
        if (wayPoints.size() > 0)
        {
            Location point0 = wayPoints.get(0);
            double dx0 = point0.getX() - loc.getX();
            double dy0 = point0.getY() - loc.getY();
            double dz0 = point0.getZ() - loc.getZ();
            double minL = Math.sqrt(dx0 * dx0 + dy0 * dy0 + dz0 * dz0);
            int minI = -1;
            for (int i = 0; i < wayPoints.size(); i++)
            {
                double dx = wayPoints.get(i).getX() - loc.getX();
                double dy = wayPoints.get(i).getY() - loc.getY();
                double dz = wayPoints.get(i).getZ() - loc.getZ();
                double l = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (l <= range && minL >= l)
                {
                    minL = l;
                    minI = i;
                }
            }
            if (minI >= 0)
                return wayPoints.remove(minI);
        }
        return null;
    }

    /**
     * Удаляет все точки на расстоянии <b>range</b> от точки <b>loc</b>
     * @return возвращает список удалённых точке
     */
    public List<Location> removeAllWayPointsInRange(Location loc, int range)
    {
        List<Location> removed = Lists.newArrayList();
        int i = 0;
        while (i < wayPoints.size())
        {
            double dx = wayPoints.get(i).getX() - loc.getX();
            double dy = wayPoints.get(i).getY() - loc.getY();
            double dz = wayPoints.get(i).getZ() - loc.getZ();
            double l = Math.sqrt(dx * dx + dy * dy + dz * dz);
            if (l <= range)
            {
                removed.add(wayPoints.remove(i));
                wayPoints.remove(i);
                continue;
            }
            i++;
        }
        return removed;
    }

	public void update(){
		// TODO: update troops
	}

	public boolean isKilled() {
		return isKilled;
	}
//
//	public void setKilled(boolean isKilled) {
//		this.isKilled = isKilled;
//	}


    public Location getMovingTo() {
        return movingTo;
    }

    public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public double getDamage() 
	{
		return damage;
	}
	
	public void setDamage(double damage) 
	{
		this.damage = damage;
	}
	
	public double getSpeed() 
	{
		return speed;
	}
	
	public void setSpeed(double speed) 
	{
		this.speed = speed;
	}
	
	public double getCost() 
	{
		return cost;
	}
	
	public void setCost(double cost) 
	{
		this.cost = cost;
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public void setHealth(double troopHealth)
	{
		this.health = troopHealth;
	}
	
	public double getKilled() 
	{
		return killed;
	}
	
	public void setKilled(double killed) 
	{
		this.killed = killed;
	}

	public EntityType getMobType() 
	{
		return mobType;
	}

	public void setMobType(EntityType mobType) 
	{
		this.mobType = mobType;
	}

	public Entity getEntityTemplate()
	{
		return entityTemplate;
	}

	public void setEntityTemplate(Entity entityTemplate)
	{
		this.entityTemplate = entityTemplate;
	}
}
