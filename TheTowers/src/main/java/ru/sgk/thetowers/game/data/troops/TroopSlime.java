package ru.sgk.thetowers.game.data.troops;

import org.bukkit.entity.EntityType;

import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.teams.GameTeam;

public class TroopSlime extends AbstractTroop 
{
	public TroopSlime(GameTeam parentTeam)
	{
		super(parentTeam);
		configSection = Configurations.getSettings().getConfigurationSection("troops.slime");
		setTitle	(configSection.getString("title"));
		setCost		(configSection.getDouble("cost"));
		setDamage	(configSection.getDouble("damage"));
		setHealth	(configSection.getDouble("health"));
		setKilled	(configSection.getDouble("killed"));
		setSpeed	(configSection.getDouble("speed"));
		setMobType	(EntityType.SLIME);
		setInvisible(true);
	}
}
