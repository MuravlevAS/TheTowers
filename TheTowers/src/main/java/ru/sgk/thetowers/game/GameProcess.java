package ru.sgk.thetowers.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ru.sgk.thetowers.utils.GameScheduler;

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
		threadId = GameScheduler.ScheduleTask(()->repeat(), 0);
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
