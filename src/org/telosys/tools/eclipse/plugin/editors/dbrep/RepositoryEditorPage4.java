package org.telosys.tools.eclipse.plugin.editors.dbrep;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.repository.model.RepositoryModel;

/**
 * Page 3 of the editor <br>
 * 
 * Shows the project configuration 
 * 
 */
/* package */ class RepositoryEditorPage4 extends RepositoryEditorPage 
{

	//--------------------------------------------------------------------------------------------------
	/**
	 * Constructor
	 * @param editor
	 * @param id
	 * @param title
	 */
	public RepositoryEditorPage4(FormEditor editor, String id, String title) {
		super(editor, id, title);
		PluginLogger.log(this, "constructor(.., '"+id+"', '"+ title +"')..." );		
	}

	//----------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		PluginLogger.log(this, "init(..,..) : site id = '" + site.getId() + "'" );
		PluginLogger.log(this, "init(..,..) : input name = '" + input.getName() + "'" );
	}

	//--------------------------------------------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		
		log(this, "createFormContent(..)..." );
		
//		ProjectConfig config = getProjectConfig();
//		if ( config == null )
//		{
//			MsgBox.error("ProjectConfig is null");
//			return;
//		}
		TelosysToolsCfg telosysToolsCfg = getProjectConfig(); // v 3.0.0
		
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
		
		Composite body = form.getBody(); 
		// body.getClass() --> org.eclipse.ui.forms.widgets.LayoutComposite
		// No Layout for the body at this moment
		
		//--- Set a LAYOUT to the BODY
		GridLayout bodyLayout = new GridLayout();	
		bodyLayout.numColumns = 2 ;
		bodyLayout.makeColumnsEqualWidth = false ;
		
		body.setLayout( bodyLayout );
		
		//---------------------------------------------------------------
		// Line 0 - Columns 1 & 2 (span) : The page title
		//---------------------------------------------------------------
		GridData gdTitle = new GridData(GridData.FILL_HORIZONTAL);
		gdTitle.horizontalSpan = 2;		
		Label labelTitle = Util.setPageTitle(body, this.getTitle() ) ; // Title defined in the constructor
		labelTitle.setLayoutData(gdTitle);
		
		
		//TelosysToolsCfg telosysToolsCfg = config.getTelosysToolsCfg();
		
		addConfigRow(body, "", "" );
		//addConfigRow(body, "Project name :", config.getProjectName() );
		addConfigRow(body, "Project name :", getProject().getName() ); // v 3.0.0
		
		//addConfigRow(body, "Workspace folder :", config.getWorkspaceFolder() );
		//addConfigRow(body, "Project folder :", config.getProjectFolder() );
		addConfigRow(body, "Project folder :", telosysToolsCfg.getProjectAbsolutePath() );

		addConfigRow(body, "Configuration file full path :", telosysToolsCfg.getCfgFileAbsolutePath() );

		addConfigRow(body, "Templates folder :", telosysToolsCfg.getTemplatesFolder() );
		addConfigRow(body, "Templates folder full path :", telosysToolsCfg.getTemplatesFolderAbsolutePath() );
		addConfigRow(body, "Repositories folder :", telosysToolsCfg.getRepositoriesFolder() );
		
		addConfigRow(body, "", "" );
		
		RepositoryModel repositoryModel = getRepositoryModel();
		addConfigRow(body, "Database used to generate the model", "" );
		addConfigRow(body, ". database ID :", ""+repositoryModel.getDatabaseId() );
		addConfigRow(body, ". database name :", ""+repositoryModel.getDatabaseName() );
		addConfigRow(body, ". database product name :", ""+repositoryModel.getDatabaseProductName() );

		//addConfigRow(body, "Generator version", GeneratorConst.GENERATOR_VERSION );
		
	}
	
	//----------------------------------------------------------------------------------------------
	private void addConfigRow(Composite c, String s1, String s2)
	{
		Label label1 = new Label( c, SWT.LEFT );
		label1.setText(s1) ;

		Label label2 = new Label( c, SWT.LEFT );
		label2.setText(s2) ;
	}

}