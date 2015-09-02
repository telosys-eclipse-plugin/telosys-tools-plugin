package org.telosys.tools.eclipse.plugin.editors.dbconfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.dbcfg.DatabaseType;
import org.telosys.tools.commons.dbcfg.DatabasesConfigurations;
import org.telosys.tools.commons.jdbc.ConnectionManager;
import org.telosys.tools.db.metadata.ColumnMetaData;
import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;
import org.telosys.tools.db.metadata.MetaDataManager;
import org.telosys.tools.db.metadata.PrimaryKeyColumnMetaData;
import org.telosys.tools.db.metadata.SchemaMetaData;
import org.telosys.tools.db.metadata.TableMetaData;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginImages;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.commons.mapping.MapperTextBean;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.repository.RepositoryGenerator;
import org.telosys.tools.repository.RepositoryUpdator;
import org.telosys.tools.repository.UpdateLogWriter;
import org.telosys.tools.repository.changelog.ChangeLog;
import org.telosys.tools.repository.model.RepositoryModel;
import org.telosys.tools.repository.persistence.PersistenceManager;
import org.telosys.tools.repository.persistence.PersistenceManagerFactory;
import org.telosys.tools.repository.rules.RepositoryRulesProvider;


/**
 * Database configuration editor - Page 1 (main page)
 * 
 * 
 */
