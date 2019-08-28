package ru.sgk.thetowers.game.data.towers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

abstract class AbstractTower 
{
	private String title;
	private List<String> description;
	private TowerLevel currentLevel;
	private double cost;
	/**
	 * Значение в мс
	 */
	private long rechargeTime;
	private TowerLevel maxLevel = TowerLevel.E;
	private double radius;
	private double damage;
	protected ConfigurationSection configSection;
	
	public abstract void setSettings();
	
	public void upgrade()
	{
		if (currentLevel == maxLevel) return;
		if (currentLevel == TowerLevel.A) currentLevel = TowerLevel.B;
		else if (currentLevel == TowerLevel.B) currentLevel = TowerLevel.C;
		else if (currentLevel == TowerLevel.C) currentLevel = TowerLevel.D;
		else if (currentLevel == TowerLevel.D) currentLevel = TowerLevel.E;
		setSettings();
	}
	
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
	
	
}
