package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.FileEditorUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;


/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
/* package */  class ModelEditorPageModelEntities extends AbstractModelEditorPage 
{

	private Table _entitiesTable = null ;
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
	
	protected Shell getShell() {
		return _entitiesTable.getDisplay().getActiveShell();
	}
	
	//----------------------------------------------------------------------------------------------
	protected void doOpenEntityInEditor(String entityAbsoluteFilePath) {
		FileEditorUtil.openEntityFileInEditor( getProject(), entityAbsoluteFilePath);
	}
	//----------------------------------------------------------------------------------------------
	protected void doNewEntity() {
		DialogBoxForNewEntity dialogBox = new DialogBoxForNewEntity( getShell() );
		if ( dialogBox.open() == Window.OK ) {
			String entityName = dialogBox.getEntityName();
			try {
				File entityFile = DslModelUtil.createNewEntity(getModelFile(), entityName);
				// refresh the Eclipse workspace (add the file in workspace view)
				EclipseWksUtil.refresh(entityFile); 
				// re-parse the model and refresh the entities in "entities table"
				reloadEntities();
				// open the new entity in the editor
				doOpenEntityInEditor(entityFile.getAbsolutePath());
			} catch (Exception e) {
				MsgBox.error("Cannot create entity '" + entityName + "'", e);
			}
		}
	}
	//----------------------------------------------------------------------------------------------
	protected void doDeleteEntity(String entityAbsoluteFilePath) {
		File entityFile = new File ( entityAbsoluteFilePath );
		String entityName = DslModelUtil.getEntityName( entityFile );
		if ( MsgBox.confirm("Confirm delete", 
				"Do you realy want to delete entity '" + entityName + "' ?" ) ) {
			boolean deleted = entityFile.delete() ;
			if ( deleted ) {
				// refresh the Eclipse workspace (removes the file in workspace view and close editor if any)
				EclipseWksUtil.refresh(entityFile); 
				// re-parse the model and refresh the entities in "entities table"
				reloadEntities();
			}
			else {
				MsgBox.error( "Cannot delete entity '" + entityName + "' !");
			}
		}
	}
	//----------------------------------------------------------------------------------------------
	protected void doRenameEntity(String entityAbsoluteFilePath) {
		String currentEntityName = DslModelUtil.getEntityName( new File(entityAbsoluteFilePath) );
		DialogBoxForRenameEntity dialogBox = new DialogBoxForRenameEntity( getShell(), currentEntityName );
		if ( dialogBox.open() == Window.OK ) {
			String newEntityName = dialogBox.getNewEntityName();
			// rename the entity file
			DslModelUtil.renameEntity(new File(entityAbsoluteFilePath), newEntityName);
			// refresh the Eclipse workspace (removes the file in workspace view and close editor if any)
			
			// TODO
			File modelFolder = DslModelUtil.getModelFolder(getModelFile());
			EclipseWksUtil.refresh(modelFolder); 
			// re-parse the model and refresh the entities in "entities table"
			reloadEntities();
		}
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
		
		_entitiesTable = createEntitiesTable(scrolledFormBody);
		
		populateEntities() ;
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Return the current selected TableItem in the entities table <br>
	 * Return null if no entity is selected or if more than one entity are selected
	 * @return
	 */
	protected TableItem getSelectedTableItem() {
		TableItem selectedItems[] = _entitiesTable.getSelection();
		if ( selectedItems != null && selectedItems.length == 1 ) {
			return selectedItems[0] ;
		}
		return null ;
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

		//--- Column for "Entity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity file name");
		col.setWidth(200);
		
		//--- Column for "Entity error"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity parsing error");
		col.setWidth(500);
		
//		Menu menu = new Menu(table);
//		table.setMenu(menu);
//		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
//		menuItem.setText("Open");
		
		createMenu(table);
		
		table.addMouseListener(new MouseListener() {
			 
			// Double-click => Open the entity file in the "Entity editor"
            public void mouseDoubleClick(MouseEvent arg0) {
                if (table.getSelectionCount() > 0) {
                	TableItem[] tableItemsSelected = table.getSelection();
                	String entityAbsoluteFilePath = "?";
                	if ( tableItemsSelected != null && tableItemsSelected.length == 1 ) {
                		//IProject project = getProject();
                		entityAbsoluteFilePath = (String) tableItemsSelected[0].getData() ;
                		//FileEditorUtil.openEntityFileInEditor(project, entityAbsoluteFilePath);
                		doOpenEntityInEditor(entityAbsoluteFilePath);
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

		table.addListener(SWT.MenuDetect, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// Event widget is "Table"
				//MsgBox.info( "Event widget class : " + event.widget.getClass());
                TableItem tableItem = getTableItemClicked(table, event);
                if ( tableItem == null ) {
                	event.doit = false ;
                }
			}
		});
		
		GridData gdTableEntities = new GridData();
		gdTableEntities.heightHint = 360 ;
		gdTableEntities.widthHint  = 600 ;
		table.setLayoutData(gdTableEntities);

		return table;
	}

	//----------------------------------------------------------------------------------------------
	private Menu createMenu(Table table) {
		Menu menu = new Menu(table);
		table.setMenu(menu);
		menu.setData(table);
		
		//IProject project = getProject();
		
		createMenuItem(menu, null, "New", 
				new TableContextMenuListener(this, TableContextMenuListener.NEW) );
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		createMenuItem(menu, null, "Open", 
				new TableContextMenuListener(this, TableContextMenuListener.OPEN) );

		createMenuItem(menu, null, "Rename", 
				new TableContextMenuListener(this, TableContextMenuListener.RENAME) );
		
//		createMenuItem(menu, PluginImages.getImage(PluginImages.ERROR), "Delete", 
		createMenuItem(menu, null, "Delete", 
				new TableContextMenuListener(this, TableContextMenuListener.DELETE) );
		
		return menu ;
	}

	//----------------------------------------------------------------------------------------------
	private MenuItem createMenuItem(Menu menu, Image image, String text, Listener listener) {
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(text);
		if ( image != null ) {
			menuItem.setImage(image);
		}
		menuItem.addListener(SWT.Selection, listener);
		menuItem.setData(menu.getData());
		return menuItem ;
	}
	//----------------------------------------------------------------------------------------------
	private TableItem getTableItemClicked(Table table, Event event) {
		// calculate click offset within table area  
		Point point = Display.getDefault().map(null, table, new Point(event.x, event.y)); 
		TableItem tableItem = table.getItem(point);
		//log("getTableItem : x = " + point.x + " / y = " + point.y + " / table item = " + tableItem );
		return tableItem ;
	}
	//----------------------------------------------------------------------------------------------
	public void reloadEntities() {
		ModelEditor modelEditor = (ModelEditor) getModelEditor();
		modelEditor.loadModel();
		populateEntities();
	}
	//----------------------------------------------------------------------------------------------
	private void populateEntities() {
		log(this, "populateEntities(table)");
		
		_entitiesTable.removeAll();
		
		ModelEditor modelEditor = (ModelEditor) getModelEditor();
		
    	List<String> entitiesFileNames = modelEditor.getEntitiesAbsoluteFileNames();

    	Map<String,String> entitiesErrors = modelEditor.getEntitiesErrors();
		for ( String entityFile : entitiesFileNames ) {
			String entityFileName = (new File(entityFile)).getName() ;
			String entityError = null ;
			String imageId = PluginImages.ENTITY_FILE ;
			if ( entitiesErrors != null ) {
				entityError = entitiesErrors.get(entityFileName);
				if ( entityError != null ) {
					imageId = PluginImages.ERROR ;
				}
			}
			if ( entityError == null ) {
				entityError = "" ;
			}
			//--- Create the TableItem and set the row content 
			
            String[] row = new String[] { entityFileName, entityError };

        	TableItem tableItem = new TableItem( _entitiesTable, SWT.NONE );
            tableItem.setText( row );
            tableItem.setChecked(false);
            tableItem.setData( entityFile );
            
            tableItem.setImage( PluginImages.getImage(imageId) );
            //tableItem.addListener(eventType, listener)
            //tableItem.addListener(eventType, listener)
		}
	}
	
}