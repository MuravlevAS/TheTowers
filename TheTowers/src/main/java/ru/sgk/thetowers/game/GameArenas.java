package ru.sgk.thetowers.game;

import com.google.common.collect.Lists;

import java.util.List;


public class GameArenas
{
    private static List<GameArena> arenas = Lists.newArrayList();

    public static List<GameArena> getArenas()
    {
        return arenas;
    }

    public static boolean addArena(GameArena arena)
    {
        if (!arenas.contains(arena))
        {
            arenas.add(arena);
            return true;
        }
        return false;
    }

    public static GameArena getArena(String name)
    {
        for (GameArena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

    public static void loadArenas()
    {
        // TODO: Загрузить арены из конфига
    }

    public static GameArena createArena(String name)
    {
        GameArena arena = new GameArena(name);
        if (addArena(arena))
            return new GameArena(name);
        return getArena(name);
    }
}
