package ru.sgk.thetowers.game.data.towers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

public class TowerBeacon extends AbstractTower 
{

	/**
	 * Supporter tower fields: <br>
	 * <b>
	 * - time<br>
	 * - cost <br>
	 */
	public TowerBeacon()
	{
		configSection = Configurations.getSettings().getConfigurationSection("towers.fire");
		setTitle		(getString("title"));
		setDescription	(getStringList("desc"));
		setLevel(TowerLevel.A);
		List<String> levels = new ArrayList<>(configSection.getConfigurationSection("levels").getKeys(false));
		setMaxLevel		(TowerLevel.valueOf(levels.get(levels.size()-1)));
		setDamage		(0);
		setRechargeTime (configSection.getLong  ("levels.A.time"));
		setCost			(configSection.getDouble("levels.A.cost"));
	}
	
	@Override
	public void upgrade() {
		super.upgrade();
		Logs.sendDebugMessage("upgrade TowerБекон to level "+ getLevel());
	}
	
	@Override
	public void setSettings() 
	{
		ConfigurationSection sect = configSection.getConfigurationSection("levels."+getLevel());

		setRechargeTime (sect.getLong  ("time"));
		setCost			(sect.getDouble("cost"));
	}
}
