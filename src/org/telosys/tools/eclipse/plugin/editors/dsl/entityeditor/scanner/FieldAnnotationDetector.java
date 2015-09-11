package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

public class FieldAnnotationDetector implements IWordDetector {

    /**
     * Determines if the specified character is permissible as the first
     * character of an annotation 
     */
    public boolean isWordStart(char c) {
        return c == '@' ;
    }

    /**
     * Determines if the specified character may be part of an annotation
     * Ok if and only if it is a letter (a..z, A..Z)
     */
    public boolean isWordPart(char c) {
    	if ( Character.isLetter(c) ) return true ;
//    	if ( c == '(' ) return true ;
//    	if ( Character.isDigit(c) ) return true ;
//    	if ( c == ')' ) return true ;
    	return false ;
    }
}