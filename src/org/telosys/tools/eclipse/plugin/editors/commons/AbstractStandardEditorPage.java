package org.telosys.tools.eclipse.plugin.editors.commons;

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
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public abstract class AbstractStandardEditorPage extends FormPage {

	private final AbstractStandardEditor  _standardEditor ; // Ref on the Editor this page belongs to
	
	private final TelosysToolsLogger      _logger ;
	
	private final Color                   _backgroundColor  ;

	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractStandardEditorPage(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		//super(editor, id, null); // ERROR if title is null
		if ( editor == null ) {
			MsgBox.error("FormEditor is null");
		}
		if ( editor instanceof AbstractStandardEditor ) {
			_standardEditor = (AbstractStandardEditor) editor;
		}
		else {
			_standardEditor = null ;
			MsgBox.error("FormEditor is not an instance of StandardEditor");
		}
		
		//--- Init the logger
		TelosysToolsLogger editorLogger = _standardEditor.getLogger();		
		if ( editorLogger != null ) {
			_logger = editorLogger;
		}
		else {
			_logger = new ConsoleLogger();
		}
		log(this, "ancestor constructor(.., '"+id+"', '"+ title +"')..." );
		
//		//--- Init the default background color // ERROR / Eclipse !!!
//		Display display = new Display();// ERROR / Eclipse !!!
//		_backgroundColor = display.getSystemColor(SWT.COLOR_GRAY);
		Device device = Display.getCurrent();
		_backgroundColor = device.getSystemColor(SWT.COLOR_GRAY);		
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(String s) {
		if ( _logger != null ) {_logger.log(s); };
	}
	
	//----------------------------------------------------------------------------------------------
	public void log(Object o, String s) {
		if ( _logger != null ) { _logger.log(o,s); } ;
	}
	//----------------------------------------------------------------------------------------------
	public TelosysToolsLogger getLogger() {
		return _logger ;
	}

	//----------------------------------------------------------------------------------------------
	public void setDirty() {
		_standardEditor.setDirty();
	}
	
	//----------------------------------------------------------------------------------------------
	protected AbstractStandardEditor getStandardEditor() {
		return _standardEditor;
	}
	//----------------------------------------------------------------------------------------------
	protected IProject getProject() {
		return _standardEditor.getProject();
	}

	//----------------------------------------------------------------------------------------------
	protected void setBackgroundColor() {
		Control pageControl = this.getPartControl();
		if ( pageControl != null ) {
			Display display = pageControl.getDisplay();	
			if ( display != null ) {
//				_backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
//				pageControl.setBackground(_backgroundColor ) ;
				Color color = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
				pageControl.setBackground(color) ;
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
	
	public TelosysToolsCfg getProjectConfig () {
//		PluginLogger.log(this, "getProjectConfig()..." );
//		return ProjectConfigManager.loadProjectConfig( getProject() ); // v 3.0.0
		return _standardEditor.getProjectConfig();
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