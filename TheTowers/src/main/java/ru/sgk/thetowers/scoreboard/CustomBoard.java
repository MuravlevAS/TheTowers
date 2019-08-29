package ru.sgk.thetowers.scoreboard;

import org.bukkit.scoreboard.Scoreboard;

public class CustomBoard {
	private Scoreboard scoreboard;
	
	
	
	/**
	 * @return the scoreboard
	 */
	public Scoreboard getScoreboard() 
	{
		return scoreboard;
	}

	/**
	 * @param scoreboard the scoreboard to set
	 */
	public void setScoreboard(Scoreboard scoreboard) 
	{
		this.scoreboard = scoreboard;
	}
}
