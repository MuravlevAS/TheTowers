package ru.sgk.thetowers.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameScheduler 
{
	private static ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
	private static Map<Integer, ScheduledFuture<?>> tasks = new HashMap<>();
	
	/**
	 * 
	 * @param task - task to carrying out
	 * @param delay - delay in milliseconds
	 * @return - id of task 
	 */
	public static int scheduleTask(Runnable task, int delay)
	{
		int id = tasks.size();
		tasks.put(id, service.scheduleAtFixedRate(task, delay, 1, TimeUnit.MILLISECONDS));
		
		return id;
//		tasks.put(id, service.)
	}
	public static void cancel(int id)
	{
		
		if (tasks.containsKey(id))
		{
			if (!tasks.get(id).isCancelled())
				tasks.get(id).cancel(false);

		}
	}
}
