package ru.sgk.thetowers.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.utils.Logs;

public class MainTowersCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Configurations.getLocaleString("console"));
            return false;
        }

        if(args.length < 1) {
            for(String string : Configurations.getLocaleStringList("commands.towers.help")) {
                sender.sendMessage(string);
            }
            return false;
        }

        //Arenas
        if(args[0].equalsIgnoreCase("arena")) {
            if(args.length < 2) {
                for(String string : Configurations.getLocaleStringList("commands.towers.arena")) {
                    sender.sendMessage(string);
                }
            } else if(args[1].equalsIgnoreCase("2")){

            }
        }

        return true;
    }
}
