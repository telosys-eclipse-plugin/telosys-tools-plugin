package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

public class ModelChangeListener implements IResourceChangeListener {
	
	private final static boolean log = true ;
	private void log(String msg) {
		if ( log ) {
			System.out.println("ModelChangeListener : " + msg );
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		log("resourceChanged(IResourceChangeEvent event) : event type " + event.getType() );
		
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
              log("POST_CHANGE : Resources have changed.");
//              event.getDelta().accept(new ResourceDeltaVisitor());
              acceptModelDeltaVisitor(event);
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
	
	
	private void acceptModelDeltaVisitor(IResourceChangeEvent event) {
		IResourceDelta delta = event.getDelta();
		log("acceptVisitor(IResourceChangeEvent event) : delta = " + delta.getFullPath() );
        try {
        	delta.accept(new ModelDeltaVisitor());
		} catch (CoreException e) {
			throw new RuntimeException("Cannot accept visitor (CoreException)", e);
		}
	}
}
