package org.telosys.tools.eclipse.plugin.editors.dsl.model;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.telosys.tools.dsl.loader.ModelLoader;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.editors.commons.AbstractModelEditorPage;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.dsl.parser.model.DomainModelInfo;


/**
 * First page of the editor : Model attributes mapping and foreign keys <br>
 *  
 */
///* package */  class ModelEditorPage2 extends ModelEditorPage 
/* package */  class ModelEditorPageModelInfo extends AbstractModelEditorPage 
{

//	private final static int TEXT_HEIGHT = 24 ;
//	
//	// Style for basic tables 
//	private final static int BASIC_TABLE_STYLE = SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL 
//		| SWT.SINGLE | SWT.HIDE_SELECTION | SWT.FULL_SELECTION ;
//	// SWT.SINGLE : Create a single-select table
//	// SWT.MULTI  : Create a multiselect table
//	// SWT.FULL_SELECTION : The selection is expanded to fill an entire row
//	// SWT.HIDE_SELECTION : The selection is hidden when focus is lost

	//-----------------------------------------------------------------

	private boolean  _bPopulateInProgress = false ;
	
	//-----------------------------------------------------------------
	//private Entity _currentEntity = null ; 
	private EntityInDbModel _currentEntity = null ; // v 3.0.0
	
	//-----------------------------------------------------------------
	
	private Text       _tFileName ;
	private Text       _tName ;
	private Text       _tVersion ;
	private Text       _tDescription ;
	

	//----------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title 
	 */
	public ModelEditorPageModelInfo(FormEditor editor, String id, String title ) {
		super(editor, id, title);
		log(this, "constructor(.., '"+id+"', '"+ title +"')..." );
	}
	
	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		log(this, "init(..,..)..." );
		log(this, "init(..,..) : site id = '" + site.getId() 
				+ "'  input name = '" + input.getName() + "'");
	}
	
	//----------------------------------------------------------------------------------------------
	protected boolean isPopulateInProgress() {
		return _bPopulateInProgress ;
	}
	
	private final static int BEAN_JAVA_CLASS      = 1 ;
	
	protected String getModelValue( int id ) {
		if ( _currentEntity != null )
		{
			switch (id) 
			{
			//case BEAN_JAVA_CLASS :      return _currentEntity.getBeanJavaClass() ;
			case BEAN_JAVA_CLASS :      return _currentEntity.getClassName() ;
			}
			MsgBox.error("getModelValue("+id+") : unknown id !");
		}
		else
		{
			MsgBox.error("getModelValue("+id+") : current entity is null !");
		}
		return "" ;
	}
	
	protected void setModelValue( int id, String v ) {		
		if ( _currentEntity != null )
		{
			switch (id) 
			{
			case BEAN_JAVA_CLASS :      
				//_currentEntity.setBeanJavaClass(v) ; 
				_currentEntity.setClassName(v) ; 
				break ;
			default : 
				MsgBox.error("getModelValue("+id+") : unknown id !"); 
				return ;
			}
		}
		else
		{
			MsgBox.error("getModelValue("+id+") : current entity is null !");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..)..." );
//		Control pageControl = getPartControl();
//		
//		if ( pageControl != null ) {
//			log(this, "createFormContent(..) : getPartControl() != null " );
//		}
//		else {
//			log(this, "createFormContent(..) : getPartControl() is null !!! " );
//			return ;
//		}
//		
//		if ( pageControl instanceof Composite ) {
//			log(this, "- pageControl is a Composite  " );
//			// Yes pageControl is a Composite
//
//			log(this, "- pageControl class = " + pageControl.getClass() );
//			// class = org.eclipse.ui.forms.widgets.ScrolledForm
//			
//			Composite pageComposite = (Composite) pageControl ;
//			Layout layout = pageComposite.getLayout();			
//			log(this, "- pageControl layout class = " + layout.getClass() );
//			// layout = org.eclipse.swt.custom.ScrolledCompositeLayout
//		}
//		else {
//			log(this, "- pageControl() is NOT a Composite !!! " );
//		}

//		ScrolledForm scrolledForm = managedForm.getForm();
//		
//		//--- Page title 
//		//scrolledForm.setText( _repEditor.getDatabaseTitle() );
//		
//		scrolledForm.setExpandHorizontal(true); // TODO ?????
//		
//		Specify the minimum width at which the ScrolledComposite will begin scrolling 
//		the content with the horizontal scroll bar. 
//		This value is only relevant if setExpandHorizontal(true) has been set. 
//		scrolledForm.setMinWidth(700);

//		form.setExpandHorizontal(false); // tout bout à bout !
//		form.setExpandVertical(false);
		
		//scrolledForm.setMinWidth(700);
		
//		Composite scrolledFormBody = scrolledForm.getBody();
//		log(this, "- body class = " + scrolledFormBody.getClass() );
//		
		
//		//Layout bodyLayout = new RowLayout(SWT.VERTICAL);
//		//--- New layout in v 1.0 : Grid with only 1 column
//		GridLayout bodyLayout = new GridLayout(1, false); 
//		
//		// marginWidth specifies the number of pixels of horizontal margin 
//		// that will be placed along the left and right edges of the layout. The default value is 5.
//		//bodyLayout.marginWidth = 20 ;
//		bodyLayout.marginWidth = ModelEditor.LAYOUT_MARGIN_WIDTH ;
//		
////		scrolledFormBody.setLayout( bodyLayout );
		
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
		
		addRow(scrolledFormBody, "", "") ;
		_tFileName    = addLabelAndText(scrolledFormBody, "File : ") ;
//		_tFileName.setEnabled(false);
		_tFileName.setEditable(false);
		_tName        = addLabelAndText(scrolledFormBody, "Name : ") ;
		_tVersion     = addLabelAndText(scrolledFormBody, "Version : ") ;
		_tDescription = addLabelAndTextMulti(scrolledFormBody, "Description : ") ;
		//addRow(scrolledFormBody, "--", "+++") ;
		//---------------------------------------------------------------
		// Line 1 - Column 1 in the "body layout"
		//---------------------------------------------------------------
		//createPageHeaderGroup(scrolledFormBody);
		
//		//---------------------------------------------------------------
//		// Line 2 - Column 1 in the "body layout"
//		//---------------------------------------------------------------
//		//--- Tab Folder 
//		Composite composite = null ;
//
//		composite = new Composite(scrolledFormBody, SWT.NONE); // Add it in the form body
//		composite.setLayout(new FillLayout());
//		
//		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL)); // new #LGU
//		//composite.setLocation(GROUP_X, 100);
//		
//		// #LGU changed for tests
//		//composite.setSize(760, 200); 
//		
//		composite.setBackground( getBackgroundColor() );
//		
//
//		TabFolder tabFolder = new TabFolder(composite, SWT.NONE);
//		
//		//tabFolder.setLocation(GROUP_X, 100);
//		//tabFolder.setSize(400, 200);
//		tabFolder.setBackground( getBackgroundColor() ); // No effect : cannot change the TabFolder color 
//
//		//--- Creates the 3 tabs 
//		createTabFolderMapping(tabFolder);
//
//		//--------------------------------------------------------------
//		
		populateFields() ;
	}
	
	private void populateFields() {
		log(this, "populateFields()");
		String modelFileAbsolutePath = this.getModelEditor().getFileAbsolutePath();
		_tFileName.setText( modelFileAbsolutePath );
//		Model model = getModel();
//		if ( model != null ) {
//			_tName.setText(model.getName());
//			_tVersion.setText(model.getVersion());
//			_tDescription.setText(model.getDescription());
//		}
//		else {
//			MsgBox.error("Model is null !");
//		}
		File modelFile = new File(modelFileAbsolutePath);
		
		ModelLoader modelLoader = new ModelLoader();
		DomainModelInfo modelInfo = modelLoader.loadModelInformation(modelFile);
		if ( modelInfo != null ) {
			_tName.setText(modelInfo.getName());
			_tVersion.setText(modelInfo.getVersion());
			_tDescription.setText(modelInfo.getDescription());
		}
	}
	
