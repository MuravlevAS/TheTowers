package ru.sgk.thetowers.game.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PlayerData 
{
	private double castleHealth;
	private Player player;
	private static List<PlayerData> onlinePlayers = new ArrayList<>();
	

	
	public PlayerData(Player player) {
		this.player = player;
	}

	/**
	 * @return the health
	 */
	public double getCastleHealth() 
	{
		return castleHealth;
	}

	/**
	 * @param health the health to set
	 */
	public void setCastleHealth(double health) 
	{
		this.castleHealth = health;
	}

	/**
	 * @return the onlinePlayers
	 */
	public static List<PlayerData> getOnlinePlayers() {
		return onlinePlayers;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
}
