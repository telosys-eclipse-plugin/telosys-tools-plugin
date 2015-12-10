package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.repository.model.RepositoryModel;

public abstract class AbstractModelEditorPage extends AbstractStandardEditorPage {

	protected final static int LAYOUT_MARGIN_WIDTH = 10 ;

	public AbstractModelEditorPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	public AbstractModelEditor getModelEditor() {
		AbstractStandardEditor standardEditor = super.getStandardEditor();
		if ( standardEditor == null ) {
			MsgBox.error("Cannot get model editor : current model editor is null ");
			return null ;
		}
		if ( standardEditor instanceof AbstractModelEditor ) {
			return (AbstractModelEditor) standardEditor ;
		}
		else {
			MsgBox.error("Current model editor is not an instance of ModelEditor");
			return null ;
		}
	}
	
	protected Model getModel() {
		AbstractModelEditor modelEditor = getModelEditor() ;
		if ( modelEditor != null ) {
			return modelEditor.getModel();
		}
		return null ;
	}
	
	public RepositoryModel getRepositoryModel() {
		AbstractModelEditor modelEditor = getModelEditor() ;
		if ( modelEditor != null ) {
			return modelEditor.getRepositoryModel();
		}
		return null ;
	}
	
}
