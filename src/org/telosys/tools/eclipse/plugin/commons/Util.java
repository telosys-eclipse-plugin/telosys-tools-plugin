package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Util {
	
    private final static int RED   = 194;
    private final static int GREEN = 208;
    private final static int BLUE  = 245;
        
    public static Color getColor(Display display){
        
        return new Color(display, RED, GREEN, BLUE);
    }

//    public static String buildGetter(String attName, String sLongType) {
//		String s = attName.substring(0, 1).toUpperCase()+attName.substring(1, attName.length());
//		if ( "boolean".equals(sLongType) )
//		{
//			return "is"+s;
//		}
//		else
//		{
//			return "get"+s;
//		}
//	}

//    public static String buildSetter(String attName) {
//		return "set"+attName.substring(0, 1).toUpperCase()+attName.substring(1, attName.length());
//	}

//    private static boolean inList (String sLongType, LinkedList<String> fullNames) 
//    {
//    	for ( String s : fullNames ) {
//    		if ( s != null )
//    		{
//    			if ( s.equals(sLongType) )
//    			{
//    				return true ; // Found 
//    			}
//    		}
//    	}
//    	
////    	Iterator iter = fullNames.iterator() ;
////    	while ( iter.hasNext() )
////    	{
////    		String s = (String) iter.next();
////    		if ( s != null )
////    		{
////    			if ( s.equals(sLongType) )
////    			{
////    				return true ; // Found 
////    			}
////    		}
////    	}
//    	return false ; // Not found
//    }
    
//    /**
//     * Determines the shortest type to use according with the given "not imported types" list
//     * @param sLongType
//     * @param notImportedTypes
//     * @return
//     */
//    public static String shortestType(String sLongType, LinkedList<String> notImportedTypes) 
//    {
//    	if ( notImportedTypes != null )
//    	{
//    		if ( inList(sLongType, notImportedTypes) )
//    		{
//    			return sLongType ; // Keep the "long type" because not imported
//    		}    		
//    	}
//		// No full names to keep => reduce it to short name
//		return JavaClassUtil.shortName(sLongType);
//	}

//    public static String buildFilePath(String dir, String file) {
//    	String s1 = dir ;
//    	if ( dir.endsWith("/") || dir.endsWith("\\") )
//    	{
//    		s1 = dir.substring(0, dir.length() - 1 );
//    	}
//    	
//    	String s2 = file ;
//    	if ( file.startsWith("/") || file.startsWith("\\") )
//    	{
//    		s1 = file.substring(1);
//    	}
//    	
//		return s1 + "/" + s2 ;
//	}

    //-----------------------------------------------------------------------------------------
    
    private final static Cursor CURSOR_WAIT  = new Cursor(null, SWT.CURSOR_WAIT);
    private final static Cursor CURSOR_ARROW = new Cursor(null, SWT.CURSOR_ARROW);
    
    public static Shell getActiveWindowShell()
    {
    	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    	if ( window != null )
    	{
    		return window.getShell();
    	}
    	return null ;
    }
    
    public static Shell cursorWait() 
    {
		Shell shell = getActiveWindowShell();
		cursorWait(shell);
		return shell ;
    }
    public static void cursorWait(Shell shell) 
    {
    	if ( shell != null )
    	{
        	shell.setCursor(CURSOR_WAIT);
    	}
    }

    public static Shell cursorArrow() 
    {
		Shell shell = getActiveWindowShell();
		cursorArrow(shell);
		return shell ;
    }
    public static void cursorArrow(Shell shell) 
    {
    	if ( shell != null )
    	{
        	shell.setCursor(CURSOR_ARROW);
    	}
    }

    
    /**
     * Returns the standard font for a page title
     * @param device
     * @return
     */
    public static Font getPageTitleFont(Device device) 
    {
		FontData fontData = new FontData(); 
		fontData.setName("Arial");
		fontData.setHeight(12);
		// fontData.setStyle(0); // use constants BOLD, ...

		Font font = new Font(device, fontData);
		return font ;
    }
    
    public static Color getPageTitleColor(Device device) 
    {
		Color color = new Color(device, 10, 36, 106); // device, R, G, B
		return color ;
    }
    
    /**
     * Set the title of the page  
     * @param composite the composite where to put the label
     * @param text
     * @return the label control of the title
     */
    public static Label setPageTitle(Composite composite, String text) 
    {
    	Device device = composite.getDisplay();
    	
    	Font font = getPageTitleFont(device); 		
    	Color color = getPageTitleColor(device) ;

    	Label label = new Label(composite, SWT.NULL);
		label.setText(text);
		label.setForeground(color);		
		label.setFont(font);
    	
		return label ;
    }
    
}
