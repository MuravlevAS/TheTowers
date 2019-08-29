package ru.sgk.thetowers.game.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import ru.sgk.thetowers.scoreboard.Board;
import ru.sgk.thetowers.scoreboard.SimpleScoreboard;

public class PlayerData 
{
	private double castleHealth;
	private Player player;
	private static List<PlayerData> onlinePlayers = Collections.synchronizedList( new ArrayList<>());
	private SimpleScoreboard customBoard = null;

	
	public PlayerData(Player player) {
		this.player = player;
		registerBoard();
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

	/**
	 * @return the scoreboard
	 */
	public SimpleScoreboard getBoard() 
	{
		if (customBoard == null) registerBoard();
		return customBoard;
	}

	/**
	 * @param customBoard the scoreboard to set
	 */
	public void registerBoard() 
	{
		customBoard = new SimpleScoreboard(Board.getTitle());
	}
	
	public void sendScoreboard()
	{
		customBoard.send(player);	
	}
	public static void add(Player player)
	{
		for (PlayerData data : onlinePlayers)
		{
			if (data.getPlayer().equals(player))
			{
				return;
			}
		}
		onlinePlayers.add(new PlayerData(player));
	}
	public static void remove(Player player)
	{
		onlinePlayers.removeIf((data) -> data.getPlayer().equals(player));
	}
}
