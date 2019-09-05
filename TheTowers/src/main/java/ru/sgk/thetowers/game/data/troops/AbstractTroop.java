package ru.sgk.thetowers.game.data.troops;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

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

	public AbstractTroop() {
		
	}

	public void spawn(Location loc) throws NullPointerException {
		entity = (LivingEntity) loc.getWorld().spawnEntity(loc, mobType);
		entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
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
