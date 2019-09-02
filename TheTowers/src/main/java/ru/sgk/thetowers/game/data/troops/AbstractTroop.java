package ru.sgk.thetowers.game.data.troops;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

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
	private EntityType mobType;
	private Location spawnLocation;
	private Location endLocation;
	private List<Location> wayPoints;
	private Entity entityTemplate;
	
	public AbstractTroop()
	{
		
	}

	public void spawn(Location loc) throws NullPointerException
	{
		loc.getWorld().spawnEntity(loc, mobType);
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
