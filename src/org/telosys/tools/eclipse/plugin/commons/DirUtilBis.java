
package org.telosys.tools.eclipse.plugin.commons ;

import java.io.File;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;

/**
 * Utility class for DIRECTORY operations ( set of static methods )
 * 
 * @author Laurent GUERIN 
 * 
 */
public class DirUtilBis {
	
	public static void checkDirIsValid(String directoryAbsolutePath) throws TelosysToolsException {
		if ( StrUtil.nullOrVoid( directoryAbsolutePath ) ) {
			throw new TelosysToolsException(" not defined");
		}
		
		File file = new File(directoryAbsolutePath);
		
		if ( ! file.exists() ) {
			throw new TelosysToolsException(" '" + directoryAbsolutePath + "' does not exist !");
		}
		if ( ! file.isDirectory() ) {
			throw new TelosysToolsException(" '" + directoryAbsolutePath + "' is not a folder !");
		}
	}
	
}
