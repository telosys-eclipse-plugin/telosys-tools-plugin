package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.ContentAssistAction;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.telosys.tools.eclipse.plugin.editors.dsl.common.EditorsException;

/**
 * Main class for the Entity Editor.
 * 
 */
@SuppressWarnings("deprecation")
public class EntityEditor extends TextEditor {

    public EntityEditor() {
        super();
        setSourceViewerConfiguration(new EntityEditorConfiguration());
    }

    protected void createActions() throws EditorsException {
        super.createActions();
        ResourceBundle resourceBundle = null;
        try {
            resourceBundle = new PropertyResourceBundle(
                    new StringBufferInputStream(
                            "ContentAssistProposal.label=Content assist\nContentAssistProposal.tooltip=Content assist\nContentAssistProposal.description=Provides Content Assistance"));
        } catch (IOException e) {
            throw new EditorsException(
                    "Error while creating the autocompletion : " + e);
        }
        ContentAssistAction action = new ContentAssistAction(resourceBundle,
                "ContentAssistProposal.", this);
        action.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
        setAction("ContentAssist", action);
    }

}
