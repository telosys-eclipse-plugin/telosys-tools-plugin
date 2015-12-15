package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditor;
import org.telosys.tools.generic.model.Model;

/**
 * Main entry point for the DSL model editor (for code generation) <br>
 * 
 */
public class ModelEditor extends AbstractModelEditor {
	
//	//----------------------------------------------------------------------------------------
//    public ModelEditor() {
//    	super();
//    }
    
	//========================================================================================
	// Editor plugin startup ( for each file to edit ) :
	// Step 1 : init()
	// Step 2 : addPages()
	//========================================================================================

	//----------------------------------------------------------------------------------------
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
	}
    
	//----------------------------------------------------------------------------------------
    @Override
	protected void addPages() {
		log(this, "addPages()..." );

		ModelEditorPageModelEntities page1 = 
			new ModelEditorPageModelEntities(this, "ModelEditorPage1", " Model entities " );
		
		ModelEditorPageModelInfo page2 = 
			new ModelEditorPageModelInfo(this, "ModelEditorPage2", " Model information " );
		
		ModelEditorPageCodeGeneration page3 = 
			new ModelEditorPageCodeGeneration(this, "ModelEditorPage3", " Code generation " );
		
		try {
			addPage(page1);
			addPage(page2);
			addPage(page3);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
	}

    //----------------------------------------------------------------------------------------
    @Override
    public void saveModel( Model model, File file ) {
    	// TODO
    }
}