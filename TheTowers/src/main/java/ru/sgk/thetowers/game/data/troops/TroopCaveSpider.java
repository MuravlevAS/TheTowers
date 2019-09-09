package ru.sgk.thetowers.game.data.troops;

import org.bukkit.entity.EntityType;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.teams.GameTeam;

public class TroopCaveSpider extends AbstractTroop 
{
	public TroopCaveSpider(GameTeam parentTeam)
	{
		super(parentTeam);
		configSection = Configurations.getSettings().getConfigurationSection("troops.cave-spider");
		setTitle	(configSection.getString("title"));
		setCost		(configSection.getDouble("cost"));
		setDamage	(configSection.getDouble("damage"));
		setHealth	(configSection.getDouble("health"));
		setKilled	(configSection.getDouble("killed"));
		setSpeed	(configSection.getDouble("speed"));
		setMobType	(EntityType.CAVE_SPIDER);
	}
}
