package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

public class ResourceChangeListener implements IResourceChangeListener {

	// Certain methods in the resources plug-in API directly modify resources in the workspace. 
	// The most common examples are creating, copying, moving 
	// and deleting files and folders, and modifying file contents.
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println("Something changed." );
		System.out.println("   Event = " + event.getType() );
		System.out.println("   Resource = " + event.getResource() );
		
        switch (event.getType()) {
        
//           case IResourceChangeEvent.PRE_CLOSE:
//              System.out.print("PRE_CLOSE : Project ");
//              System.out.print(res.getFullPath());
//              System.out.println(" is about to close.");
//              break;
//              
//           case IResourceChangeEvent.PRE_DELETE:
//              System.out.print("PRE_DELETE : Project ");
//              System.out.print(res.getFullPath());
//              System.out.println(" is about to be deleted.");
//              break;
              
           case IResourceChangeEvent.POST_CHANGE:
              System.out.println("POST_CHANGE : Resources have changed.");
//              event.getDelta().accept(new ResourceDeltaVisitor());
              acceptVisitor(event);
              break;
              
//           case IResourceChangeEvent.PRE_BUILD:
//              System.out.println("PRE_BUILD : Build about to run.");
////              event.getDelta().accept(new ResourceDeltaVisitor());
//              acceptVisitor(event);
//              break;
//              
//           case IResourceChangeEvent.POST_BUILD:
//              System.out.println("POST_BUILD : Build complete.");
////            event.getDelta().accept(new ResourceDeltaVisitor());
//              acceptVisitor(event);
//              break;
        }		
	}
	
	private void acceptVisitor(IResourceChangeEvent event) {
        try {
			event.getDelta().accept(new ResourceDeltaVisitor());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
