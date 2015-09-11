package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detector for 'entity name' <br>
 * eg : 'Book', 'Country', etc
 * 
 */
public class EntityNameDetector implements IWordDetector {

    @Override
    public boolean isWordStart(char c) {
        return Character.isUpperCase(c);
    }

    @Override
    public boolean isWordPart(char c) {
        return (Character.isLetterOrDigit(c));
    }

}
