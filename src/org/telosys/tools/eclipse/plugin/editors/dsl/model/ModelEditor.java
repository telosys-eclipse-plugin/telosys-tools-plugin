package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.telosys.tools.api.GenericModelLoader;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.dsl.DslModelManager;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.dsl.parser.model.DomainModelInfo;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditor;
import org.telosys.tools.generic.model.Model;

/**
 * Main entry point for the DSL model editor (for code generation) <br>
 * 
 */
public class ModelEditor extends AbstractModelEditor {
	
	private ModelEditorPageModelInfo _modelInformationPage = null ;
	
	private List<String>        _entitiesFileNames = null ;
	private Map<String,String>  _entitiesErrors = null ;
	private DomainModelInfo     _modelInfo = null ; 
	
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
	public DomainModelInfo getDomainModelInfo() {
		return _modelInfo ;
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

		ModelEditorPageModelEntities modelEntitiesPage = 
			new ModelEditorPageModelEntities(this, "ModelEditorPage1", " Model entities " );
		
		_modelInformationPage = 
			new ModelEditorPageModelInfo(this, "ModelEditorPage2", " Model information " );
		
		ModelEditorPageCodeGeneration codeGenerationPage = 
			new ModelEditorPageCodeGeneration(this, "ModelEditorPage3", " Code generation " );
		
		try {
			addPage(modelEntitiesPage);
			addPage(_modelInformationPage);
			addPage(codeGenerationPage);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
		setCodeGenerationPage(codeGenerationPage);
		
	}

    //----------------------------------------------------------------------------------------
    @Override
    protected Model loadModel(File modelFile) {
		//log("loadModel(" + modelFile + ")");
    	//--- 1) Load entities absolute file names 
    	_entitiesFileNames = DslModelUtil.getEntitiesAbsoluteFileNames(modelFile);
    	
    	//--- 2) Load model information from ".model" file
		DslModelManager modelManager = new DslModelManager();
		_modelInfo = modelManager.loadModelInformation(modelFile);

    	//--- 3) Try to parse the model
		GenericModelLoader genericModelLoader = new GenericModelLoader( getProjectConfig() ) ;
		
		Model model;
		try {
			model = genericModelLoader.loadModel(modelFile);
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot load model !\n Unexpected exception" , e );
			return null ;
		}
		
		//--- 4) Errors reporting if any
		if ( model != null ) {
			//--- Model OK : no parsing error
			_entitiesErrors = null ;
		}
		else {
			//--- Invalid Model : parsing errors
			_entitiesErrors = genericModelLoader.getParsingErrors();
		}
		return model;
    }
    //----------------------------------------------------------------------------------------
    @Override
    protected void saveModel( Model model, File modelFile ) {
    	if ( _modelInformationPage != null ) {
        	if  ( _modelInfo != null ) {
            	_modelInformationPage.updateModelInformation(_modelInfo);
        		DslModelManager modelManager = new DslModelManager();
        		modelManager.saveModelInformation(modelFile, _modelInfo);
        	}
        	else {
        		MsgBox.error("ModelInfo is null in the editor !");
        	}
    	}
    	else {
    		MsgBox.error("_modelInformationPage is null in the editor !");
    	}
    }
}