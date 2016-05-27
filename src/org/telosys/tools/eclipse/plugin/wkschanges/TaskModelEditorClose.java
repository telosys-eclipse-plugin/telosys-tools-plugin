package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;

public class TaskModelEditorClose implements Runnable {

	private final static boolean log = true ;
	private final static String  CLASS_NAME = TaskModelEditorClose.class.getSimpleName() ;	
	private void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}

	private final File modelFile ;
	
	public TaskModelEditorClose(File modelFile) {
		super();
		this.modelFile = modelFile;
		log("TASK created");
	}

	@Override
	public void run() {
		log("TASK run()...");
		// NB : Must be called in the task "run"
		ModelEditor modelEditor = ModelEditorFinder.findModelEditorForModelFile(modelFile);
    	if ( modelEditor != null ) {
    		log(" ModelEditor instance found for file : " + modelFile.getName()  );
    		log(" Calling the ModelEditor close() method ..." );
    		//modelEditor.close(false);
    		modelEditor.closeEditor(false);
    	}
    	else {
    		log(" No ModelEditor for this file : " + modelFile.getName() );
    	}
	}

}
