package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * IWordDetector implementation <br>
 * <br>
 * Defines the interface by which WordRule determines whether a given character <br>
 * is valid as part of a word in the current context.<br>
 * 
 * Detector for : integer, string, decimal, boolean, ....
 * 
 */
public class FieldTypeDetector implements IWordDetector {

//    private static final String NON_ACCEPT = " \t\n\r,#@/(\";{}:)[]";

    @Override
    // Returns whether the specified character is valid as the first character in a word.
    public boolean isWordStart(char c) {
//        return true;
    	return Character.isLowerCase(c);
    }

    @Override
    // Returns whether the specified character is valid as a subsequent character in a word.
    public boolean isWordPart(char c) {
//        return NON_ACCEPT.indexOf(c) == -1;
    	return Character.isLowerCase(c);
    }
    
}
