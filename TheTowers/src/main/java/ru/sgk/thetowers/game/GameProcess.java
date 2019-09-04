package ru.sgk.thetowers.game;

import ru.sgk.thetowers.game.data.arenas.GameArena;
import ru.sgk.thetowers.game.data.arenas.GameArenas;
import ru.sgk.thetowers.utils.GameScheduler;

public class GameProcess {

	private int threadId;
	private long counter;
	private long globalCounter;
	private int seconds;
	private int minutes;
	private long tmpTimer;
	private GameArena arena;
	private boolean started;
	private int startedTime;
	public GameProcess(String arena)
	{
		this.arena = GameArenas.getArena(arena);
		this.startedTime = 10_000;
	}

	public void start()
	{
		tmpTimer = 0;
		counter = 0;
		seconds = 0;
		started = false;
		threadId = GameScheduler.scheduleTask(this::repeat, 0);
	}

	public void stop()
	{
		GameScheduler.cancel(threadId);
	}

	/**
	 * repeated once per second
	 */
	private void repeat()
	{
		if (counter % 1000 == 0)
		{
			seconds++;
			counter = 0;
		}
		if (!started)
		{
			if (globalCounter >= startedTime) started = true;
		}
		else
		{
			// TODO: game process;

		}
		globalCounter++;
		counter++;
	}

	public long getGlobalCounter(){
		return this.globalCounter;
	}

}
