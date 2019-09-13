package ru.sgk.thetowers.commands;

import java.lang.reflect.Constructor;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.math.BlockVector3;

import joptsimple.ArgumentAcceptingOptionSpec;
import ru.sgk.thetowers.MainTowers;
import ru.sgk.thetowers.data.Configurations;
import ru.sgk.thetowers.game.data.PlayerData;
import ru.sgk.thetowers.game.data.arenas.GameArena;
import ru.sgk.thetowers.game.data.arenas.GameArenas;
import ru.sgk.thetowers.game.data.teams.GameTeam;
import ru.sgk.thetowers.game.data.teams.GameTeamArea;
import ru.sgk.thetowers.game.data.teams.GameTeamColor;
import ru.sgk.thetowers.game.data.troops.AbstractTroop;


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
        	// Команда может быть выполнена только от миени игрока
            if (!(sender instanceof Player))
            {
                sender.sendMessage(Configurations.getLocaleString("console"));
                return true;
            }
            Player player = (Player) sender;
            // Команды для тестов.
            if (args.length == 1)
            {
            	// Заставляет мобов перемещаться на локацию, где находится игрок, вызвавший команду
                if (args[0].equalsIgnoreCase("move"))
                {
                    for (AbstractTroop troop : troops)
                    {
                        troop.move(player.getLocation());
                    }
                    return true;
                }
            }
            if (args.length == 2)
            {
            	// Спавнит моба
            	// Тип моба определяется вторым аргументом.
                if (args[0].equalsIgnoreCase("spawn"))
                {
                    String type = args[1];
                    String typeLower = type.toLowerCase();
                    String troopType = typeLower.replaceFirst(String.valueOf(typeLower.charAt(0)), String.valueOf(typeLower.charAt(0)).toUpperCase());
                    sender.sendMessage("type - " + troopType);
                    try {

                        Constructor<?> troopConstructor = Class.forName("ru.sgk.thetowers.game.data.troops.Troop" +troopType).getDeclaredConstructor(GameTeam.class);

                        AbstractTroop troop = (AbstractTroop) troopConstructor.newInstance(new GameTeam(GameTeamColor.WHITE));
                        troops.add(troop);
                        troop.spawn(player.getLocation());
                    }
                    catch (Throwable t) {
                        sender.sendMessage("§cВозникла ошибка. Проверьте правильность написание типа моба");
                        t.printStackTrace();
                    }
                    return true;
                }

            }
            
            if (args.length < 1)
            {
                printHelp(sender, "help");
                return true;
            }
            //Help
            if(args[0].equalsIgnoreCase("help"))
            {
                printHelp(sender, "help");
                return true;
            }
            //Arenas
            else if (args[0].equalsIgnoreCase("arena"))
            {
            	// Вывод хэлпы по аренам
                if(args.length < 2)
                {
                    printHelp(sender, "arena");
                    return true;
                }
                // Вывод второй страницы хэлпы по аренам
                else if(args[1].equalsIgnoreCase("2"))
                {
                    printHelp(sender, "arena 2");
                    return true;
                }
                else
                {
                    if(args.length < 3)
                    {
                        printHelp(sender, "arena");
                        return true;
                    }
                    else 
                    {
                    	// Получаем арену из конфига
                        String arena = args[1];

                        GameArena gameArena = GameArenas.getArena(arena);
                        // Если арена не найдена, выводим ошибку и 
                        if (gameArena == null){
                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.such-arena-is-not-exist")
                                    .replaceAll("%arena%", arena));
                            return true;
                        }
                        if(args[2].equalsIgnoreCase("setlobby"))
                        {
                        	if(!hasPermission(sender, "towers.arena.setlobby"))
                        	{
                        		//  TODO вывод сообщений о недостатке прав
                        		return true;
                        	}
                            gameArena.setLobbyLocation(player.getLocation());
                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.setlobby"));
                            return true;
                        }
                        else if(args[2].equalsIgnoreCase("createteam"))
                        {
                        	// Получаем колор
                            String color = null;
                            if (args.length == 4)
                                color = args[3];
                            // Получаем выделенный регион из WE
                            LocalSession session = MainTowers.getInstance().getWorldEdit().getSession(player);
                            try 
                            {
                            	// Получаем точки
                                BlockVector3 blockVectorMin = session.getSelection(session.getSelectionWorld()).getMinimumPoint();
                                BlockVector3 blockVectorMax = session.getSelection(session.getSelectionWorld()).getMaximumPoint();
                                Location locMin = new Location(player.getWorld(), blockVectorMin.getX(), blockVectorMin.getY(), blockVectorMin.getZ());
                                Location locMax = new Location(player.getWorld(), blockVectorMax.getX(), blockVectorMax.getY(), blockVectorMax.getZ());

                                // Устанавливаем колор тимы
                                GameTeamColor teamColor = null;
                                // Если аргумент не был введён, то выбираем колор сами
                                if (color == null)
                                {
                                	// Проверяем, есть ли среди тим тима с цветом tColor 
                                    for (GameTeamColor tColor : GameTeamColor.values())
                                    {
                                    	teamColor = tColor;
                                        for (GameTeam t : gameArena.getTeams())
                                        {
                                        	// Если такая тима есть, то идём к следующему цвету
                                            if (teamColor.equals(t.getColor()));
                                            {
                                                teamColor = null;
                                                break;
                                            }
                                        }
                                        // Если такой тимы нет, то выбираем этот колор и проходим дальше
                                        if (teamColor != null) break;
                                    }
                                    if (teamColor == null)
                                    {
                                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.arena-is-full-of-teams"));
                                        return true;
                                    }
                                }
                                // Если же аргумент был введён 
                                else
                                {
                                    try
                                    {
                                    	// Преобразуем строку в объект GameTeamColor
                                        teamColor = GameTeamColor.valueOf(color);
                                    }
                                    catch (IllegalArgumentException e)
                                    {
                                    	// Если не получилось, то выводим сообщение о том,
                                    	// что такой команды не может существовать.
                                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.incorrect-team-color"));
                                        return true;
                                    }
                                    // Проверяем, есть ли уже такая команда. Если нет - проходим дальше 
                                    for (GameTeam t : gameArena.getTeams())
                                    {
                                        if (t.getColor().equals(teamColor))
                                        {
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.such-team-already-exist"));
                                            return true;
                                        }
                                    }
                                }

                                
                                GameTeam team = new GameTeam(teamColor);
                                
                                GameTeamArea area = new GameTeamArea(locMin, locMax, player.getLocation());
                                team.setArea(area);
                                
                                gameArena.addTeam(team);

                                gameArena.saveToConfig();
                                GameArenas.saveConfig();
                                sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.create")
                                .replaceAll("%arena%", arena)
                                .replaceAll("%team%", teamColor.toString()));
                            }
                            // Если с регионом что-то не так, подаём сигнал об этом.
                            catch (IncompleteRegionException e) {
                                sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.not-team-selection"));
                            }


                            return true;
                        }
                        else
                        {
                            if(args.length < 4) {
                                printHelp(sender, "arena");
                                return true;
                            }
                            else
                            {
                                if(args[2].equalsIgnoreCase("removeteam"))
                                {
                                    String color = args[3];
                                    //  TODO: Метод удаления команды
                                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.remove"));
                                    return true;
                                }
                                else if(args[2].equalsIgnoreCase("setteamsize"))
                                {
                                    int size = Integer.parseInt(args[3]);
                                    gameArena.setTeamSize(size);
                                    gameArena.saveToConfig();
                                    GameArenas.saveConfig();
                                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.setsize"));
                                    return true;
                                }
                                else
                                {
                                    if(args[2].equalsIgnoreCase("team"))
                                    {
                                        String color = args[3];

                                        if(args[4].equalsIgnoreCase("setspawn"))
                                        {
                                            double x, y, z;
                                            x = Double.parseDouble(args[5]);
                                            y = Double.parseDouble(args[6]);
                                            z = Double.parseDouble(args[7]);
                                            // TODO: Установка спавна
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.setspawn"));
                                            return true;
                                        }
                                        else if(args[4].equalsIgnoreCase("settroopsspawn"))
                                        {
                                            Location location = player.getLocation();
                                            // TODO: Метод установки спавна мобов
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.settroopsspawn"));
                                            return true;
                                        }
                                        else if(args[4].equalsIgnoreCase("addwaypoint"))
                                        {
                                            Location location = player.getLocation();
                                            // TODO: Метод добавления поворота
                                            sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team.addwaypoint"));
                                            return true;
                                        }
                                        else if(args[4].equalsIgnoreCase("removewaypoint"))
                                        {
                                            // TODO: Метод убирания поворота
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.removewaypoint"));
                                            return true;
                                        }
                                        else if(args[4].equalsIgnoreCase("settroopsend"))
                                        {
                                            // TODO: Метод установки конца дороги
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.settroopsend"));
                                            return true;
                                        }
                                        else if(args[4].equalsIgnoreCase("placingmode"))
                                        {
                                        	// TODO: Placing mode
                                            sender.sendMessage(Configurations.getLocaleString("commands.tower.arenas.team.placingmodeon"));
                                            return true;
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

                }
            }
            //create arena (CreateArena)
            else if(args[0].equalsIgnoreCase("createarena"))
            {
                if(hasPermission(sender, "towers.arena.createarena"))
                {
                    if(args.length < 2)
                    {
                        printHelp(sender, "arena");
                        return true;
                    }
                    else
                    {
                        String arena_name = args[1];
                        GameArena arena = GameArenas.createArena(arena_name);
                        if (args.length == 3) 
                        {
                        	// получаем размер команды из строки
                        	try
                        	{
	                        	int team_size = Integer.parseInt(args[2]);
	                        	if (team_size <= 0)
	                        	{
	                        		sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team-size-less-than-zero"));
	                        		return true;
	                        	}
	                        		
	                        	arena.setTeamSize(team_size);
                        	}
                        	catch (NumberFormatException e)
                        	{
                        		sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.team-size-wrong-format"));
                        		return true;
                        	}
                        }
                        
                        arena.saveToConfig();
                        GameArenas.saveConfig();
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.createarena")
                                .replaceAll("%arena%", arena_name));
                        return true;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
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
                        return true;
                    }
                    else
                    {
                        String arena_name = args[1];
                        GameArena gameArena = GameArenas.removeArena(arena_name);
                        if (gameArena == null)
                        {
                        	sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.such-arena-is-not-exist"));
                        	return true;
                        }
                        GameArenas.saveConfig();
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.removearena")
                                .replaceAll("%arena%", arena_name));
                        return true;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
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
                        return true;
                    }
                    else
                    {
                        String arena_name = args[1];
                        // TODO: метод возвращения списка команд
                        return true;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            //placingmode off
            else if(args[0].equalsIgnoreCase("placingmodeoff"))
            {
                if(hasPermission(sender, "towers.arena.placingmodeoff"))
                {
                    // TODO: выключение режима установки блоков
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.arenas.placingmodeoff"));
                    return true;
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            //player help
            else if(args[0].equalsIgnoreCase("player"))
            {
                printHelp(sender, "player");
                return true;
            }
            //misc help
            else if(args[0].equalsIgnoreCase("misc"))
            {
                printHelp(sender, "misc");
                return true;
            }
            //РАЗНОЕ, reload
            else if(args[0].equalsIgnoreCase("reload"))
            {
                if(hasPermission(sender, "towers.misc.reload"))
                {
                	MainTowers.getInstance().reload();
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.reload"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            // start (ForceStart)
            else if(args[0].equalsIgnoreCase("start"))
            {
                if(hasPermission(sender, "towers.misc.forcestart"))
                {
                    // TODO: метод быстрого запуска
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.forcestart"));
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            //force join (ForceJoin)
            else if(args[0].equalsIgnoreCase("forcejoin"))
            {
                if(hasPermission(sender, "towers.misc.forcejoin"))
                {
                    if(args.length < 2)
                    {
                        // TODO: метод крутого присоединения
                        sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.forcejoin"));
                    }
                    else
                    {
                        printHelp(sender, "misc");
                        return true;
                    }
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            //stop
            else if(args[0].equalsIgnoreCase("stop"))
            {
                if(hasPermission(sender, "towers.misc.stop"))
                {
                    // TODO: метод остановки игры
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.miscellanea.stop"));
                    return true;
                }
                else
                {
                    sender.sendMessage(Configurations.getLocaleString("no-perm"));
                    return true;
                }
            }
            //Игрок, join
            else if(args[0].equalsIgnoreCase("join"))
            {
                if(args.length == 2)
                {
                    String arena_name = args[1];
                    PlayerData.add(player);
                    sender.sendMessage(Configurations.getLocaleString("commands.towers.players.join")
                            .replaceAll("%arena%", arena_name));
                    return true;
                }
                else
                {
                    printHelp(sender, "player");
                    return true;
                }
            }
            //leave
            else if(args[0].equalsIgnoreCase("leave"))
            {
                PlayerData.remove(player);
                sender.sendMessage(Configurations.getLocaleString("commands.towers.players.leave"));
                return true;
            }
            else
            {
                printHelp(sender, "help");
            }
        }

        return true;
    }
}
