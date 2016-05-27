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
	        //processModel(delta);
	        ModelChangesProcessor.processModelChange(delta);
	        return false; // IFile => no children
		}
		else if ( isEntityFile(delta) ) {
	        //processEntity(delta);
	        EntityChangesProcessor.processEntityChange(delta);
	        return false; // IFile => no children
		}
        return true; 
	}
	
	private File getFile(IResourceDelta delta) {
        return getFile(delta.getResource());
	}
	private File getFile(IResource resource) {
        if ( resource instanceof IFile ) {
    		IFile iFile = (IFile) resource ;
    		return EclipseWksUtil.toFile(iFile);
        }		
        return null; // Not a file 
	}
	
	private boolean isModelFile(IResourceDelta delta) {
		log("isModelFile(IResourceDelta delta) : delta = " + delta.getFullPath() );
//        if ( delta.getResource() instanceof IFile ) {
//    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx.model"
//    		IFile iFile = (IFile) delta.getResource() ;
//    		File file = EclipseWksUtil.toFile(iFile);
//    		if ( DslModelUtil.isValidModelFile(file, true) ) {
//                return true ; // It's a model file 
//    		}
//        }		
//        return false; // Not a model file 
        
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
//        if ( delta.getResource() instanceof IFile ) {
//    		// If this file match the standard model name and location, eg : "TelosysTools/xxxx_model/xxx.entity"
//    		IFile iFile = (IFile) delta.getResource() ;
//    		File file = EclipseWksUtil.toFile(iFile);
//    		if ( DslModelUtil.isValidEntityFile(file, true) ) {
//                return true ; // It's an entity file 
//    		}
//        }		
		File file = getFile(delta);
        if ( file != null ) {
    		// If this file match the standard name and location, eg : "TelosysTools/xxxx_model/xxx.entity"
    		if ( DslModelUtil.isValidEntityFile(file, true) ) {
                return true ; // It's a model file 
    		}
        }
        return false; // Not entity file 
	}

//    private void processEntity(IResourceDelta delta) throws CoreException {
//    	
//    	log("processEntity()");
//    	IResource res = delta.getResource();
//
//        switch (delta.getKind()) {
//        
//           case IResourceDelta.ADDED:
//              log("ENTITY ADDED : " + res.getFullPath() );
//              processEntityChange(res);
//              break;
//              
//           case IResourceDelta.REMOVED:
//              log("ENTITY REMOVED : " + res.getFullPath() );
//              processEntityChange(res);
//              break;
//              
//           case IResourceDelta.CHANGED:
//               log("ENTITY CHANGED : " + res.getFullPath() );
//               
//               // more details about changes
//               int flags = delta.getFlags();
//               if ((flags & IResourceDelta.CONTENT) != 0) {
//             	  // Happens after File/Save in a text editor 
//            	   log("--> Content Change");
//                   processEntityChange(res);
//               }
//               
//               if ((flags & IResourceDelta.REPLACED) != 0) {
//            	   log("--> Content Replaced");
//                     processEntityChange(res);
//               }
//               
//               if ((flags & IResourceDelta.MARKERS) != 0) {
//            	   log("--> Marker Change");
//                     //IMarkerDelta[] markers = delta.getMarkerDeltas();
//                     // if interested in markers, check these deltas
//               }
//               //IResourceDelta.MOVED_FROM 
//               //IResourceDelta.OPEN
//               //IResourceDelta.COPIED_FROM
//        }
//	}
    
//    private void processEntityChange_OLD(IResource resource) throws CoreException {
//        File entityFile = EclipseWksUtil.toFile( (IFile) resource );
//        File modelFile = DslModelUtil.getModelFileForEntityFile(entityFile);
//        if ( modelFile != null ) {
//        	log("Model file : " + modelFile.getAbsolutePath() ) ;
//            // Differed refresh based on a scheduled "Job"
//        	NotificationJob job = new NotificationJob(modelFile);
//        	job.schedule();
//            log("NotificationJob().schedule() done.");
//        }
//        else {
//        	log("ERROR : no model file for entity " + entityFile.getAbsolutePath());
//        }
//    }
    
//    private void processEntityChange(IResource resource) throws CoreException {
//    
//    	File entityFile = getFile(resource);
//    	if ( entityFile != null ) {
//        	File modelFile = DslModelUtil.getModelFileForEntityFile(entityFile);
//        	if ( modelFile != null ) {
//        		notifyModelEditorIfAny(modelFile);
//        	}
//    	}
//    }
    
//    private void notifyModelEditorIfAny(File modelFile) {
//	    Display.getDefault().asyncExec(new Runnable() {
//	        @Override
//	        public void run() {
//	            //IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//	    		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
//	    		System.out.println("Editors ( count = " + editors.length + " )");
//	    		for ( IEditorReference editor : editors ) {
//	    			log(" editor.getId()   = " + editor.getId()   );
//	    			log(" editor.getName() = " + editor.getName() );
//	    			if ( isModelEditor(editor)) {
//	    				log(" editor is a ModelEditor ");
//	    				ModelEditor modelEditor = getModelEditor(editor);
//	    				if ( modelEditor != null ) {
//		    				log(" ModelEditor instance found : file name = " + modelEditor.getShortFileName() );
//		    				log(" Calling ModelEditor refresh()..." );
//		    				modelEditor.refresh();
//	    				}
//	    			}
//	    		}
//	        }
//	    });
//    }
    
//    private boolean isModelEditor(IEditorReference editor) {
//		if ( editor.getId().endsWith(".ModelEditor") ) {
//			if ( editor.getName().endsWith(".model") ) {
//    			return true ;
//			}	    				
//		}
//		return false ;
//    }
    
//    private ModelEditor getModelEditor(IEditorReference editor) {
//    	IEditorPart editorPart = editor.getEditor(true); 
//    	if ( editorPart != null ) {
//    		//Class<?> clazz = editorPart.getClass() ;
//    		if ( editorPart instanceof ModelEditor ) {
//    			return (ModelEditor) editorPart ;
//    		}
//    		else {
//    			log("ERROR : editor is not an instance of ModelEditor !!!");
//    		}
//    	}
//    	else {
//    		log("ERROR : editorPart is null !!!");
//    	}
//    	return null ;
//    }    
    
}
