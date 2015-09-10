package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

/**
 * Detector for default words.
 * 
 */
public class EntityDefaultDetector implements IWordDetector {

    private static final String NON_ACCEPT = " \t\n\r,#@/(\";{}:)[]";

    @Override
    public boolean isWordStart(char c) {
        return true;
    }

    @Override
    public boolean isWordPart(char c) {
        return NON_ACCEPT.indexOf(c) == -1;
    }
}
