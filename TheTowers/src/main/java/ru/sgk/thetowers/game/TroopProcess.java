package ru.sgk.thetowers.game;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;

import java.util.Collections;
import java.util.List;

public class TroopProcess
{
    private static List<AbstractTroop> troops = Collections.synchronizedList(Lists.newArrayList());
    private static List<AbstractTroop> remove = Collections.synchronizedList(Lists.newArrayList());
    private static int taskId = -1;

    synchronized public static void add(AbstractTroop troop, GameTeam team)
    {
        for (AbstractTroop t : troops)
        {
            if (t.getEntity().equals(troop.getEntity()) || t.equals(troop))
                return;
        }
        troops.add(troop);
        troop.spawn();
        if (troops.size() == 1 && taskId == -1)
            Bukkit.getScheduler().scheduleSyncRepeatingTask(MainTowers.getInstance(), TroopProcess::update, 0, 1);
    }
    public static void update()
    {
        for (AbstractTroop troop : troops)
        {
            troop.update();
            if (troop.isKilled())
            {
                // TODO: Отправка тиме статуса о том, что моб умер.
                remove(troop);
            }
        }
        removeAll();
    }
    public static void remove(AbstractTroop troop)
    {
        remove.add(troop);
    }
    private static void removeAll()
    {
        try
        {
            troops.removeAll(remove);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        remove.clear();
        if (troops.size() == 0 && taskId != -1)
        {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }
}
