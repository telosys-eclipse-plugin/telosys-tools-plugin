package org.telosys.tools.eclipse.plugin.commons.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.editors.dbrep.RepositoryEditor;

public class RefreshButton {

	public final static int  BUTTON_HEIGHT =  26 ;
	public final static int  BUTTON_WIDTH  =  90 ; // 80 ;
	
	private final Button           _button ;
	private final RepositoryEditor _editor ;
	
	/**
	 * Creates a "Refresh" button in the given Composite with a "GridData" LayoutData
	 * @param parent
	 * @param editor
	 */
	public RefreshButton(Composite parent, RepositoryEditor editor) {
		super();
		_editor = editor ; // v 2.0.7
		_button = new Button(parent, SWT.NONE);
		
		GridData gridData = new GridData (BUTTON_WIDTH, BUTTON_HEIGHT) ;
		// no effect
		//gridData.grabExcessVerticalSpace = false ; // default is false
		//gridData.minimumHeight = 2 ;
		//gridData.verticalIndent= 0 ;
//		gridData.verticalAlignment   = SWT.BEGINNING ;
//		gridData.horizontalAlignment = SWT.BEGINNING ;
		gridData.verticalAlignment   = GridData.BEGINNING ;
		gridData.horizontalAlignment = GridData.BEGINNING ;
		init(gridData);
	}
	
	/**
	 * Creates a "Refresh" button in the given Composite with a specific LayoutData
	 * @param parent
	 * @param layoutData
	 * @param editor
	 */
	public RefreshButton(Composite parent, Object layoutData, RepositoryEditor editor) {
		super();

		_editor = editor ; // v 2.0.7
		
		_button = new Button(parent, SWT.NONE);
		
		init(layoutData);
	}
	
	private void init(Object layoutData) {
		_button.setText("Refresh");
		_button.setToolTipText("Reload bundles and templates from the filesystem");

		_button.setImage( PluginImages.getImage(PluginImages.REFRESH ) );
		
		_button.setLayoutData ( layoutData );
		
		_button.addSelectionListener(new SelectionListener()  // v 2.0.7
		{
	        public void widgetSelected(SelectionEvent arg0)
	        {
	        	//--- Reload the targets list
	        	_editor.refreshAllTargetsTablesFromConfigFile();
	        }
	        public void widgetDefaultSelected(SelectionEvent arg0)
	        {
	        }
	    });
		
	}
	
	public Button getButton() {
		return _button ;
	}
	
	public void setEnabled(boolean enabled) {
		_button.setEnabled(enabled);
	}
	
//	public void addSelectionListener(SelectionListener listener) {
//		_button.addSelectionListener( listener ) ;
//	}
}
