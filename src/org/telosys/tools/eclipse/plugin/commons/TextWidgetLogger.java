package org.telosys.tools.eclipse.plugin.commons;

import org.telosys.tools.commons.GenericLogger;

/**
 *  
 * 
 * @author Laurent GUERIN
 *
 */
public class TextWidgetLogger extends GenericLogger // implements  TelosysToolsLogger 
{
	private StringBuilder  stringBuilder = new StringBuilder() ;
	
	//----------------------------------------------------------------------------------
	public TextWidgetLogger() {
		super();
	}
	//----------------------------------------------------------------------------------
	protected void print(String s) {
		stringBuilder.append( s + "\n");
	}
	//----------------------------------------------------------------------------------
	public String getContent() {
		return stringBuilder.toString();
	}
	//----------------------------------------------------------------------------------
	public void clear() {
		stringBuilder = new StringBuilder() ;
	}

}
