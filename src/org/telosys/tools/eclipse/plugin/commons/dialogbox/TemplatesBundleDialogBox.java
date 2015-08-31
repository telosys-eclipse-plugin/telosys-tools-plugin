package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TemplatesBundleDialogBox extends Dialog
{
	private final static int DIALOG_BOX_WIDTH  = 400 ;
	private final static int DIALOG_BOX_HEIGHT = 200 ;

	private final static int COMBO_WIDTH  = 260 ;
	private final static int COMBO_HEIGHT =  24 ;
	private final static int COMBO_VISIBLE_ITEMS =  10 ;
	
//	private final static int TEXT_WIDTH  = DIALOG_BOX_WIDTH - 40 ;
//	private final static int TEXT_HEIGHT = 20 ;
		
	//--- Widgets
	private Combo combo ;

	//--- Data
	private final List<String> comboItems ;
	private final String       bundlesLocation ;
	private final int          initialItem ;

	//--- Result
	private String selectedItem = null ;

	public TemplatesBundleDialogBox(Shell parentShell, List<String> items, String bundlesLocation, int initialItem ) 
	{
		super(parentShell);
		this.comboItems = items ;
		this.bundlesLocation = bundlesLocation ;
		this.initialItem = initialItem ;
	}

	@Override
	protected void configureShell(Shell newShell) {
		// Override this Window method in order to set the title 
		super.configureShell(newShell);
		newShell.setText("Select a template bundle");
	}
	
	@Override
	protected Point getInitialSize() {
		// Override this Dialog method in order to set the size 
		return new Point(DIALOG_BOX_WIDTH, DIALOG_BOX_HEIGHT);
	}

	@Override
	protected Control createDialogArea(Composite parent) 
	{
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite group = new Composite(container, SWT.NONE );
		
//		_gridComposite.setLayout(new GridLayout(NB_COL, false));
//		_gridComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false) ;
		//group.setLayout(     new GridLayout(1, false) ) ;
		//group.setLayoutData( new GridData(SWT.BEGINNING, SWT.CENTER, false, false) );
		
		//GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false) ;

		group.setLayout(new RowLayout(SWT.VERTICAL));
		
    	combo = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		//combo.setSize(COMBO_WIDTH, COMBO_WIDTH);
		combo.setLayoutData( new RowData(COMBO_WIDTH, COMBO_HEIGHT));
		combo.setVisibleItemCount(COMBO_VISIBLE_ITEMS); // Show a list of N items 

        combo.addSelectionListener( new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event)
            {
//                Combo combo = (Combo) event.widget ;
//        		String[] items = combo.getItems();
//        		selectedItem = items[ combo.getSelectionIndex() ] ;
        		updateSelectedItem();
            }
        });
		
		// Populate combo
		for ( String s : comboItems ) {
			combo.add(s); 
		}
		if ( initialItem < comboItems.size() ) {
			combo.select(initialItem);
//    		String[] items = combo.getItems();
//    		selectedItem = items[ combo.getSelectionIndex() ] ;
    		updateSelectedItem();
		}
		
		Label label ;
		
		//--- Filler
		label = new Label(group, SWT.NONE);
		label.setLayoutData( new RowData(1, 20)); // Filler height
		
		//--- Template bundles location
		label = new Label(group, SWT.NONE);
		label.setText("The template bundles are the folders located in :");
		//label.setLayoutData( new RowData(TEXT_WIDTH, TEXT_HEIGHT));
		label.setLayoutData( new RowData());

		label = new Label(group, SWT.BORDER);
		label.setText(bundlesLocation);
		//label.setLayoutData( new RowData(TEXT_WIDTH, TEXT_HEIGHT));
		label.setLayoutData( new RowData());
		
		return container;
	}
	
	/**
	 * Returns the selected item text (never null)
	 * @return
	 */
	public String getSelectedItem() {
		return selectedItem ;
		// Cannot use combo widget here : "Widget is disposed"
	}
	
	private void updateSelectedItem() {
		String[] items = combo.getItems();
		selectedItem = items[ combo.getSelectionIndex() ] ;
	}	
}
