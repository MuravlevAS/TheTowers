package ru.sgk.thetowers.commands;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sgk.thetowers.data.Configuration;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.GameArena;
import ru.sgk.thetowers.game.GameArenas;
import ru.sgk.thetowers.game.data.PlayerData;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.teams.GameTeamColor;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;
import ru.sgk.thetowers.game.data.troops.TroopPhantom;
import ru.sgk.thetowers.utils.Logs;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;


public class MainTowersCommand implements CommandExecutor {
    private static List<AbstractTroop> troops = Lists.newArrayList();

    /**
     *
     * @param sender
     * @param perm
     * @return Возвращает true, если sender имеет право perm.
     * Так же возвращает true, если sender имеет право towers.dev или towers.admin.
     * В любом другом случае, возвращает false
     */
    public boolean hasPermission(CommandSender sender, String perm){
        return sender.hasPermission("towers.dev") || sender.hasPermission("towers.admin") || sender.hasPermission(perm);
    }

    /**
     *
     * @param sender
     * @param type - Тип хелпы команд, которые выводить: help, arena, player, misc
     */
    public void printHelp(CommandSender sender, String type) {
        if (type.equalsIgnoreCase("arena"))
        {
            if (hasPermission(sender, "towers.arenas"))
            {
                for(String string : Configurations.getLocaleStringList("commands.towers.arena")) {
                    sender.sendMessage(string);
                }
            }
            else {
                sender.sendMessage(Configurations.getLocaleString("no-perm"));
            }
        }
        else if (type.equalsIgnoreCase("arena 2"))
        {
            if(hasPermission(sender, "towers.arenas"))
            {
                for(String string : Configurations.getLocaleStringList("commands.towers.arena2")) {
                    sender.sendMessage(string);
                }
            }
        }
        else if (type.equalsIgnoreCase("help"))
        {

            if  (   !hasPermission(sender, "towers.arenas") &&
                    // Если так же не имеет других прав, кроме
                    hasPermission(sender, "towers.player")
                )
            {
                printHelp(sender, "player");
            }
            else {
                if (hasPermission(sender, "towers.help"))
                {
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.help.help"));
                }
                if (hasPermission(sender, "towers.arenas"))
                {
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.help.arena"));
                }
                if (hasPermission(sender, "towers.player"))
                {
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.help.player"));
                }
                if (hasPermission(sender, "towers.misc"))
                {
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.help.misc"));
                }
            }
        }
        else if (type.equalsIgnoreCase("player"))
        {
            for(String string : Configurations.getLocaleStringList("commands.towers.player")) {
                sender.sendMessage(string);
            }
        }
        else if (type.equalsIgnoreCase("misc"))
        {
            if (hasPermission(sender, "towers.help.misc"))
            {
                for(String string : Configurations.getLocaleStringList("commands.towers.misc")) {
                    sender.sendMessage(string);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (command.getName().equalsIgnoreCase("towers"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(Configurations.getLocaleString("console"));
                return false;
            }
            Player player = (Player) sender;

            if (args.length == 1)
            {
                if (args[0].equalsIgnoreCase("move"))
                {
                    for (AbstractTroop troop : troops)
                    {
                        troop.move(player.getLocation());
                    }
                }
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("spawn"))
                {
                    String type = args[1];
                    String typeLower = type.toLowerCase();
                    String troopType = typeLower.replaceFirst(String.valueOf(typeLower.charAt(0)), String.valueOf(typeLower.charAt(0)).toUpperCase());
                    sender.sendMessage("type - " + troopType);
                    try {

                        Constructor<?> troopConstructor = Class.forName("ru.sgk.thetowers.game.data.troops.Troop" +troopType).getDeclaredConstructor();

                        AbstractTroop troop = (AbstractTroop) troopConstructor.newInstance();
                        troops.add(troop);
                        troop.spawn(player.getLocation());
                    }
                    catch (Throwable t) {
                        sender.sendMessage("troop not found");
                    }
                }

            }

            if (args.length < 1)
            {
                printHelp(sender, "help");
                return false;
            }
            //Help
            if(args[0].equalsIgnoreCase("help"))
            {
                printHelp(sender, "help");
                return false;
            }
            //Arenas
            else if (args[0].equalsIgnoreCase("arena"))
            {
                if(args.length < 2)
                {
                    printHelp(sender, "arena");
                    return false;
                }
                else if(args[1].equalsIgnoreCase("2"))
                {
                    printHelp(sender, "arena 2");
                    return false;
                }
                else
                {
                    if(args.length < 3)
                    {
                        printHelp(sender, "arena");
                        return false;
                    }
                    else if(hasPermission(sender, "towers.arena.setlobby"))
                    {
                        String arena = args[1];
                        if(args[2].equalsIgnoreCase("setlobby"))
                        {

                            GameArena gameArena = GameArenas.getArena(arena);
                            gameArena.setLobbyLocation(((Player) sender).getLocation());
                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.setlobby"));
                            return false;
                        }
                        else
                        {
                            if(args.length < 4) {
                                printHelp(sender, "arena");
                                return false;
                            }
                            else
                            {
                                if(args[2].equalsIgnoreCase("createteam"))
                                {
                                    String color = args[3];
                                    GameArena gameArena = GameArenas.getArena(arena);
                                    gameArena.addTeam(new GameTeam(GameTeamColor.WHITE));
                                    gameArena.saveToConfig();
                                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.create"));
                                    return false;
                                }
                                else if(args[2].equalsIgnoreCase("removeteam"))
                                {
                                    String color = args[3];
                                    //ToDo: Метод удаления команды
                                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.remove"));
                                    return false;
                                }
                                else if(args[2].equalsIgnoreCase("setteamsize"))
                                {
                                    int size = Integer.parseInt(args[3]);
                                    GameArena gameArena = GameArenas.getArena(arena);
                                    gameArena.setTeamSize(size);
                                    gameArena.saveToConfig();
                                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.setsize"));
                                    return false;
                                }
                                else
                                {
                                    if(args[2].equalsIgnoreCase("team"))
                                    {
                                        String color = args[3];

                                        if(args[4].equalsIgnoreCase("setspawn"))
                                        {
                                            int x, y, z;
                                            x = Integer.parseInt(args[5]);
                                            y = Integer.parseInt(args[6]);
                                            z = Integer.parseInt(args[7]);
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.setspawn"));
                                            return false;
                                        }
                                        else if(args[4].equalsIgnoreCase("settroopsspawn"))
                                        {
                                            Location location = ((Player) sender).getLocation();
                                            //ToDo: Метод установки спавна мобов
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.settroopsspawn"));
                                            return false;
                                        }
                                        else if(args[4].equalsIgnoreCase("addwaypoint"))
                                        {
                                            Location location = ((Player) sender).getLocation();
                                            //ToDo: Метод добавления поворота
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.addwaypoint"));
                                            return false;
                                        }
                                        else if(args[4].equalsIgnoreCase("removewaypoint"))
                                        {
                                            //ToDo: Метод убирания поворота
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.removewaypoint"));
                                            return false;
                                        }
                                        else if(args[4].equalsIgnoreCase("settroopsend"))
                                        {
                                            //ToDo: Метод установки конца дороги
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.settroopsend"));
                                            return false;
                                        }
                                        else if(args[4].equalsIgnoreCase("placingmode"))
                                        {
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.placingmodeon"));
                                            return false;
                                        }
                                        else
                                        {
                                            printHelp(sender, "arena");
                                        }
                                    }
                                    else
                                    {
                                        printHelp(sender, "arena");
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    }

                }
            }
            //create arena (CreateArena)
            else if(args[0].equalsIgnoreCase("createarena"))
            {
                if(hasPermission(sender, "towers.arena.createteam"))
                {
                    if(args.length < 2)
                    {
                        printHelp(sender, "arena");
                        return false;
                    }
                    else
                    {
                        String arena_name = args[1];
                        int team_size = Integer.parseInt(args[2]);
                        GameArena arena = GameArenas.createArena(arena_name);
                        arena.setTeamSize(team_size);
                        arena.saveToConfig();
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.createarena"));
                        return false;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //remove arena (RemoveArena)
            else if(args[0].equalsIgnoreCase("removearena"))
            {
                if(hasPermission(sender, "towers.arena.removearena"))
                {
                    if(args.length < 2)
                    {
                        printHelp(sender, "arena");
                        return false;
                    }
                    else
                    {
                        String arena_name = args[1];
                        //ToDo: метод удаления арены
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.removearena"));
                        return false;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //team list (TeamList)
            else if(args[0].equalsIgnoreCase("teamlist"))
            {
                if(hasPermission(sender, "towers.arena.teamlist"))
                {
                    if(args.length < 2)
                    {
                        printHelp(sender, "arena");
                        return false;
                    }
                    else
                    {
                        String arena_name = args[1];
                        //ToDo: метод возвращения списка команд
                        return false;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //placingmode off
            else if(args[0].equalsIgnoreCase("placingmodeoff"))
            {
                if(hasPermission(sender, "towers.arena.placingmodeoff"))
                {
                    //ToDo: выключение режима установки блоков
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.placingmodeoff"));
                    return false;
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //РАЗНОЕ, reload
            else if(args[0].equalsIgnoreCase("reload"))
            {
                if(hasPermission(sender, "towers.misc.reload"))
                {
                    Configurations.relaodConfig();
                    Configurations.relaodSettings();
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.reload"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //quick start (QuickStart)
            else if(args[0].equalsIgnoreCase("quickstart"))
            {
                if(hasPermission(sender, "towers.misc.quickstart"))
                {
                    //ToDo: метод быстрого запуска
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.quickstart"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //force start (ForceStart)
            else if(args[0].equalsIgnoreCase("forcestart"))
            {
                if(hasPermission(sender, "towers.misc.forcestart"))
                {
                    //ToDo: метод быстрого запуска №2
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.forcestart"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //force join (ForceJoin)
            else if(args[0].equalsIgnoreCase("forcejoin"))
            {
                if(hasPermission(sender, "towers.misc.forcejoin"))
                {
                    if(args.length < 2)
                    {
                        //ToDo: метод крутого вопроса
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.forcejoin"));
                    }
                    else
                    {
                        printHelp(sender, "misc");
                        return false;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //stop
            else if(args[0].equalsIgnoreCase("stop"))
            {
                if(hasPermission(sender, "towers.misc.stop"))
                {
                    //ToDo: метод остановки игры
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.stop"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return false;
                }
            }
            //Игрок, join
            else if(args[0].equalsIgnoreCase("join"))
            {
                if(args.length < 2)
                {
                    PlayerData.add((Player)sender);
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.players.join"));
                }
                else
                {
                    printHelp(sender, "player");
                    return false;
                }
            }
            //leave
            else if(args[0].equalsIgnoreCase("leave"))
            {
                PlayerData.remove((Player)sender);
                sender.sendMessage(Configurations.getLocaleString("commands.towers.players.leave"));
            }
            else
            {
                printHelp(sender, "help");
            }
        }

        return true;
    }
}
