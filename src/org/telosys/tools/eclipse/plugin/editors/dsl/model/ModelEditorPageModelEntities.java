package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.dsl.parser.ParserUtil;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;


/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
/* package */  class ModelEditorPageModelEntities extends AbstractModelEditorPage 
{

//	private boolean  _bPopulateInProgress = false ;
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title 
	 */
	public ModelEditorPageModelEntities(FormEditor editor, String id, String title ) {
		super(editor, id, title);
	}
	
	//----------------------------------------------------------------------------------------------
//	protected boolean isPopulateInProgress() {
//		return _bPopulateInProgress ;
//	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..)..." );

		//--- Set a LAYOUT to the BODY
		GridLayout bodyLayout = new GridLayout();	
		bodyLayout.numColumns = 2 ;
		bodyLayout.makeColumnsEqualWidth = false ;
		
		Composite scrolledFormBody = initAndGetFormBody(managedForm, bodyLayout);
		
		//---------------------------------------------------------------
		// Line 0 - Column 1 : The page title
		//---------------------------------------------------------------
		//---------------------------------------------------------------
		// Line 0 - Columns 1 & 2 (span) : The page title
		//---------------------------------------------------------------
		GridData gdTitle = new GridData(GridData.FILL_HORIZONTAL);
		gdTitle.horizontalSpan = 2;		
		Label labelTitle = Util.setPageTitle(scrolledFormBody, this.getTitle() ) ; // Title defined in the constructor
		labelTitle.setLayoutData(gdTitle);
		
		Table entitiesTable = createEntitiesTable(scrolledFormBody);
		
		populateEntities(entitiesTable) ;
	}
	
	//----------------------------------------------------------------------------------------------
	private Table createEntitiesTable(Composite composite) {
		// Table style
		// SWT.CHECK : check box in the first column of each row
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
//						| SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK ;
		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
		| SWT.FULL_SELECTION | SWT.HIDE_SELECTION ;
		
		final Table table = new Table(composite, iTableStyle);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Table columns : call the specialized method implemented in the descendant class
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		//--- A single column for "ENtity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity file name");
		col.setWidth(400);
		
		table.addMouseListener(new MouseListener() {
			 
			// Double-click => Open the entity file in the "Entity editor"
            public void mouseDoubleClick(MouseEvent arg0) {
                if (table.getSelectionCount() > 0) {
                	TableItem[] tableItemsSelected = table.getSelection();
                	String entityAbsoluteFilePath = "?";
                	if ( tableItemsSelected != null && tableItemsSelected.length == 1 ) {
                		IProject project = getProject();
                		entityAbsoluteFilePath = (String) tableItemsSelected[0].getData() ;
                		FileEditorUtil.openEntityFileInEditor(project, entityAbsoluteFilePath);
                	}
//                    MsgBox.info("mouseDoubleClick", "index : " + table.getSelectionIndex() 
//                    		+ " \n" + entityAbsoluteFilePath );
//                    return;
                }
            }
 
            public void mouseDown(MouseEvent arg0) {
            }
 
            public void mouseUp(MouseEvent arg0) {
            }
        });
		
		GridData gdTableEntities = new GridData();
		gdTableEntities.heightHint = 360 ;
		gdTableEntities.widthHint  = 420 ;
		table.setLayoutData(gdTableEntities);

		return table;
	}

	//----------------------------------------------------------------------------------------------
	private void populateEntities(Table table) {
		log(this, "populateEntities(table)");
		String fileAbsolutePath = this.getModelEditor().getFileAbsolutePath();
    	File modelFile = new File(fileAbsolutePath);
    	List<String> entitiesFileNames = ParserUtil.getEntitiesAbsoluteFileNames(modelFile);

		for ( String entityFile : entitiesFileNames ) {
			File file = new File(entityFile);
            //--- Create the TableItem and set the row content 
        	TableItem tableItem = new TableItem(table, SWT.NONE );
            tableItem.setChecked(false);
            tableItem.setText( file.getName() );
            tableItem.setData( entityFile );
            
            //tableItem.addListener(eventType, listener)
		}
	}
	
}