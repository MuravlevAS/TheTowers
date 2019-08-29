package ru.sgk.thetowers.game.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.sgk.thetowers.game.data.PlayerData;

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
	
}
