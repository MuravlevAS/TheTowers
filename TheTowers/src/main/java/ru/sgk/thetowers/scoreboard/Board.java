package ru.sgk.thetowers.scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.PlayerData;

public class Board implements Runnable
{
	private static ScoreboardManager sbManager;
	private static Scoreboard scoreboard = sbManager.getNewScoreboard();
	
	static List<String> lines = Collections.synchronizedList(new ArrayList<String>());
	
	public static void init()
	{
		getConfigList();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainTowers.getInstance(), new Board(), 0, 1);
	}
	
	private static void getConfigList()
	{
		List<String> list = Configurations.getConfig().getStringList("scoreboard.lines");
		lines = Collections.synchronizedList(new ArrayList<String>());
		for (int i = 0; i < list.size() && i < 15; i++)
		{
			lines.add(list.get(i));
		}
	}
	private static String getTitle()
	{
		return Configurations.getConfig().getString("scoreboard.title");
	}	

	@Override
	public void run() 
	{
		scoreboard = sbManager.getNewScoreboard();
		PlayerData.getOnlinePlayers().stream().forEach((PlayerData p) ->
		{
			
			Objective obj = null;
			if (scoreboard.getObjective("TheTowers") != null)
				obj = scoreboard.getObjective("TheTowers");
			else
				obj = scoreboard.registerNewObjective("TheTowers", "dummy", getTitle());
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			byte i = 15;
			for (String s : lines)
			{
				Score score = obj.getScore(s.replaceAll("%castle_health%", p.getCastleHealth()+""));
				score.setScore(i--);
				
			}
			
			p.getPlayer().setScoreboard(scoreboard);
		});
	}
}
