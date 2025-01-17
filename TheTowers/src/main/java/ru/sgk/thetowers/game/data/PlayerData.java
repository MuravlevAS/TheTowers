package ru.sgk.thetowers.game.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import ru.sgk.thetowers.game.GameProcess;
import ru.sgk.thetowers.scoreboard.Board;
import ru.sgk.thetowers.scoreboard.SimpleScoreboard;

public class PlayerData 
{
	private double castleHealth;
	private Player player;
	private static List<PlayerData> dataList = Collections.synchronizedList( new ArrayList<>());
	private SimpleScoreboard customBoard = null;
	private boolean wasFlying;
	private GameProcess game;
	private double money;
	
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
	/**
	 * Регистрация скорборда
	 */
	public void registerBoard() 
	{
		customBoard = new SimpleScoreboard(Board.getTitle());
	}
	/**
	 * Отправка скорборда игроку
	 */
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
	
	/**
	 * При запуске игры, у игрока включается режим полёта. wasFlying нужен для того, что бы понять, мог ли игрок летать до этого
	 * (например, игрок мог быть в креативе) и после окончания игры выдать или забрать режим полёта.  
	 * @return true, если игрок перед заходом в игру мог летать, в противном случае false 
	 */
	public boolean wasFlying()
	{
		return wasFlying;
	}
	
	public void setWasFlying(boolean wasFlying) {
		this.wasFlying = wasFlying;
	}

	public GameProcess getGame() 
	{
		return game;
	}

	public void setGame(GameProcess game) 
	{
		this.game = game;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	
}
