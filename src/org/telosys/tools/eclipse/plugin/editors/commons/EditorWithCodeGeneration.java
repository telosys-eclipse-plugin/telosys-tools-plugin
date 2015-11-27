package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.generic.model.Model;

public interface EditorWithCodeGeneration {

	public IProject getProject();

	public void refreshAllTargetsTablesFromConfigFile() ;

	public String getCurrentBundleName() ;
	
	public void setCurrentBundleName(String bundleName) ;
	
	public Model getModel();
	
	public TelosysToolsLogger getLogger() ;
}
