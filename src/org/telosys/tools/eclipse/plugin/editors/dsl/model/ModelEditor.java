package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.telosys.tools.api.GenericModelLoader;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditor;
import org.telosys.tools.generic.model.Model;

/**
 * Main entry point for the DSL model editor (for code generation) <br>
 * 
 */
public class ModelEditor extends AbstractModelEditor {
	
	private List<String>        _entitiesFileNames = null ;
	private Map<String,String>  _entitiesErrors = null ;
	
	//----------------------------------------------------------------------------------------
    public ModelEditor() {
    	super();
    }
    
    public List<String> getEntitiesAbsoluteFileNames() {
    	return _entitiesFileNames ;
    }
    
	public Map<String, String> getEntitiesErrors() {
		return _entitiesErrors;
	}

	//========================================================================================
	// Editor plugin startup ( for each file to edit ) :
	// Step 1 : init()
	// Step 2 : addPages()
	//========================================================================================

	//----------------------------------------------------------------------------------------
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		loadModel();
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
		setCodeGenerationPage(page3);
		
	}

    //----------------------------------------------------------------------------------------
    @Override
    protected Model loadModel(File modelFile) {
		//log("loadModel(" + modelFile + ")");
    	//--- 1) Load entities absolute file names 
    	_entitiesFileNames = DslModelUtil.getEntitiesAbsoluteFileNames(modelFile);
    	
    	//--- 2) Try to parse the model
		GenericModelLoader genericModelLoader = new GenericModelLoader( getProjectConfig() ) ;
		
		Model model;
		try {
			model = genericModelLoader.loadModel(modelFile);
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot load model !\n Unexpected exception" , e );
			return null ;
		}
		
		if ( model != null ) {
			//--- Model OK
			return model;
		}
		else {
			//--- Invalid Model : parsing errors
//			StringBuffer sb = new StringBuffer();
//			String errorMsg = genericModelLoader.getErrorMessage();
			_entitiesErrors = genericModelLoader.getParsingErrors();
//			sb.append("Cannot load model !\n");
//			sb.append("\n");
//			sb.append(errorMsg + " \n");
//			for ( Map.Entry<String,String> entry : _entitiesErrors.entrySet() ) {
//				sb.append(" - '" + entry.getKey() + "' : " + entry.getValue() );
//			}
//			MsgBox.error("Cannot load model !\n" + errorMsg );

			return null ;
		}
    }
    //----------------------------------------------------------------------------------------
    @Override
    protected void saveModel( Model model, File modelFile ) {
    	// TODO
    }
}