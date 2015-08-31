package org.telosys.tools.eclipse.plugin.commons.widgets;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.TemplateBundleUtil;
import org.telosys.tools.eclipse.plugin.editors.dbrep.RepositoryEditor;

public class BundleComboBox {

	public final static int COMBO_WIDTH  = 260 ;
	public final static int COMBO_HEIGHT =  24 ;
	private final static int COMBO_VISIBLE_ITEMS =  16 ;
	
	private final RepositoryEditor _editor ;
	private final Combo            _combo ;
	
//	private String _selectedItem = "";
	
	public BundleComboBox(Composite parent, RepositoryEditor editor) {
		super();

		_editor = editor ; 
		
    	_combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		//_combo.setSize(COMBO_WIDTH, COMBO_HEIGHT);
		GridData gridData = new GridData(COMBO_WIDTH, COMBO_HEIGHT);
		_combo.setLayoutData( gridData );

		_combo.setVisibleItemCount(COMBO_VISIBLE_ITEMS); // Show a list of N items 

        _combo.addSelectionListener( new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event)
            {
            	//--- Update selected item
        		String[] items = _combo.getItems();
        		String selectedBundle = items[ _combo.getSelectionIndex() ] ;
        		
				if ( StrUtil.different( selectedBundle, _editor.getCurrentBundleName() )) {
					// only if the bundle name has changed : to avoid refresh (visual list effect) if unchanged
					_editor.setCurrentBundleName(selectedBundle);
					_editor.refreshAllTargetsTablesFromConfigFile();					
				}
            }
        });
        
		
//		//--- Select initial item 
//		if ( initialItem >= 0 && initialItem < bundles.size() ) {
//			_combo.select(initialItem);
//    		updateSelectedItem();
//		}
//		else {
//			MsgBox.error("Combobox creation error : invalid item " + initialItem );
//		}

	}
	
//	public Combo getCombo() {
//		return _combo ;
//	}

//	private void updateSelectedItem() {
//		String[] items = _combo.getItems();
//		_selectedItem = items[ _combo.getSelectionIndex() ] ;
//	}	
	
//	public String getSelectedItem() {
//		return _selectedItem ;
//	}
	
	/**
	 * Populates the combo box from the project's bundles (workspace folders) <br>
	 * and select the editor's current bundle if any 
	 */
	public void refresh() {
		
		//--- Populate combo items 
        IProject eclipseProject = _editor.getProject() ;
        List<String> bundles = TemplateBundleUtil.getBundlesFromTemplatesFolder(eclipseProject);
        _combo.removeAll();
		for ( String s : bundles ) {
			_combo.add(s); 
		}
		
		//--- Select the bundle currently selected in the editor
		String currentBundle = _editor.getCurrentBundleName() ;
		if ( currentBundle != null ) {
			currentBundle = currentBundle.trim() ;
			for ( int index = 0 ; index < _combo.getItemCount() ; index++ ) {
				if ( currentBundle.equals( _combo.getItem(index) ) ) {
					_combo.select(index);
					break;
				}
			}
		}
	}
}
