package ru.sgk.thetowers.game;

import ru.sgk.thetowers.utils.GameScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class GameProcess {
	
	private ScheduledExecutorService scheduler;
	private int threadId;
	private int counter;
	private int seconds;
	public GameProcess()
	{
		scheduler = Executors.newSingleThreadScheduledExecutor();
	}
	
	public void start()
	{
		threadId = GameScheduler.scheduleTask(this::repeat, 0);
		counter = 0;
		seconds = 0;
	}
	
	public void stop()
	{
		GameScheduler.cancel(threadId);
	}
	
	/**
	 * repeating one times per second
	 */
	private void repeat()
	{
		if (counter % 1000 == 0)
		{
			seconds++;
			counter = 0;
		}
	}
}
