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
    private static String[] stringValues;
    static {

        stringValues = new String[values().length];
        GameTeamColor[] values = values();
        for (int i = 0; i < stringValues.length; i++) {
            stringValues[i] = values[i].toString();
        }
    }
    public static String[] stringValues(){
        return stringValues;
    }
}
