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
	// INFO
	//----------------------------------------------------------------------------------
	public static void info(String s) {
		logger.info(s);
	}

	//----------------------------------------------------------------------------------
	// ERROR
	//----------------------------------------------------------------------------------
	public static void error(String s) {
		logger.error(s);
	}
	
	//----------------------------------------------------------------------------------
	// LOG
	//----------------------------------------------------------------------------------
	public static void log(Object object, String s) {
		logger.log(object, s);
	}
	//----------------------------------------------------------------------------------
	public static void log(String s) {
		logger.log(s);
	}

	//----------------------------------------------------------------------------------
	// DEBUG
	//----------------------------------------------------------------------------------
	private final static boolean DEBUG = true ;
	public static void debug(String s) {
		if ( DEBUG ) {
			System.out.println("[DEBUG]: " + s );
		}
	}
	
}
