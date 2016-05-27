package org.telosys.tools.eclipse.plugin.wkschanges.old;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.wkschanges.RefreshJob;

public class ModelDeltaVisitor implements IResourceDeltaVisitor {

	private final static boolean log = true ;
	private void log(String msg) {
		if ( log ) {
			System.out.println("ModelDeltaVisitor : " + msg );
		}
	}
	
	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		log("visit(delta)");
//        if ( delta.getResource() instanceof IFile ) {
//    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx.model"
//    		IFile iFile = (IFile) delta.getResource() ;
//    		File file = EclipseWksUtil.toFile(iFile);
//    		if ( DslModelUtil.isValidModelFile(file, true) ) {
//                processModel(delta);
//    		}
//            return false; // IFile => no children
//        }
		if ( isModelFile(delta) ) {
	        processModel(delta);
	        return false; // IFile => no children
		}
        return true; 
	}
	
	private boolean isModelFile(IResourceDelta delta) {
		log("isModelFile(IResourceDelta delta) : delta = " + delta.getFullPath() );
        if ( delta.getResource() instanceof IFile ) {
    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx.model"
    		IFile iFile = (IFile) delta.getResource() ;
    		File file = EclipseWksUtil.toFile(iFile);
    		if ( DslModelUtil.isValidModelFile(file, true) ) {
                return true ; // It's a model file 
    		}
        }		
        return false; // Not a model file 
	}

    private void processModel(IResourceDelta delta) throws CoreException {
    	
    	log("processModel()");
    	IResource res = delta.getResource();

        switch (delta.getKind()) {
        
           case IResourceDelta.ADDED:
              log("MODEL ADDED : " + res.getFullPath() );
              // Nothing to do
              break;
              
           case IResourceDelta.REMOVED:
              log("MODEL REMOVED : " + res.getFullPath() );
              
              /*
               * ISSUE : 
               * Cannot refresh folder. The resource tree is locked for modifications.
               * 
               * RESPONSE :
               * You can't modify the resource tree from a resource delta event handler 
               * (imagine the potential for complete chaos if you could). 
               * The most common approach that I know of is to schedule a Job and make the 
               * modifications within the run() method of the Job. This means you need to 
               * remember the modifications that you want to make so that they can be done within the Job. 
               * It also means you can't make too many assumptions about the state of the resource 
               * tree because theoretically some other Job might run before yours that makes changes to the tree
               * 
               */
              
              // Remove the model file "xxx.model" and the folder "xxx_model"
              File modelFile = EclipseWksUtil.toFile( (IFile) delta.getResource() );
              DslModelUtil.deleteModel(modelFile);
              log("Model file and folder deleted.");
              
              // Differed refresh based on a scheduled "Job"
              RefreshJob refreshJob = new RefreshJob(modelFile.getParentFile());
              refreshJob.schedule();
              log("refreshJob.schedule() done.");
              
              break;
        }
	}
}
