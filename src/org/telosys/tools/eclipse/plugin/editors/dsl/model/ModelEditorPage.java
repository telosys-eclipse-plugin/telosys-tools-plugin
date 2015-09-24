package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.generic.model.Model;

public abstract class ModelEditorPage extends FormPage 
{
	
	private ModelEditor        _editor = null ; // Ref on the Editor this page belongs to
	
	private TelosysToolsLogger _logger = null ;
	
	private Color              _backgroundColor = null ;
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor 
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ModelEditorPage(FormEditor editor, String id, String title) 
	{
		super(editor, id, title);		
		//super(editor, id, null); // ERROR if title is null
		_editor = (ModelEditor) editor;
		if ( null == _editor ) {
			MsgBox.error("RepositoryEditor is null");
		}
		
		//--- Init the logger
		_logger = _editor.getLogger();		
		if ( null == _logger ) {
			_logger = new ConsoleLogger();
		}
		log(this, "ancestor constructor(.., '"+id+"', '"+ title +"')..." );
		
//		//--- Init the default background color // ERROR / Eclipse !!!
//		Display display = new Display();// ERROR / Eclipse !!!
//		_backgroundColor = display.getSystemColor(SWT.COLOR_GRAY);
		Device device = Display.getCurrent ();
		_backgroundColor = device.getSystemColor(SWT.COLOR_GRAY);
	}
	
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		log(this, "createFormContent(..)..." );
		super.createFormContent(managedForm);
		setBackgroundColor();
	}

	//----------------------------------------------------------------------------------------------
	protected IProject getProject() {
		return _editor.getProject();
	}
	//----------------------------------------------------------------------------------------------
	protected Model getModel() {
		return _editor.getModel();
	}
	//----------------------------------------------------------------------------------------------
	protected ModelEditor getModelEditor() {
		return _editor ;
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
		_editor.setDirty();
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
		TelosysToolsCfg config = _editor.getProjectConfig();
		if ( config == null )
		{
			MsgBox.error("ProjectConfig is null");
		}
		return config ;
	}
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
	protected Color getBackgroundColor() {
		return _backgroundColor;
	}
	//----------------------------------------------------------------------------------------------
	protected Composite initAndGetFormBody(IManagedForm managedForm, Layout layout) {
		ScrolledForm scrolledForm = managedForm.getForm();		
		Composite scrolledFormBody = scrolledForm.getBody(); 
		log(this, "- body class = " + scrolledFormBody.getClass() );
		// scrolledFormBody.getClass() --> org.eclipse.ui.forms.widgets.LayoutComposite
		
		Layout currentLayout = scrolledFormBody.getLayout();			
		if ( currentLayout != null ) {
			log(this, "- body layout class = " + currentLayout.getClass() );
		}
		else {
			log(this, "- body layout class = NO LAYOUT ! ");
		}
		// No Layout for the body at this moment
		
		scrolledFormBody.setLayout( layout );
		
		return scrolledFormBody;
	}
	
}
