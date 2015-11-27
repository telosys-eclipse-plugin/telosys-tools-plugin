package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.LinkedList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.dsl.loader.ModelLoader;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.eclipse.plugin.editors.commons.EditorWithCodeGeneration;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.target.TargetsDefinitions;
import org.telosys.tools.generator.target.TargetsLoader;
import org.telosys.tools.generic.model.Model;

/**
 * Main entry point for the DSL model editor (for code generation) <br>
 * 
 */
public class ModelEditor extends FormEditor implements EditorWithCodeGeneration
{
	//--- Pages titles ( shown at the bottom of each page tab )
	private final static String PAGE_1_TITLE = " Code generation " ;
	private final static String PAGE_2_TITLE = " Model information " ;
	
	protected final static int LAYOUT_MARGIN_WIDTH = 10 ;
	
	/** The dirty flag : see isDirty() */
    private boolean         _dirty = false;

	private String          _fileName = "???" ;
	private IFile           _file     = null ;
	private Model           _model    = null;
	
	private String          _currentBundle = null ; // v 2.0.7
	
	//--- Pages managed by this editor
	private ModelEditorPage1 _page1 = null ;
	private ModelEditorPage2 _page2 = null ;
	
    private TelosysToolsLogger _logger = new ConsoleLogger() ;

	//----------------------------------------------------------------------------------------
    public ModelEditor() {
    	super();
    }
    
	//----------------------------------------------------------------------------------------
	@Override
	public IProject getProject () {	
		return _file.getProject() ;
	}
	
	@Override
	public TelosysToolsLogger getLogger() {
		return _logger ;
	}
	
	@Override
	public Model getModel() {
		return _model ;
	}

	@Override
	public void setCurrentBundleName(String bundleName) {
		_currentBundle = bundleName ;
	}
	
	@Override
	public String getCurrentBundleName() {
		return _currentBundle ;
	}

	//----------------------------------------------------------------------------------------------
	@Override
	public void refreshAllTargetsTablesFromConfigFile()
	{
		_logger.log("refreshAllTargetsTablesFromConfigFile() : current bundle = " + _currentBundle);
//		ProjectConfig projectConfig = getProjectConfig();
//		if ( projectConfig != null ) {
		TelosysToolsCfg telosysToolsCfg = getProjectConfig(); // v 3.0.0
		if ( telosysToolsCfg != null ) {

			// v 3.0.0 -----------------------------------
			//TargetsDefinitions targetsDefinitions = projectConfig.getTargetsDefinitions(_currentBundle);
//	    	String sTemplatesFolder = projectConfig.getTelosysToolsCfg().getTemplatesFolderAbsolutePath();
	    	String sTemplatesFolder = telosysToolsCfg.getTemplatesFolderAbsolutePath(); // v 3.0.0
			TargetsLoader targetsLoader = new TargetsLoader(sTemplatesFolder);
			TargetsDefinitions targetsDefinitions;
			try {
				targetsDefinitions = targetsLoader.loadTargetsDefinitions(_currentBundle);
				//return targetsDefinitions ;
			} catch (GeneratorException e) {
				MsgBox.error("Cannot load targets definitions", e);
				// if error : void lists for templates and resources 
				targetsDefinitions = new TargetsDefinitions(new LinkedList<TargetDefinition>(), new LinkedList<TargetDefinition>());
			} 

//			_page1.refreshTargetsTable(targetsDefinitions.getTemplatesTargets());
			_page1.refreshTargetsTable(targetsDefinitions.getTemplatesTargets(), targetsDefinitions.getResourcesTargets());
		}
	}

	//----------------------------------------------------------------------------------------
	public String getFileName() {
		return _fileName ;
	}
	
	public IFile getFile() {
		return _file ;
	}
	
	public TelosysToolsCfg getProjectConfig() {
		return ProjectConfigManager.loadProjectConfig( getProject() ); 
	}
	
	//----------------------------------------------------------------------------------------
	public boolean isDirty() {
		return _dirty;
	}

	public void setDirty() {
		setDirty(true);
	}
	
	private void setDirty(boolean flag) {
		_dirty = flag ;
		editorDirtyStateChanged(); // Notify the editor 
	}

	//========================================================================================
	// Editor plugin startup ( for each file to edit ) :
	// Step 1 : init()
	// Step 2 : addPages()
	//========================================================================================

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
    @Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		super.init(site, input);
		PluginLogger.log(this, "init(..,..)..." );
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
		setPartName(input.getName());
		
		_fileName = input.getName() ;

		if ( input instanceof IFileEditorInput ) {
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_file = fileInput.getFile();
		}
		else { // never happends
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
		
		_model = loadModel( _file );
	}
    
	//----------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		PluginLogger.log(this, "addPages()..." );

		_page1 = new ModelEditorPage1(this, "RepositoryEditorPage1", PAGE_1_TITLE );
		_page2 = new ModelEditorPage2(this, "RepositoryEditorPage2", PAGE_2_TITLE );
		try {
			addPage(_page1);
			addPage(_page2);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
	}

	//----------------------------------------------------------------------------------------	
	private Model loadModel( IFile iFile ) {
		File modelFile = EclipseWksUtil.toFile(iFile);
		ModelLoader modelLoader = new ModelLoader();
		Model model = null ;
		try {
			model = modelLoader.loadModel( modelFile );
		} catch (Exception e) {
			MsgBox.error("Cannot load model ", e);
		}
		return model ;
		
//		_logger.info("Load repository from file " + repositoryFile.getAbsolutePath());
//		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
//		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, _logger);
//		RepositoryModel repositoryModel = null ;
//		try {
//			repositoryModel = persistenceManager.load();
//			_logger.info("Repository loaded : " + repositoryModel.getNumberOfEntities() + " entitie(s)"  );
//		} catch (TelosysToolsException e) {
//			MsgBox.error("Cannot load repository", e );
//		}		
//		return repositoryModel ;
	}

	private void saveRepository( Model model, IFile iFile )
	{
		File repositoryFile = EclipseWksUtil.toFile(iFile);
		
		// TODO
//		_logger.info("Save repository in file " + repositoryFile.getAbsolutePath());
//		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
//		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, _logger);
//		try {
//			persistenceManager.save(repositoryModel);
//			_logger.info("Repository saved.");
//		} catch (TelosysToolsException e) {
//			MsgBox.error("Cannot save repository", e);
//		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		PluginLogger.log(this, "doSave()..." );

		monitor.beginTask( "Saving the repository...", IProgressMonitor.UNKNOWN );

		// TODO
		//saveRepository(_repositoryModel, _file );
		
		setDirty(false);
		
		try {
			_file.refreshLocal(IResource.DEPTH_ZERO, monitor);
		} catch (CoreException e) {
			MsgBox.error("Cannot refresh the XML file after save", e );
		}
		
		monitor.done();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		PluginLogger.log(this, "doSaveAs()..." );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() { // Allow the "Save as" ?
		PluginLogger.log(this, "isSaveAsAllowed()..." );
		return false ; // "Save as" not allowed
	}

	//----------------------------------------------------------------------------------------------
	
}