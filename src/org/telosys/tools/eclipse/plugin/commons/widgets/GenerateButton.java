package org.telosys.tools.eclipse.plugin.commons.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;

public class GenerateButton {

	public final static int  BUTTON_HEIGHT =  26 ; // 28
	public final static int  BUTTON_WIDTH  = 120 ;
	
	private final Button _buttonGenerate ;
	
	public GenerateButton(Composite parent) {
		super();

		_buttonGenerate = new Button(parent, SWT.NONE);
		_buttonGenerate.setText("Generate");

		_buttonGenerate.setImage( PluginImages.getImage(PluginImages.GENERATE ) );
		
		_buttonGenerate.setLayoutData ( new GridData (BUTTON_WIDTH, BUTTON_HEIGHT) );
	}
	
	public Button getButton() {
		return _buttonGenerate ;
	}
	
	public void setEnabled(boolean enabled) {
		_buttonGenerate.setEnabled(enabled);
	}
	
	public void addSelectionListener(SelectionListener listener) {
		_buttonGenerate.addSelectionListener( listener ) ;
	}
}
