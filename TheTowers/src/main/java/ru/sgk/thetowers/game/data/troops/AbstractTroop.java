package ru.sgk.thetowers.game.data.troops;

import net.minecraft.server.v1_14_R1.Vec3D;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeWrapper;
import ru.sgk.thetowers.utils.Logs;

import java.util.List;

public abstract class AbstractTroop 
{
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

	public AbstractTroop() {
		currentWayPoint = 0;
	}

	public void spawn(Location loc) throws NullPointerException {
		entity = (LivingEntity) loc.getWorld().spawnEntity(loc, mobType);
		entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
		entity.setCollidable(false);
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
		((CraftEntity)entity).getHandle().a((float)Math.sqrt(x*x + y*y + z*z), new Vec3D(x,y,z));
		Logs.sendDebugMessage("mob moved from " + eLoc.getX() + " " + eLoc.getY() + " " + eLoc.getZ());
		Logs.sendDebugMessage("mob moved to " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
		Logs.sendDebugMessage("direction is " + x + " " + y + " " + z);
	}

	public void moveNext()
	{
		if (wayPoints.size() >= currentWayPoint)
		{
			Player p = null;
		}
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
	
	public void setHealth(double health) 
	{
		this.health = health;
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