//	//----------------------------------------------------------------------------------------------
//	private Composite createPageHeaderGroup(Composite scrolledFormBody) 
//	{
//		//--- The group "Database table"
//		//Group group1 = new Group(scrolledFormBody, SWT.NONE); // Add it in the form body
//		//Composite group1 = new Composite(scrolledFormBody, SWT.NONE | SWT.BORDER );
//		Composite group1 = new Composite(scrolledFormBody, SWT.NONE );
//		
//		//group1.setText("Database");
//		
//	    GridData gd = new GridData (GridData.FILL_HORIZONTAL);
////	    gd.heightHint    = 100 ;
//	    gd.heightHint    = 50 ;
//	    
//	    group1.setLayoutData(gd);
//		
//		//group1.setSize(700, 60);
//		//group1.setSize(900, 120); // No effect !
//		
//		//tab.setBackground(DAOColor.color(disp));
//		//group1.setLocation(GROUP_X, 20);
//        group1.setBackground( getBackgroundColor() );
//
//		int width = 200;
////		int x0 =  10;
////		int x1 = 220;
////		int x2 = 420;
//		int y1 = 2 ; //20 ;
//		int y2 = 22 ; //40 ;
//		int fillerWidth = 20 ;
//		
//    	Label label ;
//    	
//    	int x = 0 ;
//
//    	//--- The Combo Box for "table/entity" selection
//    	label = new Label(group1, SWT.NULL);
//    	//label = new Label(group1, SWT.BORDER );
//    	label.setText("Database table name : ");
//    	label.setSize(width, 20);
//    	label.setLocation(x, y1);
//
//		
//    	x = x + width + fillerWidth ;
//    	
//    	//--- The "Catalog"
//    	label = new Label(group1, SWT.NULL);
//    	//label = new Label(group1, SWT.BORDER );
//    	label.setText("Catalog : ");
//    	label.setSize(width, 20);
//    	label.setLocation(x, y1);
//
//    	_labelCatalog = new Label(group1, SWT.BORDER );
//    	_labelCatalog.setText("");
//    	_labelCatalog.setSize(width, TEXT_HEIGHT);
//    	_labelCatalog.setLocation(x, y2);
//
//    	x = x + width + fillerWidth ;
//
//    	//--- The "Schema"
//    	label = new Label(group1, SWT.NULL);
//    	//label = new Label(group1, SWT.BORDER );
//    	label.setText("Schema : ");
//    	label.setSize(width, 20);
//    	label.setLocation(x, y1);
//
//    	_labelSchema = new Label(group1, SWT.BORDER );
//    	_labelSchema.setText("");
//    	_labelSchema.setSize(width, TEXT_HEIGHT);
//    	_labelSchema.setLocation(x, y2);
//
//    	
//    	//--------------------------------------------------------------------------
//    	
//    	x = x + width + fillerWidth ;
//		
//		//-----  Entity Class name ( Java Bean class )
//    	label = new Label(group1, SWT.NULL);
//    	label.setText("Entity class name : ");
//    	label.setSize(width, 20);
//    	label.setLocation(x, y1);
//
//		_textJavaBeanClass = new Text(group1, SWT.BORDER);
//		_textJavaBeanClass.setText("");
//		_textJavaBeanClass.setSize(width, TEXT_HEIGHT);
//		_textJavaBeanClass.setLocation(x, y2);
////		_textJavaBeanClass.addModifyListener( new GenericModifyListener(this, BEAN_JAVA_CLASS) );
//		   	
//    	return group1;
//	}
	//----------------------------------------------------------------------------------------------
	private Text addLabelAndText(Composite c, String labelText ) {
		Label label1 = new Label( c, SWT.LEFT );
		label1.setText(labelText) ;

		// Standard text field 
		//Text text = new Text( c, SWT.LEFT );SWT.BORDER
		Text text = new Text( c, SWT.BORDER );
		GridData gd = new GridData();
		gd.widthHint = 400;
		text.setLayoutData ( gd );
		return text ;
	}
	//----------------------------------------------------------------------------------------------
	private Text addLabelAndTextMulti(Composite c, String labelText ) {
		Label label = new Label( c, SWT.LEFT );
		label.setText(labelText) ;
		GridData labelGridData = new GridData(); 
		labelGridData.verticalAlignment = SWT.BEGINNING; 
		labelGridData.grabExcessVerticalSpace = true; 
		label.setLayoutData(labelGridData);
		
		// Multi-line text field 
		Text text = new Text (c, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL );
		GridData textGridData = new GridData(400, 200); 
		textGridData.verticalAlignment = SWT.BEGINNING; 
		textGridData.grabExcessVerticalSpace = true; 
		text.setLayoutData ( textGridData );
		return text ;
	}
	//----------------------------------------------------------------------------------------------
	private void addRow(Composite c, String labelText1, String labelText2) {
		Label label = new Label( c, SWT.LEFT );
		label.setText(labelText1) ;
		
		label = new Label( c, SWT.LEFT );
		label.setText(labelText2) ;

	}
	//----------------------------------------------------------------------------------------------
}