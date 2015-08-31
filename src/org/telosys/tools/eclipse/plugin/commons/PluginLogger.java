package org.telosys.tools.eclipse.plugin.commons;

import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;

/**
 * Basic utility class for plugin logging (only for DEBUG) 
 * 
 * @author Laurent GUERIN
 *
 */
public class PluginLogger
{
	private final static TelosysToolsLogger logger = new ConsoleLogger();

	//----------------------------------------------------------------------------------
	public static TelosysToolsLogger getLogger() {
		return logger;
	}
	
	//----------------------------------------------------------------------------------
	public static void log(Object object, String s) {
		logger.log(object, s);
	}
	//----------------------------------------------------------------------------------
	public static void log(String s) {
		logger.log(s);
	}
	//----------------------------------------------------------------------------------
	public static void info(String s) {
		logger.info(s);
	}

	//----------------------------------------------------------------------------------
	public static void error(String s) {
		logger.error(s);
	}

	
}
