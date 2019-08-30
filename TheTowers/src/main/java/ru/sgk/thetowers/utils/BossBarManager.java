package ru.sgk.thetowers.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarManager {
	
	public static Map<String, BossBar> barList = Collections.synchronizedMap(new HashMap<>());
	
	public static BossBar createBossbar(String barName, String title, BarColor color, BarStyle style, double progressInPercents)
	{
		BossBar bar = Bukkit.createBossBar(title.replace('&', '§'), color, style);
		if (!barList.containsKey(barName))
		{
			barList.put(barName, bar);
			Logs.sendDebugMessage("Boss bar " + barName + " has created!");
		}
		else bar = barList.get(barName);
		return bar;
	}
	
	public static BossBar getBar(String barName)
	{
		Logs.sendDebugMessage("Getting BossBar: "+barName);
		BossBar bar = barList.get(barName);
		if (bar == null) Logs.sendDebugMessage("§c"+barName + " is null!");
		return  bar;
	}

	public static void addPlayers(String barName, Player... players)
	{
		
		if (barList.containsKey(barName))
			for (Player p : players)
			{
				barList.get(barName).addPlayer(p);
				Logs.sendDebugMessage("Player " + p.getName()+ " has added to boss bar " + barName);
			}
		else
			Logs.sendDebugMessage("Boss bar " + barName + " is not registered!");
	}
	
	public static void removePlayers(String barName, Player... players)
	{
		if (barList.containsKey(barName))
			for (Player p : players)
			{
				if (barList.get(barName).getPlayers().contains(p))
				{
					barList.get(barName).removePlayer(p);
					Logs.sendDebugMessage("Player " + p.getName()+ " has removed from boss bar " + barName);
				}
			}
		else
			Logs.sendDebugMessage("Boss bar " + barName + " is not registered!");
	}
}
