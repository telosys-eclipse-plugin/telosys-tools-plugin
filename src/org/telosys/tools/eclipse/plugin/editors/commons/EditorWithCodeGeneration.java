package org.telosys.tools.eclipse.plugin.editors.commons;

import org.eclipse.core.resources.IProject;

public interface EditorWithCodeGeneration {

	public IProject getProject();

	public void refreshAllTargetsTablesFromConfigFile() ;

	public String getCurrentBundleName() ;
	
	public void setCurrentBundleName(String bundleName) ;
	
}
