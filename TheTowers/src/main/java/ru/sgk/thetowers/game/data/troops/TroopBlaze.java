package ru.sgk.thetowers.game.data.troops;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import ru.sgk.thetowers.data.Configurations;

public class TroopBlaze extends AbstractTroop 
{
	public TroopBlaze() 
	{
		super();
		configSection = Configurations.getSettings().getConfigurationSection("troops.zombie");
		setTitle	(configSection.getString("title"));
		setCost		(configSection.getDouble("cost"));
		setDamage	(configSection.getDouble("damage"));
		setHealth	(configSection.getDouble("health"));
		setKilled	(configSection.getDouble("killed"));
		setSpeed	(configSection.getDouble("speed"));
		setMobType	(EntityType.BLAZE);
	}
}
