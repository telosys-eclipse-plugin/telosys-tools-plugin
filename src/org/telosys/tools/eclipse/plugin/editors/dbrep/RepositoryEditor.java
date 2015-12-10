package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.io.File;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.IFormPage;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.eclipse.plugin.commons.ModelUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditor;
import org.telosys.tools.generic.model.Model;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.PersistenceManagerFactory;

/**
 * Main entry point for the "Database Model" editor <br>
 * This editor contains 4 pages : <br>
 * . 1 : the table view with the mapping table<br>
 * . 2 : the "Bulk Generation" page <br>
 * . 3 : the "Configuration" page <br>
 * 
 */
public class RepositoryEditor extends AbstractModelEditor
{
	//--- Pages titles ( shown at the bottom of each page tab )
	private final static String PAGE_1_TITLE = " Model : Entities attributes and mapping " ;
	private final static String PAGE_2_TITLE = " Model : Links between entities " ;
	private final static String PAGE_3_TITLE = " Code generation " ;
	private final static String PAGE_4_TITLE = " Information and configuration " ;
	
//	//----------------------------------------------------------------------------------------------
//	public void refreshAllTargetsTablesFromConfigFile()
//	{
//		log("refreshAllTargetsTablesFromConfigFile() : current bundle = " + _currentBundle);
////		ProjectConfig projectConfig = getProjectConfig();
////		if ( projectConfig != null ) {
//		TelosysToolsCfg telosysToolsCfg = getProjectConfig(); // v 3.0.0
//		if ( telosysToolsCfg != null ) {
//
//			// v 3.0.0 -----------------------------------
//			//TargetsDefinitions targetsDefinitions = projectConfig.getTargetsDefinitions(_currentBundle);
////	    	String sTemplatesFolder = projectConfig.getTelosysToolsCfg().getTemplatesFolderAbsolutePath();
//	    	String sTemplatesFolder = telosysToolsCfg.getTemplatesFolderAbsolutePath(); // v 3.0.0
//			TargetsLoader targetsLoader = new TargetsLoader(sTemplatesFolder);
//			TargetsDefinitions targetsDefinitions;
//			try {
//				targetsDefinitions = targetsLoader.loadTargetsDefinitions(_currentBundle);
//				//return targetsDefinitions ;
//			} catch (GeneratorException e) {
//				MsgBox.error("Cannot load targets definitions", e);
//				// if error : void lists for templates and resources 
//				targetsDefinitions = new TargetsDefinitions(new LinkedList<TargetDefinition>(), new LinkedList<TargetDefinition>());
//			} 
//
////			_page1.refreshTargetsTable(targetsDefinitions.getTemplatesTargets());
//			_page3.refreshTargetsTable(targetsDefinitions.getTemplatesTargets(), targetsDefinitions.getResourcesTargets());
//		}
//	}
	

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
//		PluginLogger.log(this, "init(..,..)..." );
//		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
//		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
//		setPartName(input.getName());
//		
//		_fileName = input.getName() ;
//
//		if ( input instanceof IFileEditorInput )
//		{
//			IFileEditorInput fileInput = (IFileEditorInput) input;
//			_file = fileInput.getFile();
//		}
//		else // never happends
//		{
//			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
//		}
		
// Removed in v 3.0.0
//		_repositoryModel = loadRepository( _file );
		
	}
	
//	private RepositoryModel loadRepository( IFile iFile )
//	{
//		File repositoryFile = EclipseWksUtil.toFile(iFile);
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
//	}

//	private void saveRepository( RepositoryModel repositoryModel, IFile iFile )
//	{
//		File repositoryFile = EclipseWksUtil.toFile(iFile);
//
//		log("Save repository in file " + repositoryFile.getAbsolutePath());
//		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
//		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, getLogger());
//		try {
//			persistenceManager.save(repositoryModel);
//			log("Repository saved.");
//		} catch (TelosysToolsException e) {
//			MsgBox.error("Cannot save repository", e);
//		}
//	}

	@Override
    public void saveModel( Model model, File file ) {
//		if ( model instanceof RepositoryModel ) {
//			MsgBox.error("Cannot save model.\n The given model is not an instance of RepositoryModel");
//			return;
//		}
//		log("Save repository in file " + file.getAbsolutePath());
//		RepositoryModel repositoryModel = (RepositoryModel)model;
		
		RepositoryModel repositoryModel;
		try {
			repositoryModel = ModelUtil.toRepositoryModel( model );
		} catch (Exception e1) {
			MsgBox.error("Cannot save model.\n Cannot convert the given model to RepositoryModel");
			return;
		}
		
		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(file, getLogger());
		try {
			persistenceManager.save(repositoryModel);
			log("Repository saved.");
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot save repository", e);
		}
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
	 */
	protected void addPages() {
		PluginLogger.log(this, "addPages()..." );

//		//--- Get the initial list of targets/templates
//		List<TargetDefinition> targetsList = null ;
//		ProjectConfig projectConfig = getProjectConfig();
//		if ( projectConfig != null )
//		{
//			targetsList = projectConfig.getTemplates(null); // NB : the list can be null 
//		}

		IFormPage page1 = new RepositoryEditorPage1(this, "RepositoryEditorPage1", PAGE_1_TITLE );
		IFormPage page2 = new RepositoryEditorPage2(this, "RepositoryEditorPage2", PAGE_2_TITLE);
		IFormPage page3 = new RepositoryEditorPageCodeGeneration(this, "RepositoryEditorPage3", PAGE_3_TITLE );
		IFormPage page4 = new RepositoryEditorPage4(this, "RepositoryEditorPage4", PAGE_4_TITLE);
		try {
			addPage(page1);
			addPage(page2);
			addPage(page3);
			addPage(page4);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
	}

//	/* (non-Javadoc)
//	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
//	 */
//	public void doSave(IProgressMonitor monitor) {
//		PluginLogger.log(this, "doSave()..." );
//
//		monitor.beginTask( "Saving the repository...", IProgressMonitor.UNKNOWN );
//
//		saveRepository(_repositoryModel, _file );
//		
//		setDirty(false);
//		
//		try {
//			_file.refreshLocal(IResource.DEPTH_ZERO, monitor);
//		} catch (CoreException e) {
//			MsgBox.error("Cannot refresh the XML file after save", e );
//		}
//		
//		monitor.done();
//	}

//	/* (non-Javadoc)
//	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
//	 */
//	public void doSaveAs() {
//		PluginLogger.log(this, "doSaveAs()..." );
//	}

//	/* (non-Javadoc)
//	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
//	 */
//	/*
//	 * Allow the "Save as"
//	 */
//	public boolean isSaveAsAllowed() {
//		PluginLogger.log(this, "isSaveAsAllowed()..." );
//		// Auto-generated method stub
//		return false ;
//	}

//	public String getFileName () {
//		return _fileName ;
//	}
//	
//	public IFile getFile () {
//		return _file ;
//	}
	
//	public TelosysToolsCfg getProjectConfig () {
//		PluginLogger.log(this, "getProjectConfig()..." );
////		return ProjectConfigManager.getProjectConfig( getProject() );
//		return ProjectConfigManager.loadProjectConfig( getProject() ); // v 3.0.0
//	}
	
	
//	public RepositoryModel getDatabaseRepository() {
//		return ModelUtil.toRepositoryModel( getModel());
//	}

}