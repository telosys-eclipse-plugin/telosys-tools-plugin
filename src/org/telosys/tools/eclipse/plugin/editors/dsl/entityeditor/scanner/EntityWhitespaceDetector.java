package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class EntityWhitespaceDetector implements IWhitespaceDetector {

//    private static final String NON_ACCEPT = " \t\n\r:";
    private static final String NON_ACCEPT = " \t\n\r:,{}[]";

    public boolean isWhitespace(char c) {
        return NON_ACCEPT.indexOf(c) != -1;
    }
}
