package ru.sgk.thetowers.game.data.teams;

public enum GameTeamColor
{
    RED,            // &c
    BLUE,           // &9
    GREEN,          // &a
    YELLOW,         // &e

    PURPLE,         // &d
    AQUA,           // &b
    GOLD,           // &6

    DARK_PURPLE,    // &5
    DARK_GREEN,     // &2
    DARK_BLUE,      // &1

    WHITE,          // &f
    GRAY;           // &7
    public static String[] stringValues()
    {
        String[] strings = new String[values().length];
        GameTeamColor[] values = values();
        for (int i = 0; i < strings.length; i++) {
            strings[i] = values[i].toString();
        }
        return strings;
    }
}
