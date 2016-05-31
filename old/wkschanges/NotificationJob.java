package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;

public class NotificationJob extends WorkspaceJob  {

	private final static boolean log = true ;
	private static void log(String msg) {
		if ( log ) {
			PluginLogger.log(NotificationJob.class, msg);
		}
	}

	private final File modelFile ;
	
	public NotificationJob(File modelFile) {
		super("NotificationJob");
		this.modelFile = modelFile ;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor progressMonitor) throws CoreException {
		log("NotificationJob : runInWorkspace() : model updated " + modelFile.getName());

		if ( PlatformUI.getWorkbench() != null ) {
			if ( PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null ) {
				if ( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null ) {
					
				}
				else {
					log("getActivePage : null !!!! ");
				}
			}			
			else {
				log("getActiveWorkbenchWindow : null !!!! ");
			}
		}
		else {
			log("getWorkbench : null !!!! ");
		}
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		log("Editors ( count = " + editors.length + " )");
		for ( IEditorReference editor : editors ) {
			log(" . id    = " + editor.getId()  );
			log(" . name  = " + editor.getName() );
			log(" . class = " + editor.getClass() );
			log(" ----- " );
		}
		
		return Status.OK_STATUS;
	}

}
