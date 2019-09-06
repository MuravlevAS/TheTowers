package ru.sgk.thetowers.commands;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;
import ru.sgk.thetowers.game.data.troops.TroopPhantom;
import ru.sgk.thetowers.utils.Logs;

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
                        if(args[2].equalsIgnoreCase("setlobby"))
                        {
                            String arena = args[1];
                            //ToDo: Метод постановки лобби
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
                                    String arena = args[1];
                                    String color = args[3];
                                    //ToDo: Метод создания команды
                                    return false;
                                }
                                else if(args[2].equalsIgnoreCase("removeteam"))
                                {
                                    String arena = args[1];
                                    String color = args[3];
                                    //ToDo: Метод удаления команды
                                    return false;
                                }
                                else if(args[2].equalsIgnoreCase("setteamsize"))
                                {
                                    String arena = args[1];
                                    Integer size = Integer.parseInt(args[3]);
                                    //ToDo: Метод изменения размера команды
                                    return false;
                                }
                                else
                                {
                                    if(args[2].equalsIgnoreCase("team"))
                                    {
                                        
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
        }

        return true;
    }
}
