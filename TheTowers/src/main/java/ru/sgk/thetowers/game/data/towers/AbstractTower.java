package ru.sgk.thetowers.game.data.towers;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTower
{
	/** Имя башни */
	private String title;
	/** Описание башни*/
	private List<String> description;
	/** Текущий уровень башни*/
	private TowerLevel currentLevel;
	/** Позиция башни*/
	private Location location;
	/** Стоимость покупки башки*/
	private double cost;
	/** Время перезарядки. Значение в мс */
	private long rechargeTime;
	
	private TowerLevel maxLevel = TowerLevel.E;
	/** Радиус действия башни*/
	private double radius;
	/** Урон, который башня наносит мобам*/
	private double damage;
	/** Секция башни в конфиге */
	protected ConfigurationSection configSection;
	private EntityType mobType;


//	public AbstractTower(ConfigurationSection configSection, String title, List<String> description, Location location, double cost, long rechargeTime, TowerLevel maxLevel, double radius, double damage, EntityType mobType) {
//		this.configSection = configSection;
//		this.title = title;
//		this.description = description;
//		this.location = location;
//		this.cost = cost;
//		this.rechargeTime = rechargeTime;
//		this.maxLevel = maxLevel;
//		this.radius = radius;
//		this.damage = damage;
//		this.mobType = mobType;
//	}

	/**
	 * Устанавливает переменные башни в соответствии с её уровнем.
	 */
	public abstract void setSettings();

	public abstract void update();

	/**
	 * Повышает уровень башни.
	 * Если Уровень башни достиг максимума, он не изменяется
	 */
	public void upgrade()
	{
		if (currentLevel == maxLevel) return;
		if (currentLevel == TowerLevel.A) currentLevel = TowerLevel.B;
		else if (currentLevel == TowerLevel.B) currentLevel = TowerLevel.C;
		else if (currentLevel == TowerLevel.C) currentLevel = TowerLevel.D;
		else if (currentLevel == TowerLevel.D) currentLevel = TowerLevel.E;
		setSettings();
	}
	/**
	 * Понижение уровня башни на один уровень.
	 * Если уровень башни достиг минимального, он не изменяется
	 */
	public void downgrade()
	{
		if (currentLevel == TowerLevel.A) return;
		if (currentLevel == TowerLevel.E) currentLevel = TowerLevel.D;
		else if (currentLevel == TowerLevel.D) currentLevel = TowerLevel.C;
		else if (currentLevel == TowerLevel.C) currentLevel = TowerLevel.B;
		else if (currentLevel == TowerLevel.B) currentLevel = TowerLevel.A;
		setSettings();
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title) 
	{
		this.title = title;
	}

	public List<String> getDescription() 
	{
		return description;
	}

	public void setDescription(List<String> description)
	{
		this.description = description;
	}

	public TowerLevel getLevel() 
	{
		return currentLevel;
	}

	public void setLevel(TowerLevel currentLevel) 
	{
		this.currentLevel = currentLevel;
	}

	public double getCost() 
	{
		return cost;
	}

	public void setCost(double cost) 
	{
		this.cost = cost;
	}

	public long getRechargeTime() 
	{
		return rechargeTime;
	}

	public void setRechargeTime(long rechargeTime) 
	{
		this.rechargeTime = rechargeTime;
	}

	public TowerLevel getMaxLevel() 
	{
		return maxLevel;
	}

	public void setMaxLevel(TowerLevel maxLevel) 
	{
		this.maxLevel = maxLevel;
	}

	public double getRadius() 
	{
		return radius;
	}

	public void setRadius(double radius) 
	{
		this.radius = radius;
	}

	public double getDamage() 
	{
		return damage;
	}

	public void setDamage(double damage) 
	{
		this.damage = damage;
	}
	
	protected final String getString(String path)
	{
		return configSection.getString(path).replaceAll("&", "§");
	}
	
	
	protected final List<String> getStringList(String path)
	{
		List<String> oldList = configSection.getStringList(path);
		List<String> newList = new ArrayList<String>();
		for	(String s : oldList)
		{
			newList.add(s.replaceAll("&", "§"));
		}
		return newList;
	}


	public EntityType getMobType()
	{
		return mobType;
	}

	public void setMobType(EntityType mobType) {
		this.mobType = mobType;
	}
}
