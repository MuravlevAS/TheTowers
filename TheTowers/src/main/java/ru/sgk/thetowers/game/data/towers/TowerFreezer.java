package ru.sgk.thetowers.game.data.towers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

public class TowerFreezer extends AbstractTower
{
	/**
	 * Время заморозки <br>
	 * <b>Значение в мс
	 */
	private long freezeTime;

	/**
	 * Freezer tower fields: <br>
	 * <b>
	 * - time<br>
	 * - cost <br>
	 * - radius <br>
	 * - recharge-time 
	 */
	public TowerFreezer()
	{
		configSection = Configurations.getSettings().getConfigurationSection("towers.freezer");
		setTitle		(getString("title"));
		setDescription	(getStringList("desc"));
		setLevel(TowerLevel.A);
		List<String> levels = new ArrayList<>(configSection.getConfigurationSection("levels").getKeys(false));
		setMaxLevel		(TowerLevel.valueOf(levels.get(levels.size()-1)));
		setDamage		(0);
		freezeTime = 	(configSection.getLong("levels.A.time"));
		setCost			(configSection.getDouble("levels.A.cost"));
		setRadius		(configSection.getDouble("levels.A.radius"));
		setRechargeTime (configSection.getLong("levels.A.recharge-time"));
	}

	@Override
	public void upgrade() 
	{
		super.upgrade();
		Logs.sendDebugMessage("upgrade TowerFreezer to level "+ getLevel());
	}

	@Override
	public void setSettings() 
	{
		ConfigurationSection sect = configSection.getConfigurationSection("levels."  + getLevel().toString());
		setDamage(0);
		freezeTime = 	(sect.getLong("time"));
		setCost			(sect.getDouble("cost"));
		setRadius		(sect.getDouble("radius"));
		setRechargeTime (sect.getLong  ("recharge-time"));
	}
	
	public long getFreezeTime() 
	{
		return freezeTime;
	}

	public void setFreezeTime(long freezeTime) 
	{
		this.freezeTime = freezeTime;
	}
	
	
}
