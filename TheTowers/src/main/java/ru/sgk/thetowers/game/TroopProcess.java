package ru.sgk.thetowers.game;

import java.util.Collections;
import java.util.List;
import java.util.Queue;

import org.bukkit.Bukkit;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;

public class TroopProcess
{
	/** Список добвыленных мобов */
    private static List<AbstractTroop> troops = Collections.synchronizedList(Lists.newArrayList());
	/** Список мобов для их удаления из списка выше*/
    private static List<AbstractTroop> remove = Collections.synchronizedList(Lists.newArrayList());
    private static Queue<AbstractTroop> troopsQueue = Queues.newSynchronousQueue();  
    /** Айди потока, который обрабатывает мобов */
    private static int taskId = -1;
    /** Частота, с которой будут спавниться мобы */
    private static int frequency = 5;
    /** Таймер для подсчёта прошедших тиков */
    private static int timer = 0;
    /**
     * Добавляет моба в очередь на спавн
     * @param troop - моб, которого нужно добавить в очередь
     */
    synchronized public static void add(AbstractTroop troop)
    {
    	if (troop == null)
    		throw new NullPointerException("cannot add troop which is null element");
    	// Если такой моб уже находится в списке, либо в очереди на спавн, то пропускаем добавление его в очередь.
        for (AbstractTroop t : troops)
        {
            if (t.getEntity().equals(troop.getEntity()) || t.equals(troop))
                return;
        }
        for (AbstractTroop t : troopsQueue) 
        {
        	if (t.equals(troop) || t.getEntity().equals(troop.getEntity()))
        		return;
        }
        // Добавляем моба в очередь на спавн
        troopsQueue.add(troop);
        // Если обработчик не запущен - запускаем его  
        if (taskId == -1)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(MainTowers.getInstance(), TroopProcess::update, 0, 1);
    }
    
    /**
     * Обновление мобов
     */
    public static void update()
    {
    	// Если таймер равен частоте, то спавним моба из очереди
    	if (timer == frequency)
    	{
    		
    		if (troopsQueue.peek() != null)
    		{
    			troopsQueue.peek().spawn();
    			troops.add(troopsQueue.poll());
    		}
    		
    		timer = 0;
    	}
        for (AbstractTroop troop : troops)
        {
        	// Обновляем моба
            troop.update();
            // Если после обновления с ним что-то случилось, то добавляем его в список для удаления
            if (troop.isKilled() || troop.isDespawned())
                remove(troop);
        }
        // Подчищаем лишнее
        removeAll();
        // Обновляем таймер
        timer++;
    }
    /**
     * Добавляет моба в remove список  
     * @param troop - моб, которого нужно будет удалить.
     */
    synchronized public static void remove(AbstractTroop troop)
    {
        remove.add(troop);
    }
    /** Удаляет мобов из списка remove */
    synchronized private static void removeAll()
    {
        try
        {
        	// Удаляем из списка мобов тех, которые присутствуют в списке remove
            troops.removeAll(remove);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        remove.clear();
        if (troops.size() == 0 && troopsQueue.size() == 0 && taskId != -1)
        {
        	// Останавливаем поток.
            Bukkit.getScheduler().cancelTask(taskId);

        	// Обнуляем всё.
            taskId = -1;
            timer = 0;

            troopsQueue.clear();
            troops.clear();
        }
    }
}
