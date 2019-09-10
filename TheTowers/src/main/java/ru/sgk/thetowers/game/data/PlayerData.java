package ru.sgk.thetowers.game.data;

import org.bukkit.entity.Player;
import ru.sgk.thetowers.scoreboard.Board;
import ru.sgk.thetowers.scoreboard.SimpleScoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerData 
{
	private double castleHealth;
	private Player player;
	private static List<PlayerData> dataList = Collections.synchronizedList( new ArrayList<>());
	private SimpleScoreboard customBoard = null;
	private boolean wasFlying;
	
	public PlayerData(Player player) {
		this.player = player;
		wasFlying = player.getAllowFlight();
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
	public static List<PlayerData> getDataList() {
		return dataList;
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
		for (PlayerData data : dataList)
		{
			if (data.getPlayer().equals(player))
			{
				return;
			}
		}
		dataList.add(new PlayerData(player));
	}

	public static void remove(Player player)
	{
		dataList.removeIf((data) -> data.getPlayer().equals(player));
	}

	public boolean wasFlying()
	{
		return wasFlying;
	}

	public void setWasFlying(boolean wasFlying) {
		this.wasFlying = wasFlying;
	}
}
