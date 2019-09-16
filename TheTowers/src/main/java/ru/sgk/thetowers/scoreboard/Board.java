package ru.sgk.thetowers.scoreboard;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.PlayerData;

public class Board implements Runnable
{
	private static Map<String, Integer> linesLobby = new ConcurrentHashMap<>();
	private static Map<String, Integer> linesInGame = new ConcurrentHashMap<>();
	private static Map<String, Integer> linesWaiting = new ConcurrentHashMap<>();
	
	private static String title;
	
	public static void initScoreboard()
	{
		getConfigList();
		title = Configurations.getConfig().getString("scoreboard.title").replaceAll("&", "§");
//		board = new SimpleScoreboard(getTitle());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(MainTowers.getInstance(), new Board(), 0, 1);
	}

	private static void getConfigList()
	{
		List<String> list = Configurations.getConfig().getStringList("scoreboard.lobby.lines");
		linesLobby = new ConcurrentHashMap<>();
		int i = 15;
		for (String s : list)
		{
			if (i == 0) break;
			linesLobby.put(s, i--);
		}
		
		list = Configurations.getConfig().getStringList("scoreboard.in-game.lines");
		linesInGame = new ConcurrentHashMap<>();
		i = 15;
		for (String s : list)
		{
			if (i == 0) break;
			linesInGame.put(s, i--);
		}
		
		list = Configurations.getConfig().getStringList("scoreboard.waiting.lines");
		linesWaiting = new ConcurrentHashMap<>();
		i = 15;
		for (String s : list)
		{
			if (i == 0) break;
			linesWaiting.put(s, i--);
		}
		
	}
	public static String getTitle()
	{
		return title;
	}
	
	private static int i = 0;
	@Override
	public void run() 
	{
		PlayerData.getDataList().stream().forEach((PlayerData p) ->
		//for (PlayerData p : PlayerData.getOnlinePlayers())
		{
			linesLobby.forEach((s, i) -> 
			{
				p.getBoard().add(s.replaceAll("%castle_health%", p.getCastleHealth()+""), i);
			});
			p.getBoard().update();
			p.getBoard().send(p.getPlayer());
		});
		if (Board.i++ > 100) Board.i = 0;
	}
}