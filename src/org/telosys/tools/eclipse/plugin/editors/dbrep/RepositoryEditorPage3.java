package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.ConsoleLogger;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.commons.listeners.ListenerForTableToolTip;
import org.telosys.tools.eclipse.plugin.commons.listeners.OpenTemplateFileInEditor;
import org.telosys.tools.eclipse.plugin.commons.widgets.BundleComboBox;
import org.telosys.tools.eclipse.plugin.commons.widgets.GenerateButton;
import org.telosys.tools.eclipse.plugin.commons.widgets.GridPanel;
import org.telosys.tools.eclipse.plugin.commons.widgets.RefreshButton;
import org.telosys.tools.eclipse.plugin.commons.widgets.SelectDeselectButtons;
import org.telosys.tools.eclipse.plugin.commons.widgets.TargetsButton;
import org.telosys.tools.eclipse.plugin.generator.GenerationTaskWithProgress;
import org.telosys.tools.eclipse.plugin.settings.SettingsManager;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * Editor Page 3 : "Bulk Generation"
 * 
 */
/* package */ class RepositoryEditorPage3 extends RepositoryEditorPage {

//	private final static int RIGHT_PART_WIDTH = 484 ;
	private final static int TABLE_HEIGHT = 380 ;
	
	private Table  _tableEntities = null ;
	
	private Table  _tableTargets = null ;
	
	private BundleComboBox  _comboBundles = null ;
	private Button          _checkboxStaticResources = null ;
	
	//private final List<TargetDefinition> initialTargetsList ; // v 2.0.7
	private List<TargetDefinition> _resourcesTargets = null ; // v 2.0.7
	
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	//public RepositoryEditorPage2(FormEditor editor, String id, String title, List<TargetDefinition> initialTargetsList ) {
	public RepositoryEditorPage3(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		//super(editor, id, null); // ERROR if title is null
		
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		//this.initialTargetsList = initialTargetsList ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..)..." );
		Control pageControl = getPartControl();
		
		if ( pageControl != null ) {
			log(this, "createFormContent(..) : getPartControl() != null " );
		}
		else {
			log(this, "createFormContent(..) : getPartControl() is null !!! " );
			return ;
		}
		
//		if ( pageControl instanceof Composite )
//		{
//			PluginLogger.log(this, "- pageControl is a Composite  " );
//			PluginLogger.log(this, "- pageControl class = " + pageControl.getClass() );
//			
//			Composite pageComposite = (Composite) pageControl ;
//			Layout layout = pageComposite.getLayout();			
//			PluginLogger.log(this, "- pageControl layout class = " + layout.getClass() );
//		}
//		else
//		{
//			PluginLogger.log(this, "- pageControl() is NOT a Composite !!! " );
//		}

		// What do we have here ?
		// * pageControl (Composite)
		//  . class  : org.eclipse.ui.forms.widgets.ScrolledForm ( see API JavaDoc )
		//  . layout : org.eclipse.swt.custom.ScrolledCompositeLayout
		// * body 
		//  . class  : org.eclipse.ui.forms.widgets.LayoutComposite ( no API doc ! )
		//  . layout : none
		//
		/* Example from API doc :
		  ScrolledForm form = toolkit.createScrolledForm(parent);
		  form.setText("Sample form");
		  form.getBody().setLayout(new GridLayout());
		  toolkit.createButton(form.getBody(), "Checkbox", SWT.CHECK);
		*/
		
		ScrolledForm form = managedForm.getForm();
		// Page title 
		// form.setText( repEditor.getDatabaseTitle() );
		
		Composite scrolledFormBody = form.getBody();
		log(this, "- body class = " + scrolledFormBody.getClass() );
		
		GridLayout formGridLayout = new GridLayout(2, false); // Grid 2 columns

		// marginWidth specifies the number of pixels of horizontal margin 
		// that will be placed along the left and right edges of the layout. The default value is 5.
		//bodyLayout.marginWidth = 20 ;
		formGridLayout.marginWidth = RepositoryEditor.LAYOUT_MARGIN_WIDTH ;
		formGridLayout.verticalSpacing = 0;
		
		scrolledFormBody.setLayout( formGridLayout );
		
		//---------------------------------------------------------------
		// Row 1 - Column 1 : Title
		//---------------------------------------------------------------
		createFormCellRow1Col1(scrolledFormBody);
		
		//---------------------------------------------------------------
		// Row 1 - Column 2 : Buttons "Refresh" and "Generate" (ver 2.0.7)
		//---------------------------------------------------------------
		createFormCellRow1Col2(scrolledFormBody);
		
		//---------------------------------------------------------------
		// Row 2 - Column 1 : Entities panel
		//---------------------------------------------------------------
		createFormCellRow2Col1(scrolledFormBody);
		
		//---------------------------------------------------------------
		// Row 2 - Column 2 : Targets panel
		//---------------------------------------------------------------		
		createFormCellRow2Col2(scrolledFormBody);
		
		//---------------------------------------------------------------
		// Populate the 2 tables 
		//---------------------------------------------------------------
		populateEntitiesTable();
		
		//populateTargetsTable();
		//populateTargetsTable(initialTargetsList); // v 2.0.7
		_comboBundles.refresh(); 

		log(this, "createFormContent(..) - END." );
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Create the cell (1:1) : Form title
	 * @param formBody
	 */
	private void createFormCellRow1Col1(Composite formBody) {
		//Label labelTitle = Util.setPageTitle(formBody, "Bulk generation" ) ;
		Label labelTitle = Util.setPageTitle(formBody, this.getTitle() ) ; // Title defined in the constructor
		
		GridData titleGridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		titleGridData.verticalIndent = 0 ;
//		titleGridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING ;
//		titleGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING ;
		titleGridData.verticalAlignment = GridData.BEGINNING ;
		titleGridData.horizontalAlignment = GridData.FILL ;
		//gdTitle.horizontalSpan = 2; // removed in v 2.0.7
		labelTitle.setLayoutData(titleGridData);
		
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Create the cell (1:2) : Buttons "Refresh", "Generate"
	 * @param formBody
	 */
	private void createFormCellRow1Col2(Composite formBody) {
		//Composite panelTopRight = new Composite(formBody, SWT.NONE | SWT.BORDER );
		Composite panelTopRight = new Composite(formBody, SWT.NONE );
		
		//--- Panel layout
		GridLayout gridLayout = new GridLayout(5, false) ; // 5 columns
		gridLayout.marginTop = 0;
		gridLayout.marginBottom = 0;
		gridLayout.verticalSpacing = 0 ;
		//gridLayout.marginWidth = 0 ;
		gridLayout.marginHeight = 0 ;
		gridLayout.horizontalSpacing = 0 ;	
		panelTopRight.setLayout(gridLayout);
		panelTopRight.setSize(120, 26);
		
		//--- Panel "layout data"
		GridData panelGridData = new GridData();
		panelGridData.verticalAlignment   = GridData.BEGINNING ;
		panelGridData.horizontalAlignment = GridData.FILL ; // All the cell width
		//panelGridData.horizontalAlignment = GridData.END ; // END = Right side
		panelGridData.verticalIndent = 0 ;
		panelTopRight.setLayoutData(panelGridData);
		
		//--- (1) Button "Refresh"
		new RefreshButton(panelTopRight, getRepositoryEditor());  // 1 button // v 2.0.7

		//--- (2) Filler 
		//int fillerWidth = RIGHT_PART_WIDTH - GenerateButton.BUTTON_WIDTH - RefreshButton.BUTTON_WIDTH ;
		int fillerWidth = 70 ;
		createFillerInGridLayout(panelTopRight, fillerWidth);

		//--- (3) Check box  
		_checkboxStaticResources = new Button(panelTopRight, SWT.CHECK) ;
		_checkboxStaticResources.setText("Copy static resources");
		_checkboxStaticResources.setToolTipText("Generation also copy bundle resources \ninto the project (must be done once)");
		
		//--- (4) Filler 
		createFillerInGridLayout(panelTopRight, fillerWidth);
		
		//--- (5) Button "Generate"
		GenerateButton generateButton = new GenerateButton(panelTopRight); // 1 button
		generateButton.addSelectionListener(new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	if ( confirmBulkGeneration() )
        		{
        	    	Shell shell = Util.cursorWait();
        	    	launchBulkGeneration();
        			Util.cursorArrow(shell);
        		}                
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
	}
	
	//----------------------------------------------------------------------------------------------
	private Label createFillerInGridLayout(Composite composite, int fillerWidth) {
		Label labelFiller = new Label(composite, SWT.NONE);
		labelFiller.setText("");
		GridData fillerGridData = new GridData();
		fillerGridData.horizontalAlignment = GridData.FILL ; // All the cell width
		fillerGridData.verticalIndent = 0 ;
		fillerGridData.widthHint = fillerWidth ; // Filler SIZE
		labelFiller.setLayoutData( fillerGridData );
		return labelFiller ;
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Create the cell (2:1) : Buttons "Select all/Unselect all" + Table of ENTITIES
	 * @param formBody
	 */
	private void createFormCellRow2Col1(Composite formBody) {
		
		log("createFormCellRow2Col1...");
		
		Composite panel = new Composite(formBody, SWT.NONE | SWT.BORDER );
		panel.setLayout(new GridLayout(1, false));
		//panel1.setSize(200, 100);
		GridData panelGridData = new GridData();
		panelGridData.verticalAlignment = SWT.TOP ;
		panelGridData.horizontalAlignment = GridData.FILL ;
		//gd2.widthHint = 300 ;
		panel.setLayoutData(panelGridData);
		
		SelectDeselectButtons buttons = createLeftPartHeader(panel) ;
		
		//--- Create the standard "SWT Table" for entities
//		_tableEntities = createEntitiesTable(panel);
//		//_table.setLocation(20, 20);
//		GridData gdTableEntities = new GridData();
//		gdTableEntities.heightHint = 344 ;
//		gdTableEntities.widthHint  = 420 ;
//		_tableEntities.setLayoutData(gdTableEntities);
		
		_tableEntities = createLeftPartTable(panel);
		
		buttons.setTable(_tableEntities);
		
		//return tableEntities ;
	}
	//----------------------------------------------------------------------------------------------
	private void createFormCellRow2Col2(Composite formBody) {
		Composite panel2 = new Composite(formBody, SWT.NONE | SWT.BORDER );
		
		panel2.setLayout(new GridLayout(1, false));
		GridData gdpanel2 = new GridData();
		//gdpanel2.verticalAlignment   = SWT.TOP ;
		gdpanel2.verticalAlignment   = GridData.FILL ;
		gdpanel2.horizontalAlignment = GridData.FILL ;
		//gd2.widthHint = 300 ;
		panel2.setLayoutData(gdpanel2);
		
    	//--- Header : Buttons Select/Deselect, bundles ComboBox, "Config" button 
    	SelectDeselectButtons selectDeselectButtons = createRightPartHeader(panel2);
    	
		//--- Table ( standard "SWT Table" for TARGETS )
		_tableTargets = createRightPartTableTargets(panel2);
		GridData gdTableTargets = new GridData();
		gdTableTargets.heightHint = TABLE_HEIGHT ;
		gdTableTargets.widthHint  = 460 ;
		_tableTargets.setLayoutData(gdTableTargets);

		selectDeselectButtons.setTable(_tableTargets);
	}
	//----------------------------------------------------------------------------------------------
	private SelectDeselectButtons createLeftPartHeader(Composite panel) {
		
		log("createLeftPartLine..");
		
		//--- Create the line with all buttons 
		GridPanel gridPanel = new GridPanel(panel, 2); // 2 columns

		SelectDeselectButtons buttons = new SelectDeselectButtons( gridPanel.getPanel(), gridPanel.getPanel() ) ; // 2 buttons
				
		return buttons ;
	}
	//----------------------------------------------------------------------------------------------
	private Table createLeftPartTable(Composite panel) {
		
		log("createLeftPartLine..");
		
		//--- Create the standard "SWT Table" for entities
		Table tableEntities = createEntitiesTable(panel);
		//_table.setLocation(20, 20);
		GridData gdTableEntities = new GridData();
		gdTableEntities.heightHint = TABLE_HEIGHT ;
		gdTableEntities.widthHint  = 420 ;
		tableEntities.setLayoutData(gdTableEntities);
				
		return tableEntities ;
	}

	//----------------------------------------------------------------------------------------------
	//----------------------------------------------------------------------------------------------
	private SelectDeselectButtons createRightPartHeader(Composite panel) {
		
		log("createRightPartLine..");
		
		//--- Create the line with all buttons 
		GridPanel gridPanel = new GridPanel(panel, 8); // 8 columns

		SelectDeselectButtons buttons2 = new SelectDeselectButtons( gridPanel.getPanel() ) ; // 2 buttons
				
		//--- First filler
		//gridPanel.addFiller(14);
		gridPanel.addFiller(78); // After reducing the Edit templates.cfg button
		
//    	//--- Label
//		Label label = new Label(gridPanel.getPanel(), SWT.NULL);
//    	//label = new Label(group1, SWT.BORDER );
//    	label.setText("Templates bundle : ");
//    	label.setSize(100, 20); // width, height
    	
		//--- Combobox ( bundles list )
		log("create BundleComboBox...");
		_comboBundles = new BundleComboBox(gridPanel.getPanel(), getRepositoryEditor() );
		log("BundleComboBox created.");
		
		//--- First filler
		gridPanel.addFiller(14);

		//--- Button "Edit templates.cfg"
		new TargetsButton(gridPanel.getPanel(), getRepositoryEditor(), getProject() );  // 1 button

		return buttons2 ;
	}
	
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		log(this, "init(..,..)..." );
		log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}
    
	//----------------------------------------------------------------------------------------------
	private Table createEntitiesTable(Composite composite)
	{
		log(this, "createTable(..)..." );
		
		// Table style
		// SWT.CHECK : check box in the first column of each row
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK ;
		
//		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
//		SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		
		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
						| SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK ;
		Table table = new Table(composite, iTableStyle);
		
		
		//table.setSize(400, 400);
		//table.setSize(300, 100);

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Table / Entity name");
		col.setWidth(220);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Entity class");
		col.setWidth(200);
		
		return table;
	}
	//----------------------------------------------------------------------------------------------
	private Table createRightPartTableTargets(Composite composite)
	{
		log(this, "createTableTargetsList(..)..." );
		
		int iTableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
						| SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.CHECK ;
		Table table = new Table(composite, iTableStyle);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		//--- Columns
		TableColumn col = null ;
		int iColumnIndex = 0 ;

		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Target");
		col.setWidth(220);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("Template");
		col.setWidth(200);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("");
		col.setWidth(20);
		
		col = new TableColumn(table, SWT.LEFT, iColumnIndex++);
		col.setText("");
		col.setWidth(20);

		//--- Add "fake tool tip" ( since v 2.0.7 )
		table.setToolTipText(""); // Disable the native tooltip
		Listener tableListener = new ListenerForTableToolTip(table) ;
		//-- ON
//		table.addListener(SWT.MouseHover, tableListener);
		//-- OFF
//		table.addListener(SWT.Dispose, tableListener);
//		table.addListener(SWT.KeyDown, tableListener);
//		table.addListener(SWT.HardKeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseDown, tableListener);
		table.addListener(SWT.MouseUp, tableListener);
		
//		table.addListener(SWT.MouseDoubleClick, tableListener);
		
//		KeyListener tableKeyListener = (KeyListener) tableListener ;
////		table.addKeyListener(tableKeyListener);
////		Control parent = table.getParent() ;
////		while ( parent.getParent() != null ) {
////			parent = parent.getParent();
////		}
//		table.addKeyListener(tableKeyListener);
//		Shell shell = table.getShell();
//		shell.addKeyListener(tableKeyListener);
		
		return table;
	}
	
	//----------------------------------------------------------------------------------------------	
	/**
	 * Populates the list of entities ( left side table )
	 */
	private void populateEntitiesTable()
	{
		log(this, "populateEntitiesTable()");
		
		RepositoryEditor repEditor = (RepositoryEditor) getEditor();
		RepositoryModel dbRep = repEditor.getDatabaseRepository();
		
		//Entity[] entities = dbRep.getEntities();
		EntityInDbModel[] entities = dbRep.getEntitiesArray(); // v 3.0.0
		if ( entities != null )
		{
//			for ( int i = 0 ; i < entities.length ; i++ ){
//				Entity entity = entities[i];
			for ( EntityInDbModel entity : entities ) { // v 3.0.0
				//String sTableName = entity.getName() ;
				String sTableName = entity.getDatabaseTable() ; // v 3.0.0
				
				//String sBeanClass = entity.getBeanJavaClass();
				String entityClassName = entity.getClassName(); // v 3.0.0
				
				if ( entityClassName == null ) entityClassName = "???" ;
				
                //--- Create the row content 
                String[] row = new String[] { sTableName, entityClassName };
				
                //--- Create the TableItem and set the row content 
            	TableItem tableItem = new TableItem(_tableEntities, SWT.NONE );
                tableItem.setChecked(false);                
                tableItem.setText(row);                
                //tableItem.setData( sTableName );
                tableItem.setData( entityClassName ); // v 3.0.0
			}
		}
	}
	
	//----------------------------------------------------------------------------------------------
	/**
	 * Refresh the targets table from the current configuration supposed to be up to date 
	 */
	//protected void refreshTargetsTable()
	protected void refreshTargetsTable(List<TargetDefinition> targetslist, List<TargetDefinition> resourcesTargets )	// v 2.0.7
	
	{
		log("refreshTargetsTable : " + targetslist.size() + " targets / " 
				+ (resourcesTargets != null ? resourcesTargets.size() + " resources" : "no resource" ));

		//--- Keep the list of bundle static resource for copy during generation
		_resourcesTargets = resourcesTargets ;
		
		//--- Refresh the bundles list in the combobox
		_comboBundles.refresh();
		
		//--- Re-populate the SWT table
		populateTargetsTable(targetslist);
		
		//--- Enable the check box only if there's at least one resource
		if ( _resourcesTargets.size() > 0 ) {
			//--- This bundle has static resources 
			String bundleName = getRepositoryEditor().getCurrentBundleName();
        	SettingsManager settingsManager = new SettingsManager( getProject() ) ;        	
        	if ( settingsManager.readBundleStaticResourcesCopiedFlag(bundleName) != true ) {
        		//--- Not yet copied for this bundle => checked by default
    			_checkboxStaticResources.setSelection(true);
        	}
        	else {
        		//--- Already copied for this bundle => not checked by default
    			_checkboxStaticResources.setSelection(false);
        	}
			_checkboxStaticResources.setEnabled(true) ;
		}
		else {
			//--- No static resources
			_checkboxStaticResources.setSelection(false);
			_checkboxStaticResources.setEnabled(false) ;
		}
		
	}

	//----------------------------------------------------------------------------------------------
	//private void populateTargetsTable()
	private void populateTargetsTable(List<TargetDefinition> list) // v 2.0.7
	{
		log("populateTargetsTable");
//		ProjectConfig projectConfig = getProjectConfig();
//		if ( projectConfig == null )
//		{
//			return ;
//		}
		
//		List<TargetDefinition> list = projectConfig.getTemplates(); // NB : the list can be null 
		if ( list != null )
		{
			_tableTargets.removeAll();
			//for ( SpecificTemplate st : list ) {
			for ( TargetDefinition targetDef : list ) {
				
//		        // Build a target instance and bind it with the table item
//		        GenericTarget target = new GenericTarget(st.getName(), st.getTargetFile(), 
//		        		st.getTargetFolder(), st.getTemplate() );
		        
				log(this, " . Target : " + targetDef.getName() + " - " + targetDef.getTemplate() );
				
                //--- Create the TableItem and set the row content 
            	TableItem tableItem = new TableItem(_tableTargets, SWT.NONE );
                tableItem.setChecked(false);                
				tableItem.setImage((Image)null);
                tableItem.setData( targetDef ); // Keep the target as "Data"
                
                //--- Col 0
				tableItem.setText(0, targetDef.getName()) ;
				//tableItem.getImageIndent();
                //tableItem.setImage(0,  null );
				if ( targetDef.isOnce() ) {
					tableItem.setImage( PluginImages.getImage(PluginImages.FILE1 ) );
				} else {
					tableItem.setImage( PluginImages.getImage(PluginImages.FILES ) );
				}

                //--- Col 1
				tableItem.setText(1, targetDef.getTemplate()) ;
				
                //tableItem.setText( new String[] { st.getName(), st.getTemplate() } );    
                
                //--- Col 2
                tableItem.setImage(2,  PluginImages.getImage(PluginImages.EDIT_ICON ) );
                
                //--- Col 3
                tableItem.setText(3, (targetDef.isOnce() ? "1" : "*" ) );
			}
		}
		
		// Edit template file if click on column 2
		OpenTemplateFileInEditor listener = new OpenTemplateFileInEditor(getRepositoryEditor(), getProject(), _tableTargets, 2 ) ;
		_tableTargets.addListener(SWT.MouseDown, listener );
	}
	
    private boolean confirmBulkGeneration()
    {
		log(this, "confirmBulkGeneration()");
    	int numberOfSelectedTargets = 0 ;
    	for ( TableItem item : _tableTargets.getItems() ) {
    		if ( item.getChecked() ) {
    			numberOfSelectedTargets++ ;
    		}
    	}
		if ( numberOfSelectedTargets > 0 ) {
			String sMsg = "This bulk generation will overwrite existing files if they exist." 
				+ "\n\n" + numberOfSelectedTargets + " target(s) selected."
				+ "\n\n" + "Launch generation ?";
			return MsgBox.confirm(" Confirm generation", sMsg) ;
		}
		else {
			//--- Nothing to generate
			if ( _checkboxStaticResources.getSelection() ) {
				return true ; // Copy the resources 
			}
			else {
				MsgBox.info("Nothing to generate, nothing to copy.");
				return false ; // Nothing to do
			}
		}
    }
    
    //private LinkedList<TargetDefinition> getSelectedTargets(ProjectConfig projectConfig)
    private LinkedList<TargetDefinition> getSelectedTargets()
    {
    	LinkedList<TargetDefinition> selectedTargets = new LinkedList<TargetDefinition>();
    	
    	TargetsUtil.addSelectedItemsToList(selectedTargets, _tableTargets);
    	
		return selectedTargets ;
    }
    
    /**
     * Returns a List of entity names ( list of String )
     * @return
     */
    private LinkedList<String> getSelectedEntities()
    {
    	LinkedList<String> selectedEntities = new LinkedList<String>();
    	
		int n = _tableEntities.getItemCount() ;
		for ( int i = 0 ; i < n ; i++ )
		{
			TableItem item = _tableEntities.getItem(i);
			if ( item.getChecked() == true )
			{
				// Retrieve the Target associated with the table item
		    	Object oData = item.getData();
		    	if ( oData != null )
		    	{
			    	if ( oData instanceof String )
			    	{
			    		String entityName = (String) oData ;
			    		selectedEntities.addLast(entityName);
						log(this, "getSelectedEntities() : add entity " + entityName );
			    	}
			    	else
			    	{
			    		MsgBox.error("Table item " + i + " : invalid Data type");
			    	}
		    	}
		    	else
		    	{
		    		MsgBox.error("Table item " + i + " : no data");
		    	}
			}
		}
    	return selectedEntities ;
    }
    
    private void launchBulkGeneration()
    {
    	log("launchBulkGeneration()...");
    	
        //--- Get the project configuration
    	//RepositoryEditor editor = (RepositoryEditor) getEditor();
//		IProject iProject = getProject();
    	
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( iProject );
//        if ( projectConfig == null )
//        {
//        	MsgBox.error("Cannot get project configuration");
//        	return 0;
//        }
//        String sSourceDir = projectConfig.getSourceFolder();
//		PluginLogger.log( "Source Folder : " + sSourceDir );
		
		//String sProjectLocation = EclipseProjUtil.getProjectDir(iProject);

		//--- Get the selected entities list
		LinkedList<String> selectedEntities = getSelectedEntities();
    	log("launchBulkGeneration() : " + selectedEntities.size() + "selected entitie(s)" );
		
    	//--- Get the selected targets list
    	LinkedList<TargetDefinition> selectedTargets = getSelectedTargets();
    	log("launchBulkGeneration() : " + selectedTargets.size() + " selected target(s)");
    	
    	//GenerationTask generationTask = new GenerationTask( getRepositoryEditor() );
    	
    	//--- Set static bundle resources to be copied if check-box is selected
    	List<TargetDefinition> resourcesTargets = null ;
    	if ( this._checkboxStaticResources.getSelection() ) {
    		resourcesTargets = _resourcesTargets ;
    	}
    	
    	//generationTask.generateTargets(selectedEntities, selectedTargets, resourcesTargets );

    	launchBulkGenerationTask(selectedEntities, selectedTargets, resourcesTargets); // v 3.0.0
    	
    	
    	if ( resourcesTargets != null ) {
    		//--- Update the settings file : set the 'Bundle Static Resources Copied' Flag to 'TRUE'
			String bundleName = getRepositoryEditor().getCurrentBundleName();
        	SettingsManager settingsManager = new SettingsManager( getProject() ) ;
        	settingsManager.updateBundleStaticResourcesCopiedFlag(bundleName, true);
        	//--- Uncheck the check-box
        	_checkboxStaticResources.setSelection(false);
    	}
    }
    
    private GenerationTaskResult launchBulkGenerationTask(
    		LinkedList<String> selectedEntities, 
    		LinkedList<TargetDefinition> selectedTargets,
    		List<TargetDefinition> resourcesTargets )
    {
    	//--- Prepare the generation task environment 	
    	RepositoryEditor editor = getRepositoryEditor();
//		ProjectConfig projectConfig = editor.getProjectConfig();
		TelosysToolsCfg telosysToolsCfg = getProjectConfig(); // v3.0.0
//		GeneratorConfigManager configManager = new GeneratorConfigManager(null);
//		GeneratorConfig generatorConfig ;
//    	try {
//    		generatorConfig = configManager.initFromDirectory( 
//    				telosysToolsCfg.getProjectAbsolutePath(), //projectConfig.getProjectFolder(),     				
//    				editor.getCurrentBundleName() );
//		} catch (GeneratorException e) {
//        	MsgBox.error("GenerationTask constructor : Cannot initialize the generator configuration", e);
//        	throw new RuntimeException("Cannot initialize the generator configuration");
//		}
		
    	String bundleName = editor.getCurrentBundleName();
    	TelosysToolsLogger telosysToolsLogger = ( editor.getLogger() != null ? editor.getLogger() : new ConsoleLogger() ) ;
    	
    	//--- Create the generation task
    	GenerationTaskWithProgress generationTaskWithProgress = null ;
		try {
			generationTaskWithProgress = new GenerationTaskWithProgress(
					editor.getDatabaseRepository(),
					selectedEntities, 
					bundleName,
					selectedTargets, resourcesTargets,
					//generatorConfig,
					telosysToolsCfg,
					telosysToolsLogger
					);
		} catch (TelosysToolsException e) {
        	MsgBox.error("Cannot create GenerationTaskWithProgress", e);
		}
    	
    	//--- Launch the generation task
		if ( generationTaskWithProgress != null ) {
	    	GenerationTaskResult result = generationTaskWithProgress.launch();
	    	return result ;
		}
		else {
			return new GenerationTaskResult();
		}
    }
}