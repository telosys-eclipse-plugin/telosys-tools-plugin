package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

public class NotificationJob extends WorkspaceJob  {

	private final File modelFile ;
	
	public NotificationJob(File modelFile) {
		super("NotificationJob");
		this.modelFile = modelFile ;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor progressMonitor) throws CoreException {
		System.out.println("NotificationJob : runInWorkspace() : model updated " + modelFile.getName());

		if ( PlatformUI.getWorkbench() != null ) {
			if ( PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null ) {
				if ( PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null ) {
					
				}
				else {
					System.out.println("getActivePage : null !!!! ");
				}
			}			
			else {
				System.out.println("getActiveWorkbenchWindow : null !!!! ");
			}
		}
		else {
			System.out.println("getWorkbench : null !!!! ");
		}
		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
		System.out.println("Editors ( count = " + editors.length + " )");
		for ( IEditorReference editor : editors ) {
			System.out.println(" . id    = " + editor.getId()  );
			System.out.println(" . name  = " + editor.getName() );
			System.out.println(" . class = " + editor.getClass() );
			System.out.println(" ----- " );
		}
		
		return Status.OK_STATUS;
	}

}
