package ru.sgk.thetowers.game.data.towers;

import com.google.common.collect.Iterables;
import org.bukkit.configuration.ConfigurationSection;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

import java.util.ArrayList;

public class TowerSkeleton extends AbstractTower
{
	/**
	 * Skeleton tower fields: <br>
	 * <b>
	 * - damage<br>
	 * - cost <br>
	 * - radius <br>
	 * - recharge-time 
	 */
	public TowerSkeleton()
	{
		configSection = Configurations.getSettings().getConfigurationSection("towers.hunter");
		setTitle		(configSection.getString("title"));
		setDescription	(configSection.getStringList("desc"));
		setLevel(TowerLevel.A);
		setMaxLevel		(TowerLevel.valueOf(Iterables.getLast(new ArrayList<>(configSection.getConfigurationSection("levels").getKeys(false)))));
		setDamage		(configSection.getDouble("levels.A.damage"));
		setCost			(configSection.getDouble("levels.A.cost"));
		setRadius		(configSection.getDouble("levels.A.radius"));
		setRechargeTime (configSection.getLong  ("levels.A.recharge-time"));
	}
	
	@Override
	public void upgrade() {
		super.upgrade();
		Logs.sendDebugMessage("upgrade TowerSkeleton to level "+ getLevel());
	}

	@Override
	public void setSettings() {
		
		ConfigurationSection sect = configSection.getConfigurationSection("levels."  + getLevel().toString());
		setDamage		(sect.getDouble("damage"));
		setCost			(sect.getDouble("cost"));
		setRadius		(sect.getDouble("radius"));
		setRechargeTime (sect.getLong  ("recharge-time"));
		
	}
}
