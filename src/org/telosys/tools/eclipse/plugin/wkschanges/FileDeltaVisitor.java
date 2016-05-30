package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;

public class FileDeltaVisitor implements IResourceDeltaVisitor {

	private final static boolean log = true ;
	private final static String  CLASS_NAME = FileDeltaVisitor.class.getSimpleName() ;	
	private void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		log("visit(delta)");
		if ( isModelFile(delta) ) {
	        ModelChangesProcessor.processModelChange(delta);
	        return false; // IFile => no children
		}
		else if ( isEntityFile(delta) ) {
	        EntityChangesProcessor.processEntityChange(delta);
	        return false; // IFile => no children
		}
        return true; 
	}
	
	private boolean isModelFile(IResourceDelta delta) {
		log("isModelFile(IResourceDelta delta) : delta = " + delta.getFullPath() );
		File file = getFile(delta);
        if ( file != null ) {
    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx.model"
    		if ( DslModelUtil.isValidModelFile(file, true) ) {
                return true ; // It's a model file 
    		}
        }
        return false; // Not a model file 
	}

	private boolean isEntityFile(IResourceDelta delta) {
		log("isEntityFile(IResourceDelta delta) : delta = " + delta.getFullPath() );
		File file = getFile(delta);
        if ( file != null ) {
    		// If this file match the standard name and location, eg : "TelosysTools/xxxx_model/xxx.entity"
    		if ( DslModelUtil.isValidEntityFile(file, true) ) {
                return true ; // It's a model file 
    		}
        }
        return false; // Not entity file 
	}

	private File getFile(IResourceDelta delta) {
		IResource resource = delta.getResource() ;
        if ( resource instanceof IFile ) {
    		IFile iFile = (IFile) resource ;
    		return EclipseWksUtil.toFile(iFile);
        }		
        return null; // Not a file 
	}
//	private File getFile(IResource resource) {
//        if ( resource instanceof IFile ) {
//    		IFile iFile = (IFile) resource ;
//    		return EclipseWksUtil.toFile(iFile);
//        }		
//        return null; // Not a file 
//	}
	
}
