package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.repository.model.RepositoryModel;

public abstract class RepositoryEditorPage extends FormPage 
{
	
	private RepositoryEditor   _repEditor   = null ; // Ref on the Editor this page belongs to
	
	private TelosysToolsLogger _logger = null ;
	
	private Color              _backgroundColor = null ;
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPage(FormEditor editor, String id, String title) 
	{
		super(editor, id, title);		
		//super(editor, id, null); // ERROR if title is null
		_repEditor = (RepositoryEditor) editor;
		if ( null == _repEditor ) {
			MsgBox.error("RepositoryEditor is null");
		}
		
		//--- Init the logger
		_logger = _repEditor.getLogger();		
		if ( null == _logger )
		{
			_logger = new ConsoleLogger();
		}
		log(this, "ancestor constructor(.., '"+id+"', '"+ title +"')..." );
		
//		//--- Init the default background color // ERROR / Eclipse !!!
//		Display display = new Display();// ERROR / Eclipse !!!
//		_backgroundColor = display.getSystemColor(SWT.COLOR_GRAY);
		Device device = Display.getCurrent ();
		_backgroundColor = device.getSystemColor(SWT.COLOR_GRAY);
	}
	
//	//----------------------------------------------------------------------------------------------
//	public void setPageTitle(String title) { // v 2.0.7
//		super.setPartName(title); 
//	}
	//----------------------------------------------------------------------------------------------
	protected IProject getProject() {
		return _repEditor.getProject();
	}
	//----------------------------------------------------------------------------------------------
	protected RepositoryModel getRepositoryModel() {
		return _repEditor.getRepositoryModel();
	}
	
	//----------------------------------------------------------------------------------------------
	protected RepositoryEditor getRepositoryEditor() {
		return _repEditor ;
	}
	
	//----------------------------------------------------------------------------------------------
	protected void log(String s) {
		_logger.log(s);
	}
	//----------------------------------------------------------------------------------------------
	protected void log(Object o, String s) {
		_logger.log(o,s);
	}
	//----------------------------------------------------------------------------------------------
	protected TelosysToolsLogger getLogger() {
		return _logger ;
	}
	
	//----------------------------------------------------------------------------------------------
	protected void setDirty() {
		_repEditor.setDirty();
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Returns the current project's configuration <br>
	 * Show a error dialog box if the configuration is not available 
	 * @return the configuration (or null if not available )
	 */
//	protected ProjectConfig getProjectConfig()
	protected TelosysToolsCfg getProjectConfig() // v 3.0.0
	{
//		ProjectConfig config = _repEditor.getProjectConfig();
		TelosysToolsCfg config = _repEditor.getProjectConfig();
		if ( config == null )
		{
			MsgBox.error("ProjectConfig is null");
		}
		return config ;
	}

	//----------------------------------------------------------------------------------------------
	// Not used => Removed in v 3.0.0 
//	protected void reloadTargetsFromConfigFile()
//	{
//		ProjectConfig projectConfig = getProjectConfig();
//		if ( projectConfig != null ) {
//			//--- Reload from file
//			projectConfig.refreshTemplates() ;
//		}
//	}
	//----------------------------------------------------------------------------------------------
	// Not used => Removed in v 3.0.0 
//	protected void refreshAllTargetsTablesFromConfigFile()
//	{
//		reloadTargetsFromConfigFile();
//	}
	
	//----------------------------------------------------------------------------------------------
	protected void setBackgroundColor()
	{
		Control pageControl = this.getPartControl();
		if ( pageControl != null ) {
			Display display = pageControl.getDisplay();	
			if ( display != null ) {
				_backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);		
				pageControl.setBackground(_backgroundColor ) ;
			}
			else {
				MsgBox.error("setBackgroundColor() : display is null");
			}		
		}
		else {
			MsgBox.error("setBackgroundColor() : getPartControl() has returned null");
		}		
	}
	//----------------------------------------------------------------------------------------------
	protected Color getBackgroundColor()
	{
		return _backgroundColor;
	}
	
	//----------------------------------------------------------------------------------------------
	protected void createFormContent(IManagedForm managedForm) 
	{
		log(this, "createFormContent(..)..." );
		super.createFormContent(managedForm);
		setBackgroundColor();
	}
}
