package org.telosys.tools.eclipse.plugin.editors.dbrep;

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
import org.eclipse.ui.forms.editor.IFormPage;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.target.TargetsDefinitions;
import org.telosys.tools.generator.target.TargetsLoader;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.PersistenceManagerFactory;

/**
 * Main entry point for the DBRepository editor <br>
 * This editor contains 3 pages : <br>
 * . 1 : the table view with the mapping table<br>
 * . 2 : the "Bulk Generation" page <br>
 * . 3 : the "Configuration" page <br>
 * 
 */
public class RepositoryEditor extends FormEditor 
{
	//--- Pages titles ( shown at the bottom of each page tab )
	private final static String PAGE_1_TITLE = " Model : Entities attributes and mapping " ;
	private final static String PAGE_2_TITLE = " Model : Links between entities " ;
	private final static String PAGE_3_TITLE = " Code generation " ;
	private final static String PAGE_4_TITLE = " Information and configuration " ;
	
	protected final static int LAYOUT_MARGIN_WIDTH = 10 ;
	
	/** The dirty flag : see isDirty() */
    private boolean  _dirty = false;

	private String          _fileName = "???" ;
	private IFile           _file     = null ;
	private RepositoryModel _repositoryModel = null;
	
	private String          _currentBundle = null ; // v 2.0.7
	
	//--- Pages managed by this editor
	private RepositoryEditorPage1 _page1 = null ;
	private RepositoryEditorPage3 _page3 = null ;
	
    private TelosysToolsLogger _logger = new ConsoleLogger() ;

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

		if ( input instanceof IFileEditorInput )
		{
			IFileEditorInput fileInput = (IFileEditorInput) input;
			_file = fileInput.getFile();
		}
		else // never happends
		{
			MsgBox.error("The editor input '" + input.getName() + "' is not a File ! ");
		}
		
// Removed in v 3.0.0
//		ProjectConfig projectConfig = getProjectConfig();
//		if ( projectConfig != null )
//		{
//			PluginLogger.log(this, "init(..,..) : refreshSpecificTemplates()..." );
//			projectConfig.refreshTemplates() ;
//		}
//		else
//		{
//			MsgBox.error("Cannot get project configuration");
//		}

		_repositoryModel = loadRepository( _file );
		
	}
	
	private RepositoryModel loadRepository( IFile iFile )
	{
		File repositoryFile = EclipseWksUtil.toFile(iFile);
		_logger.info("Load repository from file " + repositoryFile.getAbsolutePath());
		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, _logger);
		RepositoryModel repositoryModel = null ;
		try {
			repositoryModel = persistenceManager.load();
			_logger.info("Repository loaded : " + repositoryModel.getNumberOfEntities() + " entitie(s)"  );
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot load repository", e );
		}		
		return repositoryModel ;
	}

	private void saveRepository( RepositoryModel repositoryModel, IFile iFile )
	{
		File repositoryFile = EclipseWksUtil.toFile(iFile);

		_logger.info("Save repository in file " + repositoryFile.getAbsolutePath());
		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, _logger);
		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, _logger);
		try {
			persistenceManager.save(repositoryModel);
			_logger.info("Repository saved.");
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

		_page1          = new RepositoryEditorPage1(this, "RepositoryEditorPage1", PAGE_1_TITLE );
		IFormPage page2 = new RepositoryEditorPage2(this, "RepositoryEditorPage2", PAGE_2_TITLE);
		_page3          = new RepositoryEditorPage3(this, "RepositoryEditorPage3", PAGE_3_TITLE );
		IFormPage page4 = new RepositoryEditorPage4(this, "RepositoryEditorPage4", PAGE_4_TITLE);
		try {
			addPage(_page1);
			addPage(page2);
			addPage(_page3);
			addPage(page4);
		} catch (PartInitException e) {
			MsgBox.error("RepositoryEditor : addPage(page) throws PartInitException ", e);
		}		
	}

	public boolean isDirty()
	{
		return _dirty;
	}

	public void setDirty()
	{
		setDirty(true);
//		editorDirtyStateChanged(); // Notify the editor 
	}
	
	private void setDirty(boolean flag)
	{
		_dirty = flag ;
		editorDirtyStateChanged(); // Notify the editor 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		PluginLogger.log(this, "doSave()..." );

		monitor.beginTask( "Saving the repository...", IProgressMonitor.UNKNOWN );

		saveRepository(_repositoryModel, _file );
		
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
	/*
	 * Allow the "Save as"
	 */
	public boolean isSaveAsAllowed() {
		PluginLogger.log(this, "isSaveAsAllowed()..." );
		// Auto-generated method stub
		return false ;
	}

	public String getFileName ()
	{
		return _fileName ;
	}
	
//	public String getDatabaseTitle()
//	{
//		if ( _repositoryModel != null )
//		{
//			String sName = _repositoryModel.getDatabaseName();
//			String sType = _repositoryModel.getDatabaseType();
//			return "Database \"" + sName + "\"  ( " + sType+ " ) " ; 
//		}
//		else
//		{
//			MsgBox.error("getTitle() : _databaseRepository is null ");
//			return "???";
//		}
//	}
	public IFile getFile ()
	{
		return _file ;
	}
	
	public IProject getProject ()
	{		
		return _file.getProject() ;
	}
	
//	public ProjectConfig getProjectConfig ()
	public TelosysToolsCfg getProjectConfig ()
	{
		PluginLogger.log(this, "getProjectConfig()..." );
//		return ProjectConfigManager.getProjectConfig( getProject() );
		return ProjectConfigManager.loadProjectConfig( getProject() ); // v 3.0.0
	}
	
	public TelosysToolsLogger getLogger ()
	{		
		return _logger ;
	}
	
	public RepositoryModel getDatabaseRepository()
	{
		return _repositoryModel ;
	}
	
//	//----------------------------------------------------------------------------------------------
//	public void refreshAllTargetsTablesFromConfigFile(String bundleName)
//	{
//		//_page2.setPageTitle("bundle '" + bundleName + "'");
//	}
	//----------------------------------------------------------------------------------------------
	public void setCurrentBundleName(String bundleName) {
		_currentBundle = bundleName ;
	}
	//----------------------------------------------------------------------------------------------
	public String getCurrentBundleName() {
		return _currentBundle ;
	}
	//----------------------------------------------------------------------------------------------
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

			_page1.refreshTargetsTable(targetsDefinitions.getTemplatesTargets());
			_page3.refreshTargetsTable(targetsDefinitions.getTemplatesTargets(), targetsDefinitions.getResourcesTargets());
		}
	}
	
}