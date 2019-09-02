package ru.sgk.thetowers.game.data.towers;

import com.google.common.collect.Iterables;
import org.bukkit.configuration.ConfigurationSection;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

import java.util.ArrayList;

public class TowerFire extends AbstractTower
{
	/**
	 * burning time <br> 
	 * in milliSeconds
	 */
	private long burnTime;
	/**
	 * Skeleton tower fields: <br>
	 * <b>
	 * - time<br>
	 * - cost <br>
	 * - radius <br>
	 * - recharge-time 
	 */
	public TowerFire()
	{
		configSection = Configurations.getSettings().getConfigurationSection("towers.fire");
		setTitle		(getString("title"));
		setDescription	(getStringList("desc"));
		setLevel(TowerLevel.A);
		setMaxLevel		(TowerLevel.valueOf(Iterables.getLast(new ArrayList<>(configSection.getConfigurationSection("levels").getKeys(false)))));
		setDamage		(0);
		burnTime = 		(configSection.getLong("levels.A.time"));
		setCost			(configSection.getDouble("levels.A.cost"));
		setRadius		(configSection.getDouble("levels.A.radius"));
		setRechargeTime (configSection.getLong("levels.A.recharge-time"));
	}
	
	@Override
	public void upgrade() {
		super.upgrade();
		Logs.sendDebugMessage("upgrade TowerFire to level "+ getLevel());
	}
	
	@Override
	public void setSettings() 
	{
		ConfigurationSection sect = configSection.getConfigurationSection("levels."+getLevel());

		burnTime = 		(sect.getLong  ("time"));
		setCost			(sect.getDouble("cost"));
		setRadius		(sect.getDouble("radius"));
		setRechargeTime (sect.getLong  ("recharge-time"));
	}

	public long getBurnTime() 
	{
		return burnTime;
	}

	public void setBurnTime(long burnTime) 
	{
		this.burnTime = burnTime;
	}
	
	
}
