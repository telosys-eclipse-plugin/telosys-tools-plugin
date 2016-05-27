package org.telosys.tools.eclipse.plugin.wkschanges;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;

public class FileChangeListener implements IResourceChangeListener {
	
	private final static boolean log = true ;
	private final static String  CLASS_NAME = FileChangeListener.class.getSimpleName() ;	
	private void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		log("resourceChanged(IResourceChangeEvent event) : event type " + event.getType() );
		
		if ( event.getType() == IResourceChangeEvent.POST_CHANGE ) {
			log("event type = 'POST_CHANGE' : Resources have changed.");
			
			IResourceDelta delta = event.getDelta();
	        try {
				log("delta.accept(...) : delta = " + delta.getFullPath() );
	        	delta.accept(new FileDeltaVisitor());
			} catch (CoreException e) {
				throw new RuntimeException("Cannot accept visitor (CoreException)", e);
			}
		}
	}
}
