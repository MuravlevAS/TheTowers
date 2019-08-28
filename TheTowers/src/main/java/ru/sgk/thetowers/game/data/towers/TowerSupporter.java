package ru.sgk.thetowers.game.data.towers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

public class TowerSupporter extends AbstractTower
{
	
	private int gain;
	
	/**
	 * Supporter tower fields: <br>
	 * <b>
	 * - gain<br>
	 * - cost <br>
	 * - radius <br>
	 */
	public TowerSupporter()
	{
		configSection = Configurations.getSettings().getConfigurationSection("towers.fire");
		setTitle		(getString("title"));
		setDescription	(getStringList("desc"));
		setLevel(TowerLevel.A);
		List<String> levels = new ArrayList<>(configSection.getConfigurationSection("levels").getKeys(false));
		setMaxLevel		(TowerLevel.valueOf(levels.get(levels.size()-1)));
		setDamage		(0);
		setGain			(configSection.getInt	("levels.A.gain"));
		setCost			(configSection.getDouble("levels.A.cost"));
		setRadius		(configSection.getDouble("levels.A.radius"));
		setRechargeTime (0);
	}
	
	@Override
	public void upgrade() {
		super.upgrade();
		Logs.sendDebugMessage("upgrade TowerSupporter to level "+ getLevel());
	}
	
	@Override
	public void setSettings() 
	{
		ConfigurationSection sect = configSection.getConfigurationSection("levels."+getLevel());

		setGain			(sect.getInt   ("gain"));
		setCost			(sect.getDouble("cost"));
		setRadius		(sect.getDouble("radius"));
	}

	public int getGain()
	{
		return gain;
	}

	public void setGain(int gain) 
	{
		this.gain = gain;
	}
	
	
}
