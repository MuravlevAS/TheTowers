package ru.sgk.thetowers.scoreboard;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import com.google.gson.internal.LinkedTreeMap;

import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.PlayerData;

public class Board implements Runnable
{
//	private static ScoreboardManager sbManager = Bukkit.getScoreboardManager();
	
//	private static SimpleScoreboard board;
	
	static Map<String,Integer> lines = new ConcurrentHashMap<>();
	public static void newScoreboard()
	{
		getConfigList();
//		board = new SimpleScoreboard(getTitle());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainTowers.getInstance(), new Board(), 0, 1);
	}
	
	private static void getConfigList()
	{
		List<String> list = Configurations.getConfig().getStringList("scoreboard.lines");
		lines = Collections.synchronizedMap(new LinkedTreeMap<String, Integer>());
		int i = 15;
		for (String s : list)
		{
			if (i == 0) break;
			lines.put(s, i--);
		}
	}
	public static String getTitle()
	{
		return Configurations.getConfig().getString("scoreboard.title");
	}
	private static int i = 0;
	@Override
	public void run() 
	{
		PlayerData.getOnlinePlayers().stream().forEach((PlayerData p) ->
		//for (PlayerData p : PlayerData.getOnlinePlayers())
		{
			lines.forEach((s, i) -> 
			{
				p.getBoard().add(s.replaceAll("%castle_health%", p.getCastleHealth()+""), i);
			});
			p.getBoard().update();
			p.getBoard().send(p.getPlayer());
		});
		if (Board.i++ > 100) Board.i = 0;
	}
}