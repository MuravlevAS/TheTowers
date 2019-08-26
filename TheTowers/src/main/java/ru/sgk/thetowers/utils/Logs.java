package ru.sgk.thetowers.utils;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

public class Logs {
	private static final String PREFIX = "§e[TheTowers]";
	private static Logger logger = Bukkit.getLogger();
	private static boolean debug = true;
	private static final String DEBUG_PREFIX = PREFIX + " §c§l[DEBUG]";
	
	/**
	 * Sends message to console with plugin prefix 
	 * @param message
	 */
	public static void send(String message)
	{
		logger.info(PREFIX + "§r " + message);
		
	}
	/**
	 * If debug mode is on, then send message to console
	 * @param debugMessage
	 */
	public static void sendDebugMessage(String debugMessage)
	{
		if (debug) logger.info(DEBUG_PREFIX + "§r " + debugMessage);
	}
	
	public static void setDebug(boolean debug)
	{
		Logs.debug = debug;
	}
	
	public static void setLoggger(Logger logger)
	{
		Logs.logger = logger;
	}
	
}
