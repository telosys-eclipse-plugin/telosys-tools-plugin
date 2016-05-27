package org.telosys.tools.eclipse.plugin.wkschanges.old;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class EntityDeltaVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
        if ( delta.getResource() instanceof IFile ) {
//        	System.out.println("File extension : " + delta.getFullPath().getFileExtension() );
        	if ( "entity".equals( delta.getFullPath().getFileExtension() ) ) {
        		// TODO
        		// check location : TelosysTools/xxxx_model/xxx.entity
                processEntity(delta);
        	}
            return false; // IFile => no children
        }
        return true; 
	}
	
    private void processEntity(IResourceDelta delta) throws CoreException {
    	IResource res = delta.getResource();

        switch (delta.getKind()) {
        
           case IResourceDelta.ADDED:
              System.out.print("ENTITY ADDED : " + res.getFullPath() );
              // TODO : parse model 
              break;
              
           case IResourceDelta.REMOVED:
              System.out.print("ENTITY REMOVED : " + res.getFullPath() );
              // TODO : parse model 
              break;
              
           case IResourceDelta.CHANGED:
              System.out.print("ENTITY CHANGED : " + res.getFullPath() );
              
              // more details about changes
              int flags = delta.getFlags();
              if ((flags & IResourceDelta.CONTENT) != 0) {
            	  // Happens after File/Save in a text editor 
                  System.out.println("--> Content Change");
                  // TODO : parse entity 
              }
              
              if ((flags & IResourceDelta.REPLACED) != 0) {
                    System.out.println("--> Content Replaced");
                    // TODO : parse entity 
              }
              if ((flags & IResourceDelta.MARKERS) != 0) {
                    System.out.println("--> Marker Change");
                    //IMarkerDelta[] markers = delta.getMarkerDeltas();
                    // if interested in markers, check these deltas
              }
              //IResourceDelta.MOVED_FROM 
              //IResourceDelta.OPEN
              //IResourceDelta.COPIED_FROM
              break;
        }
        //return true; // visit the children
	}
}
