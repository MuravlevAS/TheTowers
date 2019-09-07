package ru.sgk.thetowers.game.events;

import com.google.common.collect.Lists;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import ru.sgk.thetowers.game.GameArenas;
import ru.sgk.thetowers.game.data.teams.GameTeamColor;
import ru.sgk.thetowers.game.data.PlayerData;

import java.util.List;

public class MainEvents implements Listener
{
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData.add(e.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		PlayerData.remove(e.getPlayer());
	}

	@EventHandler
	public void onTabEvent(TabCompleteEvent e)
	{
		List<String> buf = Lists.newArrayList(e.getBuffer().split(" "));
		List<String> completions = Lists.newArrayList();
		if (buf.get(0).equalsIgnoreCase("/towers"))
		{
			if (buf.size() == 2 && e.getBuffer().endsWith(" ") || buf.size() == 3)
			{
				if (buf.get(1).equalsIgnoreCase("arena"))
				{
					completions = GameArenas.toStringList();
				}
			}
			if ((buf.size() == 4 && e.getBuffer().endsWith(" ")) || buf.size() == 5)
			{
				if (buf.get(1).equalsIgnoreCase("arena") &&
						buf.get(3).equalsIgnoreCase("createTeam"))
				{
					completions = Lists.newArrayList(GameTeamColor.stringValues());

				}
			}
		}
		// TODO: доделать таб комплиты.
		e.setCompletions(completions);
	}
}
