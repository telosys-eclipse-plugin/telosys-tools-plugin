package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPageForGeneration;
import org.telosys.tools.generic.model.Entity;

/**
 * Editor Page 3 : "Bulk Generation"
 * 
 */
///* package */ class ModelEditorPage1 extends ModelEditorPage {
/* package */ class ModelEditorPageCodeGeneration extends AbstractModelEditorPageForGeneration {

	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	//public RepositoryEditorPage2(FormEditor editor, String id, String title, List<TargetDefinition> initialTargetsList ) {
	public ModelEditorPageCodeGeneration(FormEditor editor, String id, String title ) {
		super(editor, id, title);
	}

	
	//----------------------------------------------------------------------------------------------
	
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
	}
	
	@Override
	public void createEntitiesTableColumns(Table table) {
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		//--- A single column for "ENtity class name"
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity class name");
		col.setWidth(400);
	}
	
	//----------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------	
	/**
	 * Populates the list of entities ( left side table )
	 */
//	private void populateEntitiesTable()
//	{
//		log(this, "populateEntitiesTable()");
//		
////		ModelEditor modelEditor = (ModelEditor) getEditor();
////		Model model = modelEditor.getModel();
//		Model model = getModel();
//		if ( model == null ) {
//			MsgBox.error("Model is null !");
//			return ;
//		}
//		
//		List<Entity> entities = model.getEntities();
//		if ( entities != null )
//		{
//			for ( Entity entity : entities ) { 
////				String tableName = entity.getDatabaseTable() ; // v 3.0.0
////				if ( entity.getWarnings() != null && entity.getWarnings().size() > 0 ) {
////					tableName = "(!) " + tableName;
////				}
//				String entityClassName = entity.getClassName(); 
//				
//				if ( entityClassName == null ) entityClassName = "???" ;
//				
//                //--- Create the row content 
////                String[] row = new String[] { tableName, entityClassName };
//                String[] row = new String[] { entityClassName };
//				
//                //--- Create the TableItem and set the row content 
//            	TableItem tableItem = new TableItem(_tableEntities, SWT.NONE );
//                tableItem.setChecked(false);                
//                tableItem.setText(row);                
//                tableItem.setData( entityClassName ); 
//			}
//		}
//	}
	
	public void populateEntitiesTable(Table table, List<Entity> entities) {
		if ( entities != null )
		{
			for ( Entity entity : entities ) { 
	//			String tableName = entity.getDatabaseTable() ; // v 3.0.0
	//			if ( entity.getWarnings() != null && entity.getWarnings().size() > 0 ) {
	//				tableName = "(!) " + tableName;
	//			}
				String entityClassName = entity.getClassName(); 
				
				if ( entityClassName == null ) entityClassName = "???" ;
				
	            //--- Create the row content 
	//            String[] row = new String[] { tableName, entityClassName };
	            String[] row = new String[] { entityClassName };
				
	            //--- Create the TableItem and set the row content 
	        	TableItem tableItem = new TableItem(table, SWT.NONE );
	            tableItem.setChecked(false);                
	            tableItem.setText(row);                
	            tableItem.setData( entityClassName ); 
			}
		}
	}

}