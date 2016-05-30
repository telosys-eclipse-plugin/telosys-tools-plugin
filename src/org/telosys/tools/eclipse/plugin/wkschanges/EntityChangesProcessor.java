package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;

/**
 * Processor for entity changes ( when ".entity" files has changed ) <br>
 * 
 * @author L. Guerin
 *
 */
public class EntityChangesProcessor {

	private final static boolean log = true ;
	private final static String  CLASS_NAME = EntityChangesProcessor.class.getSimpleName() ;	
	private static void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}
	
    public static void processEntityChange(IResourceDelta delta) throws CoreException {
    	
    	log("processEntity()");
    	IResource res = delta.getResource();

        switch (delta.getKind()) {
        
           case IResourceDelta.ADDED:
              log("ENTITY ADDED : " + res.getFullPath() );
              processEntity(res);
              break;
              
           case IResourceDelta.REMOVED:
              log("ENTITY REMOVED : " + res.getFullPath() );
              processEntity(res);
              break;
              
           case IResourceDelta.CHANGED:
               log("ENTITY CHANGED : " + res.getFullPath() );
               
               // more details about changes
               int flags = delta.getFlags();
               if ((flags & IResourceDelta.CONTENT) != 0) {
             	  // Happens after File/Save in a text editor 
            	   log("--> Content Change");
                   processEntity(res);
               }
               
               if ((flags & IResourceDelta.REPLACED) != 0) {
            	   log("--> Content Replaced");
                     processEntity(res);
               }
               
//               if ((flags & IResourceDelta.MARKERS) != 0) {
//            	   log("--> Marker Change");
//                     //IMarkerDelta[] markers = delta.getMarkerDeltas();
//                     // if interested in markers, check these deltas
//               }
               //IResourceDelta.MOVED_FROM 
               //IResourceDelta.OPEN
               //IResourceDelta.COPIED_FROM
        }
	}
    
    private static void processEntity(IResource resource) throws CoreException {
    
    	// Just notify the model editor (if open) that an entity has changed
    	File entityFile = getFile(resource);
    	if ( entityFile != null ) {
        	File modelFile = DslModelUtil.getModelFileForEntityFile(entityFile);
        	if ( modelFile != null ) {
        		notifyModelEditorIfAny(modelFile);
        	}
    	}
    }
    
	private static File getFile(IResource resource) {
        if ( resource instanceof IFile ) {
    		IFile iFile = (IFile) resource ;
    		return EclipseWksUtil.toFile(iFile);
        }		
        return null; // Not a file 
	}

    /**
     * Ask the model editor to refresh and then to show the new entities and the errors if any
     * @param modelFile
     */
    private static void notifyModelEditorIfAny(File modelFile) {
    	if ( modelFile.exists() ) {
    		log(" The model file exists ( it can be parsed ): " + modelFile.getName() );
    	    Display.getDefault().asyncExec( new TaskModelEditorRefresh(modelFile) );
    	}
    	else {
    		log(" The model file deosn't exist ( it cannot be parsed => no refresh ): " + modelFile.getName() );
    	}
    }
    
}
