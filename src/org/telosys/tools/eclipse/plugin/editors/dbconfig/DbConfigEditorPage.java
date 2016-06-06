package org.telosys.tools.eclipse.plugin.editors.dbconfig;

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
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public abstract class DbConfigEditorPage extends FormPage 
{
	
	private DbConfigEditor      _editor   = null ; // Ref on the Editor this page belongs to
	
	private TelosysToolsLogger  _logger = null ;
	
	private Color               _backgroundColor = null ;
	
	public DbConfigEditorPage(FormEditor editor, String id, String title) 
	{
		super(editor, id, title);// NB : ERROR if title is null
		
		_editor = (DbConfigEditor) editor;
		if ( null == _editor ) {
			MsgBox.error("Editor is null");
		}
		
		//--- Init the logger
		_logger = _editor.getLogger();		
		if ( null == _logger )
		{
			_logger = new ConsoleLogger();
		}
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		
		Device device = Display.getCurrent ();
		_backgroundColor = device.getSystemColor(SWT.COLOR_GRAY);
	}
	
	protected IProject getProject()
	{
		return _editor.getProject();
	}
	
	protected DbConfigEditor getDbConfigEditor()
	{
		return _editor ;
	}
	
	protected void logException(Throwable e)
	{
		_logger.logStackTrace(e);
	}
	protected void logError(String s)
	{
		_logger.error(s);
	}
	protected void log(String s)
	{
		_logger.log(s);
	}
	protected void log(Object o, String s)
	{
		_logger.log(o,s);
	}
	protected TelosysToolsLogger getLogger()
	{
		return _logger ;
	}
	
	protected void setDirty()
	{
		//RepositoryEditor repEditor = (RepositoryEditor) getEditor();
		_editor.setDirty();
	}
	
//	protected ProjectConfig getProjectConfig()
//	{
//		ProjectConfig config = _editor.getProjectConfig();
//		if ( config == null )
//		{
//			MsgBox.error("ProjectConfig is null");
//		}
//		return config ;
//	}
	
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
	protected Color getBackgroundColor()
	{
		return _backgroundColor;
	}
	
	protected void createFormContent(IManagedForm managedForm) 
	{
		log(this, "createFormContent(..)..." );
		super.createFormContent(managedForm);
		setBackgroundColor();
	}
}
