package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.swt.widgets.Display;
import org.telosys.tools.dsl.DslModelUtil;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;

/**
 * Processor for model changes
 * 
 * @author L. Guerin
 *
 */
public class ModelChangesProcessor {

	private final static boolean log = true ;
	private final static String  CLASS_NAME = ModelChangesProcessor.class.getSimpleName() ;	
	private static void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}
	
	//-----------------------------------------------------------------------------
	// Process model change event 
	//-----------------------------------------------------------------------------
    public static void processModelChange(IResourceDelta delta) { //throws CoreException {
    	
    	log("processModelChange(delta)");
//    	IResource res = delta.getResource();

        switch (delta.getKind()) {
        
           case IResourceDelta.ADDED:
        	  modelFileAdded(delta);
              break;
              
           case IResourceDelta.REMOVED:
              modelFileRemoved(delta);              
              break;

           case IResourceDelta.CHANGED:
        	   modelFileChanged(delta);
//               log("MODEL CHANGED : " + res.getFullPath() );
//               // more details about changes
//               int flags = delta.getFlags();
////               if ((flags & IResourceDelta.CONTENT) != 0) {
////             	  // Happens after File/Save in a text editor 
////            	   log("--> Content Change");
////                   processEntityChange(res);
////               }
//               
////               if ((flags & IResourceDelta.REPLACED) != 0) {
////            	   log("--> Content Replaced");
////                     processEntityChange(res);
////               }
//               
//               if ((flags & IResourceDelta.MARKERS) != 0) {
//            	   log("--> Marker Change");
////                   IMarkerDelta[] markerDeltas = delta.getMarkerDeltas();
//                   // if interested in markers, check these deltas
////                   for ( IMarkerDelta md : markerDeltas ) {
////                   }
//                   scheduleRefreshJob(delta);
//               }

               break;
        }
	}
    
    private static void modelFileAdded(IResourceDelta delta) {
    	IResource res = delta.getResource();
        log("MODEL ADDED : " + res.getFullPath() );
        log("MODEL ADDED : nothing to do" );
    }
    
    private static void modelFileRemoved(IResourceDelta delta) {
    	IResource res = delta.getResource();
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
        
        // Differed refresh job ( based on a scheduled "Job" )
        RefreshJob refreshJob = new RefreshJob(modelFile.getParentFile());
        refreshJob.schedule();
        log("refreshJob.schedule() done.");
        
        closeModelEditor(modelFile);
    }
    
    private static void closeModelEditor(File modelFile) {
    	Display.getDefault().asyncExec( new TaskModelEditorClose(modelFile) );
    }
    
    private static void modelFileChanged(IResourceDelta delta) {
    	IResource res = delta.getResource();
        log("MODEL CHANGED : " + res.getFullPath() );
        // more details about changes
        int flags = delta.getFlags();
//        if ((flags & IResourceDelta.CONTENT) != 0) {
//      	  // Happens after File/Save in a text editor 
//     	   log("--> Content Change");
//        }
        
//        if ((flags & IResourceDelta.REPLACED) != 0) {
//     	   log("--> Content Replaced");
//        }
        
        if ((flags & IResourceDelta.MARKERS) != 0) {
     	   log("--> Marker Change");
//            IMarkerDelta[] markerDeltas = delta.getMarkerDeltas();
            // if interested in markers, check these deltas
//            for ( IMarkerDelta md : markerDeltas ) {
//            }
//            scheduleRefreshJob(delta);
        }
    }
    
//    private static void scheduleRefreshJob(IResourceDelta delta) {
//        File modelFile = EclipseWksUtil.toFile( (IFile) delta.getResource() );
//        log("scheduleRefreshJob(" + modelFile.getName() + ")");
//        // Differed refresh based on a scheduled "Job"
//        RefreshJob refreshJob = new RefreshJob(modelFile.getParentFile());
//        refreshJob.schedule();
//        log("refreshJob.schedule() done.");
//    }
    
}
