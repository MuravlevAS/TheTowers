package ru.sgk.thetowers.game.data.troops;

import org.bukkit.entity.EntityType;

import ru.sgk.thetowers.data.Configurations;

public class TroopSpider extends AbstractTroop
{
	public TroopSpider() 
	{
		super();
		configSection = Configurations.getSettings().getConfigurationSection("troops.spider");
		setTitle	(configSection.getString("title"));
		setCost		(configSection.getDouble("cost"));
		setDamage	(configSection.getDouble("damage"));
		setHealth	(configSection.getDouble("health"));
		setKilled	(configSection.getDouble("killed"));
		setSpeed	(configSection.getDouble("speed"));
		setMobType	(EntityType.SPIDER);
	}
}