/* package */ class DbConfigEditorPage1 extends DbConfigEditorPage 
{

    private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd.HHmmss");
    private final static String  TEXT_DATA_MAPPER       = "TEXT_DATA_MAPPER" ;
    private final static String  TEXT_DATA_UPDATE_COMBO = "TEXT_DATA_UPDATE_COMBO" ;

    private final static int GROUP_X = 12 ;
	
	private final static int TEXT_X = 10 ;

	private final static int TEXT_HEIGHT = 22 ;

	private final static int GET_NOTHING      = 0 ;
	private final static int GET_COLUMNS      = 1 ;
	private final static int GET_PRIMARY_KEYS = 2 ;
	private final static int GET_FOREIGN_KEYS = 3 ;
	private final static int GET_CATALOGS  = 11 ;
	private final static int GET_SCHEMAS   = 12 ;
	
    //-----------------------------------------------------------
	private DbConfigEditor _editor = null ;
    //-----------------------------------------------------------
	
	private Combo _ComboDatabases = null ;
	
    private Text _tId             = null ;
    private Text _tName           = null ;
    private Text _tDriver         = null ;
    private Text _tUrl            = null ;
    private Text _tUser           = null ;
    private Text _tPassword       = null ;
    private Text _tIsolationLevel = null ;
    private Text _tPoolSize       = null ;
    private Text _tTypeName       = null ;
    private Text _tDialect        = null ;

    private Text _InfoURL       = null ;
    private Text _InfoProdName  = null ;
    private Text _InfoProdVer   = null ;
    private Text _InfoDriverName = null ;
    private Text _InfoDriverVer  = null ;
    private Text _InfoMaxConn    = null ;
    private Text _InfoUser      = null ;
    private Text _InfoIsolation = null ;
    private Text _InfoCatalogTerm = null ;
    private Text _InfoCatalogSepar = null ;
    private Text _InfoSchemaTerm = null ;
    private Text _InfoSearchEscape = null ;
    
	private Text _tMetaDataCatalog = null ;
	private Text _tMetaDataSchema = null;
	private Text _tMetaDataTablePattern = null;
	private Text _tMetaDataTableTypes = null;
	private Text _tMetaDataTableInclude = null;
	private Text _tMetaDataTableExclude = null;

    private Text _tMetaData = null ;
    
    //-----------------------------------------------------------
	
	private boolean _bPopulateInProgress = false ;
	
	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DbConfigEditorPage1(FormEditor editor, String id, String title) {
		super(editor, id, title);
		//PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
		_editor = (DbConfigEditor) editor;
	}

//	private void setDirty()
//	{
//		//RepositoryEditor repEditor = (RepositoryEditor) getEditor();
//		// repEditor.setDirty();
//		_editor.setDirty();
//	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..)..." );
		Control pageControl = getPartControl();
		
//		Display display = pageControl.getDisplay();
//		_backgroundColor = display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);		
//		pageControl.setBackground(_backgroundColor ) ;
		
		if ( pageControl != null )
		{
			log(this, "createFormContent(..) : getPartControl() != null " );
		}
		else
		{
			log(this, "createFormContent(..) : getPartControl() is null !!! " );
			return ;
		}
		
		if ( pageControl instanceof Composite )
		{
			log(this, "- pageControl is a Composite  " );
			log(this, "- pageControl class = " + pageControl.getClass() );
			
			Composite pageComposite = (Composite) pageControl ;
			Layout layout = pageComposite.getLayout();			
			log(this, "- pageControl layout class = " + layout.getClass() );
		}
		else
		{
			log(this, "- pageControl() is NOT a Composite !!! " );
		}

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
		// form.setText( _repEditor.getDatabaseTitle() );
		
		Composite body = form.getBody();
		log(this, "- body class = " + body.getClass() );
		
		Layout layout = body.getLayout();			
		if ( layout != null )
		{
			log(this, "- body layout class = " + layout.getClass() );
		}
		else
		{
			log(this, "- body layout class = NO LAYOUT ! ");
		}
		
		Layout bodyLayout = new RowLayout(SWT.VERTICAL);
		
		body.setLayout( bodyLayout );
		
		//--------------------------------------------------------------
		//--- Group "Database" ( composite )
		Group group1 = new Group(body, SWT.NONE);
		group1.setText("Database");
		//group1.setSize(GROUP_WIDTH, 60);
		//tab.setBackground(DAOColor.color(disp));
		//group1.setLocation(GROUP_X, 20);
        group1.setBackground( getBackgroundColor() );

		//--- Combo Box "Database table"
		_ComboDatabases = new Combo(group1, SWT.BORDER | SWT.READ_ONLY);
		_ComboDatabases.setBounds(TEXT_X, 25, 260, TEXT_HEIGHT);
		_ComboDatabases.setVisibleItemCount(12);
		setDatabasesComboAction(_ComboDatabases);

		//--------------------------------------------------------------
		Button button ;
		//--------------------------------------------------------------
		button = new Button(group1, SWT.NONE);
		button.setText("New");
		button.setBounds(280, 25, 70, 25); // (x,y, width, height)
		button.setImage( PluginImages.getImage(PluginImages.NEW_DATABASE ) );
		button.setToolTipText("New database configuration");

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	actionNewDatabase() ;
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		//--------------------------------------------------------------
		button = new Button(group1, SWT.NONE);
		button.setText("Delete");
		button.setBounds(360, 25, 70, 25); // (x,y, width, height)
		button.setImage( PluginImages.getImage(PluginImages.DELETE_DATABASE ) );
		button.setToolTipText("Delete database configuration");

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	actionDeleteDatabase() ;
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		//--------------------------------------------------------------
		button = new Button(group1, SWT.NONE);
		button.setText("Generate repository");
		button.setBounds(450, 25, 140, 25); // (x,y, width, height)
		button.setImage( PluginImages.getImage(PluginImages.GENERATE_REPO ) );
		button.setToolTipText("Generate a new repository");

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
                actionGenerateRepository();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		//--------------------------------------------------------------
		button = new Button(group1, SWT.NONE);
		button.setText("Update repository");
		button.setBounds(600, 25, 140, 25); // (x,y, width, height)
		button.setImage( PluginImages.getImage(PluginImages.UPDATE_REPO ) );
		button.setToolTipText("Update existing repository");

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
                actionUpdateRepository();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		//--------------------------------------------------------------
		//--- Tab Folder 
		Composite composite = null ;

		composite = new Composite(body, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLocation(GROUP_X, 100);
		composite.setSize(400, 200);
		composite.setBackground( getBackgroundColor() );
		

		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
		
		//tabFolder.setLocation(GROUP_X, 100);
		//tabFolder.setSize(400, 200);
		tabFolder.setBackground( getBackgroundColor() ); // No effect : cannot change the TabFolder color 

		createTabFolder1(tabFolder);
		createTabFolder2(tabFolder);
		createTabFolder3(tabFolder);

		//--------------------------------------------------------------
		
		log(this, "Populate DATABASES combo ..." );

		populateComboDatabases();
		selectFirstDatabaseInCombo();

		log(this, "Populate DATABASES combo : done." );
	}
	
	//----------------------------------------------------------------------------------------------
	private void createTabFolder1(TabFolder tabFolder) {
		log(this, "createTabFolder1() ..." );

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("  Configuration  ");

		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setBackground( getBackgroundColor() );
		
	    // Color color = DAOColor.color(tabContent.getDisplay());
		createConfigurationFields(tabContent) ;

		//--- Button "Test connection"
		Button button = new Button(tabContent, SWT.NONE);
		button.setText("Test connection");
		button.setBounds(460, 20, 120, 25);

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	actionTestConnection();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
    	
		//--- Button "Show Libraries"
		Button buttonLibraries = new Button(tabContent, SWT.NONE);
		buttonLibraries.setText("Show libraries");
		buttonLibraries.setBounds(460, 50, 120, 25);

		buttonLibraries.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	showLibraries();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
    	
		tabItem.setControl(tabContent);
	}
	//----------------------------------------------------------------------------------------------
	private void createConfigurationFields(Composite container) 
	{
		int x = 5 ; 
		int y = 20 ; 
		int yGap = 30 ; 
		int labelWidth = 100 ;
		int textWidth  = 320 ;
		
	    _tId       = createTextWithLabel(container, x, y, "Id", false, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tName     = createTextWithLabel(container, x, y, "Name", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tDriver   = createTextWithLabel(container, x, y, "Driver", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tUrl      = createTextWithLabel(container, x, y, "URL", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    
	    _tUser     = createTextWithLabel(container, x, y, "User", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tPassword = createTextWithLabel(container, x, y, "Password", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;

	    _tTypeName = createTextWithLabel(container, x, y, "Type ", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tDialect  = createTextWithLabel(container, x, y, "Dialect", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;

	    createSingleLabel(container, x, y, "Configuration below is useful only for the framework : ", labelWidth+textWidth );
	    y = y + yGap ;
	    
	    _tIsolationLevel = createTextWithLabel(container, x, y, "Isolation level", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _tPoolSize  = createTextWithLabel(container, x, y, "Pool size", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;

	    bindViewToModel(_tId,             "setDatabaseId",     int.class );
	    bindViewToModel(_tName,           "setDatabaseName",   String.class);
	    bindViewToModel(_tDriver,         "setDriverClass",    String.class);
	    bindViewToModel(_tUrl,            "setJdbcUrl",        String.class);
	    bindViewToModel(_tUser,           "setUser",           String.class);
	    bindViewToModel(_tPassword,       "setPassword",       String.class);
	    bindViewToModel(_tTypeName,       "setTypeName",       String.class); // ver 2.1.0
	    bindViewToModel(_tDialect,        "setDialect",        String.class); // ver 2.1.0
	    
	    bindViewToModel(_tIsolationLevel, "setIsolationLevel", String.class);
	    bindViewToModel(_tPoolSize,       "setPoolSize",       int.class );
	    _tName.setData( TEXT_DATA_UPDATE_COMBO, "true" ) ; // Flag for update combo item 
	}
	
	//----------------------------------------------------------------------------------------------
	private void createTabFolder2(TabFolder tabFolder) 
	{
		log(this, "createTabFolder2() ..." );
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("  Information  ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setBackground( getBackgroundColor() );
		
		createInformationFields(tabContent) ;
		
		//--- Button "Get infos"
		Button button = new Button(tabContent, SWT.NONE);
		button.setText("Get database info");
		button.setBounds(460, 20, 120, 25);

    	button.addSelectionListener( new SelectionListener() 
    	{
            public void widgetSelected(SelectionEvent arg0)
            {
            	actionGetInformations();
            }
            public void widgetDefaultSelected(SelectionEvent arg0)
            {
            }
        });
		
		tabItem.setControl(tabContent);
	}
	//----------------------------------------------------------------------------------------------
	private void createInformationFields(Composite container) 
	{
		int x = 5 ; 
		int y = 20 ; 
		int yGap = 30 ; 
		int labelWidth = 120 ;
		int textWidth  = 320 ;
		
	    _InfoProdName  = createTextWithLabel(container, x, y, "Product name", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoProdVer   = createTextWithLabel(container, x, y, "Product version", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoDriverName  = createTextWithLabel(container, x, y, "Driver name", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoDriverVer   = createTextWithLabel(container, x, y, "Driver version", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoURL       = createTextWithLabel(container, x, y, "JDBC URL", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoUser      = createTextWithLabel(container, x, y, "User name", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoIsolation = createTextWithLabel(container, x, y, "Def. isolation level", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoMaxConn = createTextWithLabel(container, x, y, "Max connections", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoCatalogTerm = createTextWithLabel(container, x, y, "Catalog term", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoCatalogSepar = createTextWithLabel(container, x, y, "Catalog separator", true, labelWidth, textWidth ) ;  
	    y = y + yGap ;
	    _InfoSchemaTerm = createTextWithLabel(container, x, y, "Schema term", true, labelWidth, textWidth ) ; 
	    y = y + yGap ;
	    _InfoSearchEscape = createTextWithLabel(container, x, y, "Search escape", true, labelWidth, textWidth ) ; 
	}

	//----------------------------------------------------------------------------------------------
	/**
	 * Tab "Meta-data"
	 * @param tabFolder
	 */
	private void createTabFolder3(TabFolder tabFolder) 
	{
		log(this, "createTabFolder3() ..." );
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("  Meta-data  ");
		
		Composite tabContent = new Composite(tabFolder, SWT.NONE);
		tabContent.setBackground( getBackgroundColor() );
		
		GridLayout gridLayout = new GridLayout ();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 12;
		tabContent.setLayout(gridLayout);
		
		createTabFolder3Fields(tabContent) ;
		
		tabItem.setControl(tabContent);

	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Creates the set of label + input field
	 * @param container
	 */
	private void createTabFolder3Panel1(Composite container) 
	{
		GridData gdPanel = new GridData();
		gdPanel.verticalAlignment = SWT.BEGINNING ;
		gdPanel.widthHint = 400 ;
		
		Composite panel = new Composite(container, SWT.NONE );
		GridLayout gridLayout = new GridLayout ();
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 12;
		panel.setLayout(gridLayout);
		panel.setLayoutData(gdPanel);

		{
			GridData gd = new GridData();
			gd.widthHint = 260;
			gd.verticalAlignment = SWT.BEGINNING ;

			_tMetaDataCatalog = createTextWithLabel(panel, "Catalog ('!' for null) ", gd, true );

			_tMetaDataSchema = createTextWithLabel(panel, "Schema ('!' for null) ", gd, true );
	
			_tMetaDataTablePattern = createTextWithLabel(panel, "Table name pattern", gd, true );
	
			_tMetaDataTableTypes = createTextWithLabel(panel, "Table types (separated by blanks)", gd, true );
			
			_tMetaDataTableInclude = createTextWithLabel(panel, "Regex pattern to include tables", gd, true ); // v 2.1.1
			
			_tMetaDataTableExclude = createTextWithLabel(panel, "Regex pattern to exclude tables", gd, true ); // v 2.1.1
						
			bindViewToModel(_tMetaDataCatalog,      "setMetadataCatalog",          String.class);
			bindViewToModel(_tMetaDataSchema,       "setMetadataSchema",           String.class);
			bindViewToModel(_tMetaDataTablePattern, "setMetadataTableNamePattern", String.class);
			bindViewToModel(_tMetaDataTableTypes,   "setMetadataTableTypes",       String.class);
			bindViewToModel(_tMetaDataTableInclude, "setMetadataTableNameInclude", String.class);
			bindViewToModel(_tMetaDataTableExclude, "setMetadataTableNameExclude", String.class);
		}
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Creates the 1rst set of buttons 
	 * @param container
	 */
	private void createTabFolder3Panel2(Composite container) 
	{
		GridData gdPanel = new GridData();
		gdPanel.verticalAlignment = SWT.BEGINNING ;
		//gdPanel.widthHint = 200 ;

		Composite panel = new Composite(container, SWT.NONE );
		GridLayout gl = new GridLayout ();
		gl.numColumns = 1;
		gl.marginHeight = 12;
		gl.marginLeft = 20 ;
		panel.setLayout(gl);
		panel.setLayoutData(gdPanel);

		GridData gd = new GridData();
		gd.widthHint = 120;
		gd.verticalAlignment = SWT.BEGINNING ;
		{
			Button button = new Button(panel, SWT.NONE);
			button.setText("Get tables");
			button.setLayoutData(gd);
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_NOTHING);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}

		{
	    	Button button = new Button(panel, SWT.NONE);
			button.setText("Get columns");
			button.setLayoutData(gd);		
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_COLUMNS);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}

		{
	    	Button button = new Button(panel, SWT.NONE);
			button.setText("Get primary keys");
			button.setLayoutData(gd);		
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_PRIMARY_KEYS);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}
		{
	    	Button button = new Button(panel, SWT.NONE);
			button.setText("Get foreign keys");
			button.setLayoutData(gd);		
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_FOREIGN_KEYS);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}
	}
	//----------------------------------------------------------------------------------------------
	/**
	 * Creates the 2nd set of buttons
	 * @param container
	 */
	private void createTabFolder3Panel3(Composite container) 
	{
		GridData gdPanel = new GridData();
		gdPanel.verticalAlignment = SWT.BEGINNING ;
		//gdPanel.widthHint = 200 ;

		Composite panel = new Composite(container, SWT.NONE  );
		GridLayout gl = new GridLayout ();
		gl.numColumns = 1;
		gl.marginHeight = 12;
		gl.marginLeft = 20 ;
		panel.setLayout(gl);
		panel.setLayoutData(gdPanel);

		GridData gd = new GridData();
		gd.widthHint = 120;
		gd.verticalAlignment = SWT.BEGINNING ;
		{
			Button button = new Button(panel, SWT.NONE);
			button.setText("Get catalogs");
			button.setLayoutData(gd);
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_CATALOGS);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}

		{
	    	Button button = new Button(panel, SWT.NONE);
			button.setText("Get schemas");
			button.setLayoutData(gd);		
	    	button.addSelectionListener( new SelectionListener() 
	    	{
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	actionGetMetaData(GET_SCHEMAS);
	            }
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            }
	        });
		}

	}
	//----------------------------------------------------------------------------------------------
	private void createTabFolder3Fields(Composite container) 
	{
		//--- ROW 1
		createTabFolder3Panel1(container);
		
		createTabFolder3Panel2(container);
		
		createTabFolder3Panel3(container);
		
		//--- ROW 2 ( Span 3 ) : the text area used to print the result
		GridData gd = new GridData();
		gd.widthHint  = 700;
		gd.heightHint = 340 ;
		gd.horizontalSpan = 3 ;
		_tMetaData = new Text (container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		
		_tMetaData.setLayoutData(gd);
	}
	
	//----------------------------------------------------------------------------------------------
	private Text createTextWithLabel(Composite container, int x, int y, String sLabel, boolean b, int labelWidth, int textWidth ) 
	{
		Label label = new Label(container, SWT.NONE);
		label.setText( sLabel + " : ");
		label.setBounds(x, y, labelWidth, TEXT_HEIGHT);
		//label.setLocation(x, y);
		 
		Text text = new Text(container, SWT.BORDER);
		text.setBounds(x + labelWidth + 10 , y, 320, TEXT_HEIGHT);
		//text.setLocation(x + 80, y);
		text.setEnabled(b);
		 
		return text ;
	}
	private void createSingleLabel(Composite container, int x, int y, String labelText, int labelWidth ) 
	{
		Label label ;
		
		label = new Label(container, SWT.NONE);
		label.setText( labelText );
		label.setBounds(x, y, labelWidth, TEXT_HEIGHT);
		 
//		label = new Label(container, SWT.NONE);
//		label.setText( label2 );
//		label.setBounds(x + labelWidth1 + 10 , y, 320, TEXT_HEIGHT);
	}
	//----------------------------------------------------------------------------------------------
	private Text createTextWithLabel(Composite container, String sLabel, GridData gridData, boolean b ) 
	{
		Label label = new Label(container, SWT.NONE);
		label.setText( sLabel + " : ");
		
		Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(gridData);
		text.setEnabled(b);
		return text ;
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
	
	
    //------------------------------------------------------------------------------------------------------
    private void populateComboDatabases() 
    {
    	DatabasesConfigurations databasesConfigurations = _editor.getDatabasesConfigurations() ;
        if ( databasesConfigurations != null )
    	{
    		if ( _ComboDatabases != null )
    		{
    			_ComboDatabases.removeAll(); 
                
                List<DatabaseConfiguration> list = databasesConfigurations.getDatabaseConfigurationsList() ;
        		log(this, "loadComboBases() : nb bases = " + list.size() );
        		for ( DatabaseConfiguration db : list ) {
                    String sItem = db.getDatabaseId() + " - " + db.getDatabaseName() ;
                    _ComboDatabases.add(sItem);
            		log(this, "loadComboBases() : add " + sItem );			
        		}
    		}
    		else
    		{
        		MsgBox.error("populateDatabases : ComboBox is null !");
    		}
    	}
    	else
    	{
    		MsgBox.error("populateDatabases : no db configurations ( null, file not loaded ) ");
    	}
    	
    }
    
	private void clearFields()
	{
		_bPopulateInProgress = true ;
		
		//--- Tab 1
		_tId.setText("");
		_tName.setText("");
		_tDriver.setText("");
		_tUrl.setText("");
		_tIsolationLevel.setText("");
		_tPoolSize.setText("");
		_tUser.setText("");
		_tPassword.setText("");
		_tTypeName.setText(""); // ver 2.1.0
		_tDialect.setText(""); // ver 2.1.0
	      
		//--- Tab 2
		_InfoURL.setText( "" );
		_InfoProdName.setText( "" );
		_InfoProdVer.setText( "" );
		_InfoDriverName.setText( "" );
		_InfoDriverVer.setText( "" );
		_InfoMaxConn.setText( "" );
		_InfoUser.setText("");
		_InfoIsolation.setText( "" );
		_InfoCatalogTerm.setText( "" );
		_InfoCatalogSepar.setText( "" );
		_InfoSchemaTerm.setText( "" );
		_InfoSearchEscape.setText( "" );

		//--- Tab 3
		_tMetaDataCatalog.setText("");
		_tMetaDataSchema.setText("");
		_tMetaDataTablePattern.setText("");
		_tMetaDataTableTypes.setText("");
		_tMetaDataTableInclude.setText("");
		_tMetaDataTableExclude.setText("");
		_tMetaData.setText("");

		//--- Tab
		_bPopulateInProgress = false ;
	}
	
	private String nn ( String s )
	{
		return s != null ? s : "" ;
	}

	private String quote ( String s )
	{
		if ( s == null ) return "null" ;
		return "'" + s + "'" ;
	}
	private String arrayToString ( String[] array )
	{
		if ( array == null ) return "null" ;
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for ( int i = 0 ; i < array.length ; i++ )
		{
			if ( i > 0 ) sb.append(", ");
			sb.append(array[i]);
		}
		sb.append(" ]");
		return sb.toString();
	}
	
	private void populateDatabaseConfigFields(int databaseId)
	{
		log(this, "populateDatabaseConfigFields()");
		
		_bPopulateInProgress = true ;
		
    	DatabasesConfigurations databasesConfigurations = _editor.getDatabasesConfigurations() ;		
		if ( databasesConfigurations != null )
		{
			DatabaseConfiguration db = databasesConfigurations.getDatabaseConfiguration(databaseId);
			if ( db != null )
			{
				//---   
				_tId.setText       ( "" + db.getDatabaseId() );
				_tName.setText     ( nn( db.getDatabaseName() ) );
				_tDriver.setText   ( nn( db.getDriverClass() ) );
				_tUrl.setText      ( nn( db.getJdbcUrl() ) );
				_tUser.setText     ( nn( db.getUser() ) );
			    _tPassword.setText ( nn( db.getPassword() ));
			    _tTypeName.setText ( nn( db.getTypeName() )); // ver 2.1.0
			    _tDialect.setText  ( nn( db.getDialect() )); // ver 2.1.0

			    _tIsolationLevel.setText( nn( db.getIsolationLevel() ) );
				_tPoolSize.setText ( "" + db.getPoolSize() );
			    
			    //--- Meta-Data
			    _tMetaDataCatalog.setText( nn( db.getMetadataCatalog() ) ) ;
			    _tMetaDataSchema.setText( nn( db.getMetadataSchema() ) ) ;
			    _tMetaDataTablePattern.setText( nn( db.getMetadataTableNamePattern() ) );
			    //_tMetaDataTableTypes.setText( arrayToString ( db.getMetadataTableTypes() ) );
			    _tMetaDataTableTypes.setText( db.getMetadataTableTypes() );
			    _tMetaDataTableInclude.setText( nn( db.getMetadataTableNameInclude() ) );
			    _tMetaDataTableExclude.setText( nn( db.getMetadataTableNameExclude() ) );
			}
			else 
			{
				MsgBox.error("Database '" + databaseId + "' not found in the configuration.");
			}
		}
		else 
		{
			MsgBox.error("Databases configurations not loaded.");
		}

		_bPopulateInProgress = false ;
	}
	
    /**
     * Listener for DATABASE COMBO BOX 
     * Event fired when a database is selected
     * @param combo
     */
    private void setDatabasesComboAction(Combo combo)
    {
        combo.addSelectionListener( new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event)
            {
        		//log(this, "Tables combo listener : widgetSelected()" );
                Combo combo = (Combo) event.widget ;
                populateDatabaseConfigFields(combo);
            }
        });
    }
    
    private int getSelectedDatabaseId(Combo databasesCombo, boolean required)
    {
    	if ( databasesCombo.getSelectionIndex() >= 0 ) {
            String s = databasesCombo.getText();
            if ( s != null )
            {
        		log(this, "Database selected in combo : '" + s + "'");
                String[] parts = StrUtil.split(s, '-');
                if ( parts.length > 0 )
                {
                	String sDbId = parts[0].trim();
            		int databaseId = databaseIdAsInt(sDbId);
            		return databaseId ;
                }
                else {
        			MsgBox.error("Invalid selected text in combobox");
                }
            }
            else {
    			MsgBox.error("Invalid selected text in combobox");
            }
    	}
    	else {
    		if ( required ) {
    			MsgBox.warning("No database selected.");
    		}
    	}
    	return -99999 ;
    }
    
    /**
     * Load and show the database configuration associated with the database ID selected in the combo
     * @param databasesCombo
     */
    private void populateDatabaseConfigFields(Combo databasesCombo)
    {
        int databaseId = getSelectedDatabaseId(databasesCombo, true);
        if ( databaseId >= 0 ) {
    		//--- Clear the fields
        	clearFields();
    		//--- Populate the fields
    		populateDatabaseConfigFields(databaseId);
        }
    }    
    
    /**
     * Returns the mapper associated with the given text control
     * @param text
     * @return
     */
    @SuppressWarnings("unchecked")
    private MapperTextBean<DatabaseConfiguration> getMapper(Text text)
    {
    	Object o = text.getData(TEXT_DATA_MAPPER);
    	return (MapperTextBean<DatabaseConfiguration>) o ;
    }
    
    /**
     * Updates the model (DatabaseConfiguration) from the view fields
     * @param text
     * @param setterMethodName
     * @param setterMethodArgType
     */
    private void bindViewToModel(Text text, String setterMethodName, Class<?> setterMethodArgType )
    {
    	MapperTextBean<DatabaseConfiguration> mapperInstance = 
    		new MapperTextBean<DatabaseConfiguration>(text, DatabaseConfiguration.class, setterMethodName, setterMethodArgType);
    	text.setData(TEXT_DATA_MAPPER, mapperInstance);
    	
    	//--- Add the listener
    	text.addModifyListener(new ModifyListener()
    	{
    		// This event is fired even when the Text is modified programmatically
    		// without user action
			public void modifyText(ModifyEvent e) {
				if ( ! _bPopulateInProgress )
				{
					if ( _ComboDatabases.getSelectionIndex() >= 0 ) {
						
						Text text = (Text) e.widget ;
		        		//log(this, "Text modified : '" + text.getText() + "'" );
		        		//--- Update the model 
		        		//MapperTextBean<DatabaseConfiguration> mapper = (MapperTextBean<DatabaseConfiguration>) text.getData(TEXT_DATA_MAPPER);
		        		MapperTextBean<DatabaseConfiguration> mapper = getMapper(text) ;
		        		DatabaseConfiguration currentDatabaseConfig = getCurrentDatabaseConfig(true) ;
		        		if ( currentDatabaseConfig != null ) {		        			
			        		try {
								mapper.updateBean( currentDatabaseConfig ); // Update bean or throw exception
				        		//--- Update the combo current text if flag is set for this field
				        		if ( text.getData(TEXT_DATA_UPDATE_COMBO) != null ) {
					        		//log(this, "Text modified : Update combo item with '" + sTextValue + "'" );
				   		            String s = currentDatabaseConfig.getDatabaseId() + " - " + currentDatabaseConfig.getDatabaseName() ;
					        		int index = _ComboDatabases.getSelectionIndex();
					        		_ComboDatabases.setItem(index, s);
				        		}
				        		//--- Set "dirty" flag ON for this editor
								setDirty();
							} catch (Exception e1) {
								MsgBox.error("Cannot update the bean from the view", e1);
							}
			        		//log(this, "Model bean : '" + currentDatabaseConfig + "'" );
		        		}
					}
				}
				else
				{
	        		//log(this, "Text modified : populate in progress => no action" );
				}
			}
    		
    	});
    }
    
    private int databaseIdAsInt(String id) {
    	try {
			return Integer.parseInt(id);
		} catch (NumberFormatException e) {
			MsgBox.error("Invalid database id '" + id + "'" );
			return -9999 ;
		}
    }
    
    /**
     * Returns the Database configuration for the selected Database ID
     * ( loaded from the XML DOM tree )
     * @return
     */
    private DatabaseConfiguration getCurrentDatabaseConfig(boolean bConfigRequired) 
    {
    	DatabasesConfigurations databasesConfigurations = _editor.getDatabasesConfigurations() ;
        if ( databasesConfigurations != null )
        {
        	int id = getSelectedDatabaseId(_ComboDatabases, bConfigRequired) ;
        	if ( id >= 0 ) {
            	DatabaseConfiguration db = databasesConfigurations.getDatabaseConfiguration(id);
            	if ( db != null )
            	{
            		return db ;
            	}
            	else
            	{
            		if ( bConfigRequired ) {
                		MsgBox.error("No database configuration for id '" + id + "'" );        			
            		}
                    return null ;
            	}
        	}
        }
        else
        {
    		MsgBox.error("No databases configuration file (file probably not loaded) " );
        }
        return null ;
    }
    
    private void msgBoxErrorWithClassPath(String msg, Exception e, String[] classPath) 
    {
		StringBuffer sb = new StringBuffer();
		sb.append(msg + "\n" ) ;
		sb.append("Project Class Path : \n" ) ;
		for ( String s : classPath ) {
			sb.append(" . " + s + "\n") ;
		}
		MsgBox.error(sb.toString(), e );
    }
    
    private ConnectionManager createConnectionManager() 
    {
        TelosysToolsLogger logger = _editor.getTextWidgetLogger();

        //--- Get libraries where to search the JDBC Driver
        String[] libraries = getLibraries();
        
        ConnectionManager cm = null ;
		try {
			cm = new ConnectionManager( libraries, logger );
		} catch (TelosysToolsException e) {
			logException(e);
			msgBoxErrorWithClassPath("Cannot create ConnectionManager", e, libraries);
    		cm = null ;
		}
        return cm ;
    }
    
    /**
     * Returns the libraries where to search the JDBC driver
     * @return list of libraries (never null)
     */
    private String[] getLibraries() 
    {
    	log("getLibraries()");    	
        IProject project = getProject();
    	log("getLibraries() : project name = '" + project.getName() + "' " );    	
        //--- Get libraries where to search the JDBC Driver
        String[] librariesFromFolder = getLibrariesFromLibFolder(project) ;
    	log("getLibraries() : librariesFromFolder.length = " + librariesFromFolder.length );    	
        if ( EclipseProjUtil.isJavaProject(project) ) {
        	log("getLibraries() : Java project");    	
        	// if Java project : use project build path
        	String[] librariesFromJavaBuildPath = EclipseProjUtil.getClassPathLibraries(project);
        	if ( librariesFromJavaBuildPath != null ) {
            	log("getLibraries() : librariesFromJavaBuildPath.length = " + librariesFromJavaBuildPath.length );    	
            	return combine(librariesFromFolder, librariesFromJavaBuildPath);
        	}
        	else {
            	log("getLibraries() : No libraried from Java Build Path");    	
                return librariesFromFolder ;
        	}
        }
        else {
        	log("getLibraries() : Not a Java project");    	
            return librariesFromFolder ;
        }
    }

    private String[] combine(String[] a, String[] b){
    	log("combine(a[" + a.length + "), b["+ b.length+ "])" );    	
    	
        String[] result = new String[ a.length + b.length ];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    /**
     * Returns the libraries located in the Telosys Tools "lib" folder
     * @return
     */
    private String[] getLibrariesFromLibFolder(IProject project) 
    {
    	log("getLibrariesFromFolder()");    	
    	String[] voidLib = new String[0];
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( project );	
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( project ); // v 3.0.0
		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( project ); // v 3.0.0
//		if ( null == projectConfig ) {
//	    	log("getLibrariesFromFolder() : project configuration is NULL !");    	
//        	MsgBox.error("Cannot get project configuration !\n Check project properties / Telosys Tools");
//        	return voidLib ;
//		}
        //String dir  = projectConfig.getLibrariesFolder();
//        String dir  = projectConfig.getTelosysToolsCfg().getLibrariesFolder();
        String dir  = telosysToolsCfg.getLibrariesFolder(); // v 3.0.0
    	log("getLibrariesFromFolder() : folder = '" + dir + "'");    	
        
        IResource resource = EclipseProjUtil.getResource(project, dir);
        if ( null == resource ) {
        	MsgBox.error("Folder '"+dir+"' not found !");
        	return voidLib ;
        }
    	if ( ! ( resource instanceof IFolder ) ) {
        	MsgBox.error("'"+dir+"' is not a folder !");
        	return voidLib ;
    	}
        if ( resource.isAccessible() != true) {
        	MsgBox.error("Folder '"+dir+"' is not accessible !");
        	return voidLib ;
        }
        
		IFolder folder = (IFolder) resource ;
		IResource[] members = null ;
		try {
			members = folder.members();
		} catch (CoreException ce) {
        	MsgBox.error("Cannot get files from folder '"+dir+"' (CoreException)", ce);
        	return voidLib ;
		}
		
		//--- Build list of ".jar" and ".zip" files
		List<String> libraries = new LinkedList<String>();
		for ( IResource r : members ) {
			//--- If .jar file
			if ( r instanceof IFile ) {
				String ext = r.getFileExtension(); // can be null
				if ( "jar".equals(ext) || "zip".equals(ext)) {
					String fileInProject = r.getProjectRelativePath().toString();
					String libFile = EclipseProjUtil.getAbsolutePathInFileSystem(project, fileInProject);
					libraries.add(libFile);
				}
			}
		}
    	log("getLibrariesFromFolder() : libraries size = " + libraries.size() );    	
		return libraries.toArray(new String[0]);
    }
    
    private String getRepositoryFileName(String sDatabaseName) 
    {
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( _editor.getProject() );		
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( _editor.getProject() ); // v 3.0.0
    	TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( _editor.getProject() ); // v 3.0.0
        //String dir   = projectConfig.getRepositoriesFolder();
//        String dir   = projectConfig.getTelosysToolsCfg().getRepositoriesFolder();
        String dir   = telosysToolsCfg.getModelsFolder(); // v 3.0.0
        return FileUtil.buildFilePath(dir, sDatabaseName+".dbrep" );
    }
    
    private File getRepositoryFile(String sDatabaseName) 
    {
		String sRepositoryFile = getRepositoryFileName( sDatabaseName ) ;

		IProject project = _editor.getProject();
        if ( project == null )
        {
        	MsgBox.error("actionGenerateRepository() : Cannot get project ");
        	return null ;
        }
		IFile iFile = project.getFile(sRepositoryFile);
		File file = EclipseWksUtil.toFile(iFile);
		return file ;
    }
    
    /**
     * Returns the log file name ( built from the repository file name )
     * @param sRepoFile
     * @return
     */
    private String getUpdateLogFileName(String sRepoFile)
    {
    	Date now = new Date();
    	String suffix = ".update." + DATE_FORMAT.format( now ) + ".log";
    	if ( sRepoFile.endsWith(".dbrep") )
    	{
    		int last = sRepoFile.length() - 6 ; 
    		return sRepoFile.substring(0,last) + suffix ;
    	}
    	else
    	{
    		return sRepoFile + suffix ;
    	}
    }
    
    private File getUpdateLogFile(String sRepoFile) 
    {
		String sRepositoryFile = getUpdateLogFileName( sRepoFile ) ;
		File file = new File(sRepositoryFile);
		return file ;
    }
    
    private Connection getConnection() 
    {
    	DatabaseConfiguration databaseConfiguration = getCurrentDatabaseConfig(true) ;
        if ( null == databaseConfiguration ) return null ;
        
		ConnectionManager cm = createConnectionManager();
        if ( null == cm ) return null ;

        try {
			return cm.getConnection(databaseConfiguration);
		} catch (TelosysToolsException e) {
			logException(e);
			Throwable cause = e.getCause();
			if ( cause != null && cause instanceof SQLException ) {
				SQLException sqlException = (SQLException)cause;
	            MsgBox.error("Cannot connect to the database ! "
	                      + "\n SQLException :"
	                      + "\n . Message   : " + sqlException.getMessage() 
	                      + "\n . ErrorCode : " + sqlException.getErrorCode() 
	                      + "\n . SQLState  : " + sqlException.getSQLState() 
	                      );
			}
			else {
				msgBoxErrorWithClassPath("Cannot connect to the database !", e, cm.getLibraries());
			}
		} catch (Throwable e) {
			logException(e);
		    MsgBox.error("Cannot connect to the database ! "
		            + "\n Exception : " + e.getClass().getName()
		            + "\n . Message : " + e.getMessage() 
		            );
		}
		return null ;
    }
    
    private void closeConnection(Connection con) 
    {
	    try {
		    con.close();
		} catch (SQLException e) {
			MsgBox.error("Cannot close connection (SQLException)", e );
		}
    }
    
    /**
     * Show the project's libraries defined in the "Java Build Path"
     */
    private void showLibraries() {
    	log("--- Show libraries");
    	StringBuffer sb = new StringBuffer();
    	sb.append("Libraries : \n\n") ;
    	String[] libraries = getLibraries();
    	if ( libraries != null && libraries.length > 0 ) {
    		for ( String s : libraries ) {
    			sb.append(s + "\n");
    		}
    	}
    	else {
    		sb.append("No library !\n");
    	}
    	log("--- Show libraries : " + libraries.length );
    	MsgBox.info(sb.toString());
    }
    
    /**
     * Test the Database connection
     */
    private void actionTestConnection() 
    {
    	Shell shell = Util.cursorWait();

    	Connection con = getConnection();
    	if ( con != null )
    	{
    		boolean ok = true ;
	    	String catalog = "" ;
	    	boolean autocommit = false ;
	    	
			try {
				catalog = con.getCatalog() ;
			} catch (SQLException e) {
				ok = false ;
				String s = "Cannot get 'catalog' from the connection ! " ;
				logError(s);
				MsgBox.error(s, e);
			}
	
			try {
				autocommit = con.getAutoCommit() ;
			} catch (SQLException e) {
				ok = false ;
				String s = "Cannot get 'auto-commit' from the connection ! " ;
				logError(s);
				MsgBox.error(s, e);
			} 
			
			closeConnection(con);

			if ( ok ) {
				MsgBox.info("Connection is OK.\n\n" 
						+ " . catalog = '" + catalog + "' \n" 
						+ " . auto-commit = '" + autocommit + "' \n" );
			}
    	}
		Util.cursorArrow(shell);
    }

    private void actionGetInformations() 
    {
    	Shell shell = Util.cursorWait();
    	
		Connection con = getConnection();
		if ( con != null )
		{
            try {
				DatabaseMetaData dbmd = con.getMetaData();
				
			    _InfoURL.setText( dbmd.getURL() );
			    _InfoProdName.setText( dbmd.getDatabaseProductName() );
			    _InfoProdVer.setText( dbmd.getDatabaseProductVersion() );
			    _InfoDriverName.setText( dbmd.getDriverName() );
			    _InfoDriverVer.setText( dbmd.getDriverVersion() );
			    _InfoMaxConn.setText( ""+dbmd.getMaxConnections() );
			    _InfoUser.setText(dbmd.getUserName());
			    _InfoIsolation.setText( ""+dbmd.getDefaultTransactionIsolation() );
			    
			    _InfoCatalogTerm.setText( dbmd.getCatalogTerm() );
			    _InfoCatalogSepar.setText( dbmd.getCatalogSeparator() );
				
			    _InfoSchemaTerm.setText( dbmd.getSchemaTerm() );
			    _InfoSearchEscape.setText( dbmd.getSearchStringEscape() );
			    
			    con.close();
			    
			} catch (SQLException e) {
		    	logException(e);				
				MsgBox.error("SQLException : ", e );
			} finally {
				closeConnection(con);
			}
            
		}
   		// else : nothing to do ( message already shown )

   		Util.cursorArrow(shell);
    }

    private void actionGetMetaData(int whatElse) 
    {
    	Shell shell = Util.cursorWait();
		_tMetaData.setText("");
    	Connection con = getConnection();
    	if ( con != null )
    	{
    		log("connected.");

    		DatabaseConfiguration db = getCurrentDatabaseConfig(true) ;
            if ( null == db ) return ;
    		String sCatalog = db.getMetadataCatalog() ;
    		String sSchema  = db.getMetadataSchema() ;
    		String sTableNamePattern = db.getMetadataTableNamePattern() ;
    		String[] tableTypes = db.getMetadataTableTypesArray() ;
    		String sTableNameInclude = db.getMetadataTableNameInclude() ;
    		String sTableNameExclude = db.getMetadataTableNameExclude() ;
    		
    		log("get DatabaseMetaData from connection...");
			DatabaseMetaData dbmd = null ;
			List<TableMetaData> tables = null ;
			try {
				//--- Get the database Meta-Data
				dbmd = con.getMetaData();		

			} catch (SQLException e) {
		    	logException(e);
				MsgBox.error("Cannot get meta-data ! ", e );
			} 

			log("DatabaseMetaData instance ready.");
			
			TelosysToolsLogger logger = _editor.getTextWidgetLogger();
    		MetaDataManager metaDataManager = new MetaDataManager(logger);

    		//--- Get the tables
    		log("get tables from DatabaseMetaData ... ");
    		log(" . catalog = " + quote(sCatalog) );
    		log(" . schema  = " + quote(sSchema) );
    		log(" . pattern = " + quote(sTableNamePattern) );
    		log(" . types   = " + arrayToString(tableTypes) );
    		log(" . include   = " + quote(sTableNameInclude) );
    		log(" . exclude   = " + quote(sTableNameExclude) );
    		try {
				tables = metaDataManager.getTables(dbmd, sCatalog, sSchema, sTableNamePattern, tableTypes, sTableNameInclude, sTableNameExclude );
			} catch (SQLException e) {
				tables = null ;
		    	logException(e);
				MsgBox.error("Cannot get tables from meta-data ! ", e );
			} 
			
			if ( tables != null ) 
			{
				switch ( whatElse )
				{
				case GET_COLUMNS :
		    		log("get columns ... ");
					getMetaDataColumns( metaDataManager, dbmd, tables, sCatalog, sSchema);
					break;
					
				case GET_PRIMARY_KEYS :
		    		log("get primary keys ... ");
					getMetaDataPrimaryKeys( metaDataManager, dbmd, tables, sCatalog, sSchema);
					break;
					
				case GET_FOREIGN_KEYS :
		    		log("get foreign keys ... ");
					getMetaDataForeignKeys( metaDataManager, dbmd, tables, sCatalog, sSchema);
					break;
					
				case GET_CATALOGS :
		    		log("get catalogs ... ");
					getMetaDataCatalogs( metaDataManager, dbmd);
					break;
					
				case GET_SCHEMAS :
		    		log("get schemas ... ");
					getMetaDataSchemas( metaDataManager, dbmd);
					break;
					
				default :
					printMetaDataTables(tables);
					break;
				}
			}
			closeConnection(con);
    	}
		Util.cursorArrow(shell);
    }
    
    private void getMetaDataColumns(MetaDataManager metaDataManager, DatabaseMetaData dbmd, 
    		List<TableMetaData> tables, String sCatalog, String sSchema) 
    {
    	if ( tables.size() > 0 ) {    		
			//--- Get the columns for each table
			for ( TableMetaData t : tables ) {
				String sTableName = t.getTableName();
				try {
					List<ColumnMetaData> columns = metaDataManager.getColumns(dbmd, sCatalog, sSchema, sTableName );
					printMetaDataColumns(sTableName, columns);
				} catch (SQLException e) {
			    	logException(e);
					MsgBox.error("Cannot get columns for table '" + sTableName + "'", e );
					break;
				} catch (Exception e) {
			    	logException(e);
					MsgBox.error("Cannot get columns for table '" + sTableName + "'", e );
					break;
				}
			}
    	}
    	else {
    		_tMetaData.append("No table => no column.\n");
    	}
    }
    
    private void getMetaDataPrimaryKeys(MetaDataManager metaDataManager, DatabaseMetaData dbmd, 
    		List<TableMetaData> tables, String sCatalog, String sSchema) 
    {
    	if ( tables.size() > 0 ) {
    		
			//--- Get the columns for each table
			for ( TableMetaData t : tables ) {
				String sTableName = t.getTableName();
				try {
					List<PrimaryKeyColumnMetaData> columns = metaDataManager.getPKColumns(dbmd, sCatalog, sSchema, sTableName );
					printMetaDataPrimaryKeys(sTableName, columns);
				} catch (SQLException e) {
			    	logException(e);
					MsgBox.error("Cannot get Primary Key for table '" + sTableName + "'", e );
					break;
				} catch (Exception e) {
			    	logException(e);
					MsgBox.error("Cannot get Primary Key for table '" + sTableName + "'", e );
					break;
				} 
			}
		}
		else {
			_tMetaData.append("No table => no primary key.\n");
		}		
    }

    private void getMetaDataForeignKeys(MetaDataManager metaDataManager, DatabaseMetaData dbmd, 
    		List<TableMetaData> tables, String sCatalog, String sSchema) 
    {
    	if ( tables.size() > 0 ) {
    		
			//--- Get the columns for each table
			for ( TableMetaData t : tables ) {
				String sTableName = t.getTableName();
				try {
					List<ForeignKeyColumnMetaData> columns = metaDataManager.getFKColumns(dbmd, sCatalog, sSchema, sTableName );
					printMetaDataForeignKeys(sTableName, columns);
				} catch (SQLException e) {
			    	logException(e);
					MsgBox.error("Cannot get Foreign Keys for table '" + sTableName + "'", e );
					break;
				} catch (Exception e) {
			    	logException(e);
					MsgBox.error("Cannot get Foreign Keys for table '" + sTableName + "'", e );
					break;
				} 
			}
    	}
    	else {
    		_tMetaData.append("No table => no foreign key.\n");    		
    	}
    }
    
    //----------------------------------------------------------------------------------------------------
    private void getMetaDataCatalogs(MetaDataManager metaDataManager, DatabaseMetaData dbmd ) 
    {
    	try {
			List<String> list = metaDataManager.getCatalogs(dbmd);
			printMetaDataCatalogs(list);
		} catch (SQLException e) {
	    	logException(e);
			MsgBox.error("Cannot get catalogs", e );
		} catch (Exception e) {
	    	logException(e);
			MsgBox.error("Cannot get catalogs", e );
		}
    }
    private void printMetaDataCatalogs(List<String> list)
    {
    	if ( list.size() > 0 ) {
    		for ( String s : list ) {
    			_tMetaData.append(" . " + s + " \n");
    		}
    	}
    	else {
			_tMetaData.append("No catalog.\n");
    	}
    }

    //----------------------------------------------------------------------------------------------------
    private void getMetaDataSchemas(MetaDataManager metaDataManager, DatabaseMetaData dbmd ) 
    {
    	try {
			List<SchemaMetaData> list = metaDataManager.getSchemas(dbmd);
			printMetaDataSchemas(list);
		} catch (SQLException e) {
	    	logException(e);
			MsgBox.error("Cannot get schemas", e );
		} catch (Exception e) {
	    	logException(e);
			MsgBox.error("Cannot get schemas", e );
		}
    }
    private void printMetaDataSchemas(List<SchemaMetaData> list)
    {
    	if ( list.size() > 0 ) {
			for ( SchemaMetaData schema : list ) {
				_tMetaData.append(" . " + schema.getSchemaName() + " ( catalog : "+ schema.getSchemaName() + " ) \n");
			}
    	}
    	else {
			_tMetaData.append("No schema.\n");
    	}
    }
    //----------------------------------------------------------------------------------------------------
    
    private void printMetaDataTables(List<TableMetaData> tables)
    {
    	if ( tables.size() > 0 ) {
			for ( TableMetaData t : tables ) {
				_tMetaData.append(" . " + t.getTableName() + " (" + t.getTableType() + ") "
					+ " catalog = '" + t.getCatalogName() + "'"
					+ " schema = '" + t.getSchemaName() + "'" 
					+ "\n");
	
			}
    	}
    	else {
			_tMetaData.append("No table.\n");
    	}
    }
    private void printMetaDataColumns(String tableName, List<ColumnMetaData> columns)
    {
    	_tMetaData.append("Table '" + tableName + "' : \n");
		for ( ColumnMetaData c : columns ) {
			String s = 
					"[" +c.getOrdinalPosition() + "]"
					+ " " + c.getColumnName() + " : " 
					+ "  " + c.getDbTypeName() 
					+ "  (jdbc:" + c.getJdbcTypeCode()+")" 
					+ "  size=" + c.getSize()
					+ "  " + ( c.isNotNull() ? "NOT NULL" : "" )
					+ "  charOctetLength=" + c.getCharOctetLength()
					+ "  decimalDigits=" + c.getDecimalDigits()
					+ "  numPrecRadix=" + c.getNumPrecRadix()
					+ "  defaultValue=" + c.getDefaultValue()
					;
			
			_tMetaData.append(" . " + s + " \n");
		}
    	_tMetaData.append("\n");
    }
    
    private void printMetaDataPrimaryKeys(String tableName, List<PrimaryKeyColumnMetaData> columns)
    {
    	_tMetaData.append("Table '" + tableName + "' : \n");
		for ( PrimaryKeyColumnMetaData c : columns ) {
			String s = 
					" " + c.getPkName() + " : " 
					+ " [" + c.getPkSequence() + "]"
					+ "  " + c.getColumnName()
					;
			
			_tMetaData.append(" . " + s + " \n");
		}
    	_tMetaData.append("\n");
    }
    
    private void printMetaDataForeignKeys(String tableName, List<ForeignKeyColumnMetaData> columns)
    {
    	_tMetaData.append("Table '" + tableName + "' : \n");
		for ( ForeignKeyColumnMetaData c : columns ) {
			
			String s = 
					" " + c.getFkName() + " : " 
					+ c.getFkTableName() + "." + c.getFkColumnName()
					+ " --> " 
					+ c.getPkTableName()  + "." + c.getPkColumnName()
					+ "  ( PK : " + c.getPkName() + ")"
					;
			
			_tMetaData.append(" . " + s + " \n");
		}
    	_tMetaData.append("\n");
    }
    
    private IProject getEclipseProject()
    {
        IProject project = _editor.getProject();
        if ( project == null )
        {
        	MsgBox.error("actionGenerateRepository() : Cannot get project ");
        }
    	return project ;
    }
    
    private void syncRepoFolder()
    {
    	IProject project = getEclipseProject();
//    	ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig(project);
//    	ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig(project); // v 3.0.0
//    	TelosysToolsCfg telosysToolsCfg = projectConfig.getTelosysToolsCfg();
    	TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig(project); // v 3.0.0
		
    	log("repositoriesFolder = " + telosysToolsCfg.getModelsFolder() );
		IFolder modelsFolder = project.getFolder(telosysToolsCfg.getModelsFolder());
		
		log("is repositories Folder Synchronized = " + modelsFolder.isSynchronized(IResource.DEPTH_INFINITE) );
		try {
			modelsFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			log("refreshLocal : CoreException " );
		}
		log("is repositories Folder Synchronized = " + modelsFolder.isSynchronized(IResource.DEPTH_INFINITE) );
    	
    }

    //---------------------------------------------------------------------------------------------------------
    /**
     * Add a new database
     */
    private void actionNewDatabase() 
    {
		Display display = Display.getCurrent();
		 
		//--- New DialogBox instance
		DialogBoxNewDatabase dialogBox = new DialogBoxNewDatabase(display.getActiveShell(), 
					_editor.getDatabasesConfigurations() );
		
		//--- Open the Dialog Box Window ( Modal Window )
		log( "openDialogBox() : before dialog.open() ----- " );		
		int iRet = dialogBox.open();
		log( "openDialogBox() : after dialog.open() ----- " );		
		
		//--- Use result if "Ok" has been pressed
		if ( Window.OK == iRet ) {
			int databaseIdSelected = dialogBox.getDatabaseId() ;
			DatabaseType databaseTypeSelected = dialogBox.getDatabaseType();
//			MsgBox.info("Dialog Box 'Ok' : "
//					+ "\n db id = "   + dbId 
//					+ "\n db type = " + dbType );
			if ( databaseIdSelected < 0 ) {
				MsgBox.error("Invalid database id " + databaseIdSelected );
				return ;
			}
			if ( databaseTypeSelected == null ) {
				MsgBox.error("No database type selected ! " );
				return ;
			}
			// Everything ok : add the database
			DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(databaseIdSelected, databaseTypeSelected);
//		    //--- Init Connection info (must be initialized)
//			databaseConfiguration.setDatabaseId(dbId);
//			databaseConfiguration.setDatabaseName( databaseTypeSelected.getTypeName() + " database") ;
//			databaseConfiguration.setDriverClass( databaseTypeSelected.getDriver() ) ;
//			databaseConfiguration.setJdbcUrl( databaseTypeSelected.getUrl() ) ;
//			databaseConfiguration.setUser(TO_BE_DEFINED) ;
//			databaseConfiguration.setPassword(TO_BE_DEFINED);
//			databaseConfiguration.setPoolSize(1);
//			databaseConfiguration.setIsolationLevel("");
//
//		    //--- Init Meta-Data info (must be initialized)
//			databaseConfiguration.setMetadataCatalog( databaseTypeSelected.getMetadataCatalog() ) ;
//			databaseConfiguration.setMetadataSchema(TO_BE_DEFINED);
//			databaseConfiguration.setMetadataTableNamePattern("%") ;
//			databaseConfiguration.setMetadataTableTypes("TABLE");
			
	    	DatabasesConfigurations databasesConfigurations = _editor.getDatabasesConfigurations() ;
	    	databasesConfigurations.storeDatabaseConfiguration(databaseConfiguration); 
	    	setDirty();
	    	clearFields();
	    	populateComboDatabases() ;
	    	selectDatabaseInCombo(databaseIdSelected) ; // Select the new database
		}
		else {
//			MsgBox.info("Dialog Box 'Cancel' ");
		}		
    }
    
    private void selectFirstDatabaseInCombo() 
    {
        if ( _ComboDatabases.getItemCount() > 0 ) {
        	_ComboDatabases.select(0);
        	populateDatabaseConfigFields(_ComboDatabases);
        }

    }
    private void selectDatabaseInCombo(int databaseId) 
    {
    	String sDbId = databaseId + " "; // item format = "ID - Name"
    	int n =_ComboDatabases.getItemCount() ;
    	for ( int i = 0 ; i < n ; i++ ) {
    		String s = _ComboDatabases.getItem(i);
    		if ( s.startsWith(sDbId) ) {
    			_ComboDatabases.select(i);
            	populateDatabaseConfigFields(_ComboDatabases);
    			return;
    		}
    	}
    }
    
    //---------------------------------------------------------------------------------------------------------
    /**
     * Delete current database
     */
    private void actionDeleteDatabase() 
    {
    	DatabaseConfiguration db = getCurrentDatabaseConfig(false);
    	if ( db != null ) {
    		String sMsg = "Do you realy want to delete this database configuration ?" 
    			+ "\n\n" 
    			+ ". Id : " + db.getDatabaseId() 
    			+ "\n" 
    			+ ". Name : " + db.getDatabaseName()  ;
    		if ( MsgBox.confirm(" Confirm generation", sMsg) )
    		{
    	    	DatabasesConfigurations databasesConfigurations = _editor.getDatabasesConfigurations() ;
    	    	databasesConfigurations.removeDatabaseConfiguration( db.getDatabaseId() ) ;
    	    	setDirty();
    	    	clearFields();
    	    	populateComboDatabases() ;
    			selectFirstDatabaseInCombo();
    		}
    	}
    	else {
    		MsgBox.info("No current database.");
    	}
    }
    //---------------------------------------------------------------------------------------------------------
    /**
     * Generates the repository file 
     */
    private void actionGenerateRepository() 
    {
        // MsgBox.debug("Generate Repository");
        boolean repositoryCreated = false ;
        String sRepositoryFile = null ;
        
        IProject project = _editor.getProject();
        if ( project == null ) {
        	MsgBox.error("actionGenerateRepository() : Cannot get project ");
        	return ;
        }

        TelosysToolsLogger logger = _editor.getTextWidgetLogger();	
		
        DatabaseConfiguration db = getCurrentDatabaseConfig(true) ;
        if ( null == db ) return ;
		String sDatabaseName = db.getDatabaseName();		
		sRepositoryFile = getRepositoryFileName( db.getDatabaseName() ) ;
		
		IFile repositoryFile = project.getFile(sRepositoryFile);

		String sMsg = "This operation will replace the current version of the repository if it exists." 
				+ "\n\n" + "Repository file : \n" + sRepositoryFile 
				+ "\n\n" + "Database name : \n" + sDatabaseName 
				+ "\n\n" + "Launch the generation ?";
		if ( MsgBox.confirm(" Confirm generation", sMsg) )
		{
			Shell shell = Util.cursorWait();
			
// v 3.0.0 : Connection is now useless => removed
//    		Connection con = getConnection() ;
//    		if ( con != null )
//    		{
				try {
					//repositoryCreated = generateRepository(con, db, repositoryFile, logger ) ; 
					repositoryCreated = generateRepository(db, repositoryFile, logger ) ; // v 3.0.0
				} 
				catch (Exception e)  // Catch ALL exceptions 
				{
			    	logException(e);					
					//MsgBox.error("Exception : " + e.getClass() + " \n\n" + e.getMessage() ) ;
					MsgBox.error("Cannot generate repository", e) ;
				} 
//				finally {
//					closeConnection(con);
//				}

				if ( repositoryCreated )
		        {
		        	syncRepoFolder();
					Util.cursorArrow(shell);
					MsgBox.info("Repository generated.\n\nSee file " + sRepositoryFile );
		        }
				else
				{
					Util.cursorArrow(shell);
				}
//			}
    		// if connection is null message already shown
//			Util.cursorArrow(shell);
		}
    }
    
    private boolean generateRepository(//Connection con, 
    		DatabaseConfiguration db, 
    		IFile repositoryFile,
    		TelosysToolsLogger logger ) 
    {
		RepositoryModel repo = null ;

		//--- 1) Generate the repository in memory
		try {
			ConnectionManager connectionManager = createConnectionManager();
			RepositoryGenerator generator = new RepositoryGenerator(connectionManager, RepositoryRulesProvider.getRepositoryRules(), logger) ;	
			
			repo = generator.generate(db); // ver 2.1.1
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot generate.", e);
			return false ;
		}
			
		//--- 2) Save the repository in the file
		try {
			File file = EclipseWksUtil.toFile(repositoryFile);
			logger.info("Saving repository in file " + file.getAbsolutePath() );
			//StandardFilePersistenceManager pm = new StandardFilePersistenceManager(file, logger);
			PersistenceManager pm = PersistenceManagerFactory.createPersistenceManager(file, logger);
			pm.save(repo);
			logger.info("Repository saved.");
			
		} catch (TelosysToolsException e) {
			MsgBox.error("Cannot save file", e);
			return false ;
		}

		return true ;
    }

    /**
     * Updates the repository and returns the number of entities changed
     * @param con
     * @param db
     * @param projectConfig
     * @param logger
     * @return
     * @throws TelosysToolsException
     */
    //private int updateRepository(Connection con, DatabaseConfiguration db, ProjectConfig projectConfig, TelosysToolsLogger logger ) 
    private int updateRepository(DatabaseConfiguration db, TelosysToolsLogger logger ) // v 3.0.0
    	throws TelosysToolsException
    {
		
		//--- 1) LOAD the repository from the file
		File repositoryFile = getRepositoryFile( db.getDatabaseName() );
		logger.info("Load repository from file " + repositoryFile.getAbsolutePath());
		//StandardFilePersistenceManager persistenceManager = new StandardFilePersistenceManager(repositoryFile, logger);
		PersistenceManager persistenceManager = PersistenceManagerFactory.createPersistenceManager(repositoryFile, logger);
		RepositoryModel repositoryModel = persistenceManager.load();		
		logger.info("Repository loaded : " + repositoryModel.getNumberOfEntities() + " entitie(s)"  );
		
		//--- 2) UPDATE the repository in memory
		File updateLogFile = getUpdateLogFile( repositoryFile.getAbsolutePath() );
		UpdateLogWriter updateLogger = new UpdateLogWriter( updateLogFile ) ;
		
		ConnectionManager connectionManager = createConnectionManager();
		RepositoryUpdator updator = new RepositoryUpdator(connectionManager, RepositoryRulesProvider.getRepositoryRules(), logger,  updateLogger);// #LGU ver 2.1.1
		
		ChangeLog changeLog = updator.updateRepository(db, repositoryModel); // #LGU ver 2.1.1
		
		//--- 3) SAVE the repository in the file
		logger.info("Save repository in file " + repositoryFile.getAbsolutePath());
		persistenceManager.save(repositoryModel);
		logger.info("Repository saved.");
		
		return changeLog.getNumberOfEntities() ;
    }
    
    /**
     * Updates the repository file 
     */
    private void actionUpdateRepository() 
    {
    	int nbChanges = 0 ;
    	
        boolean repositoryUpdated = false ;
        
        IProject project = _editor.getProject();
        if ( project == null )
        {
        	MsgBox.error("actionGenerateRepository() : Cannot get project ");
        	return ;
        }

        DatabaseConfiguration db = getCurrentDatabaseConfig(true) ;
        if ( null == db ) return ;
		String sDatabaseName = db.getDatabaseName();
		String sRepositoryFile = getRepositoryFileName( db.getDatabaseName() ) ;
		
		//--- Check repository existence
		IFile iFile = project.getFile(sRepositoryFile);
		if ( ! iFile.exists() )
		{
        	MsgBox.info("The repository file doesn't exist => cannot update."
        			+ "\n\nRepository file : \n" + sRepositoryFile );
        	return ;
		}
        
//        ConnectionManager cm = createConnectionManager();
        
//        if ( db != null && cm != null )
		if ( db != null )
        {
			String sMsg = "This operation will update the current repository if it exists." 
				+ "\n\n" + "Repository file : \n" + sRepositoryFile 
				+ "\n\n" + "Database name : \n" + sDatabaseName 
				+ "\n\n" + "Launch update ?";
			if ( MsgBox.confirm(" Confirm ", sMsg) )
			{
				Shell shell = Util.cursorWait();
				
// v 3.0.0 : connection is now useless				
//	    		Connection con = getConnection();
//	    		if ( con != null )
//	    		{
	    	        try {
// v 3.0.0 : projectConfig is now useless
//	    	        	ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig(project);
	    	        	TelosysToolsLogger logger = _editor.getTextWidgetLogger();
	    	        	
	    	        	//nbChanges = updateRepository(con, db, projectConfig, logger ) ;
	    	        	nbChanges = updateRepository(db, logger ) ; // v 3.0.0
							
    	        		repositoryUpdated = true ;
					}
	    	        catch ( Throwable e ) // Catch ALL exceptions 
					{
						MsgBox.error("Exception : " + e.getClass() + " \n\n" + e.getMessage() ) ;
					}
//	    	        finally
//	    	        {
//						closeConnection(con);
//	    	        }
//	    		}
	    		// else : no connection : nothing to do ( message already showned )

	    		if ( repositoryUpdated )
	            {
	            	syncRepoFolder();

	            	Util.cursorArrow(shell);

	            	String msgChanges = "No change." ;
	            	if ( nbChanges > 0 )
	            	{
	            		msgChanges = nbChanges + " change(s).\n\n"
	            		  			+ "See the update log file for details.";
	            	}
	                MsgBox.info("Repository updated.\n"
	                		+ "\n" 
	                		+ "Repository file : \n" 
	                		+ sRepositoryFile + "\n"
	                		+ "\n"
	                		+ msgChanges );
	            }
	    		else
	    		{
	    			Util.cursorArrow(shell);
	    		}
			}
        }
    }

}