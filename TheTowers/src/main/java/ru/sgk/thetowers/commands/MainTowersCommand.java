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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("towers")) {
            if (!(sender instanceof Player)) {
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

            if (args.length < 1) {
                printHelp(sender, "help");
                return false;
            }
            //Arenas
            if (args[0].equalsIgnoreCase("arena")) {
                if(args.length < 2) {
                    printHelp(sender, "arena");
                    return false;
                } else if(args[1].equalsIgnoreCase("2")) {
                    printHelp(sender, "arena 2");
                    return false;
                }
            }
        }

        return true;
    }
}
