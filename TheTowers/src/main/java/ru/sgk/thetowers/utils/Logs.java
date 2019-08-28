package ru.sgk.thetowers.utils;

import org.bukkit.configuration.file.FileConfiguration;

import ru.sgk.thetowers.data.Configurations;

public class Logs {
	private static boolean debug = false;
	
	public static void init()
	{
		Logs.send("Checking for debug mode.");
		FileConfiguration config;
		if ((config = Configurations.getConfig()) != null)
		{
			debug = config.contains("debug") && config.getBoolean("debug");
		} else debug = false;
		if (debug) Logs.send("Debug mode is enable.");
		else   Logs.send("Debug mode is disable.");
	}
	
	/**
	 * Sends message to console with plugin prefix 
	 * @param message
	 */
	public static void send(String message)
	{
		Consts.logger.info(Consts.PREFIX + "§r " + message);
		
	}
	/**
	 * If debug mode is on, then send message to console
	 * @param debugMessage
	 */
	public static void sendDebugMessage(String debugMessage)
	{
		if (debug) Consts.logger.info(Consts.DEBUG_PREFIX + "§r " + debugMessage);
	}
	
	public static void setDebug(boolean debug)
	{
		if (Logs.debug = debug) Logs.send("Debug mode is now enable.");
	}
	
}
