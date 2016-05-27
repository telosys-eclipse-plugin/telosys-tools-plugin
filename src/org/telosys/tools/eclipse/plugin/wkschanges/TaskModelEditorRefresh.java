package org.telosys.tools.eclipse.plugin.wkschanges;

import java.io.File;

import org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor;

public class TaskModelEditorRefresh implements Runnable {

	private final static boolean log = true ;
	private final static String  CLASS_NAME = TaskModelEditorRefresh.class.getSimpleName() ;	
	private void log(String msg) {
		if ( log ) {
			System.out.println( CLASS_NAME + " : " + msg );
		}
	}

	private final File modelFile ;
	
	public TaskModelEditorRefresh(File modelFile) {
		super();
		this.modelFile = modelFile;
		log("TASK created");
	}

	@Override
	public void run() {
		log("TASK run()...");
		// NB : Must be called in the task "run"
    	if ( modelFile.exists() ) {
    		log(" The model file exists : " + modelFile.getName() );
    		ModelEditor modelEditor = ModelEditorFinder.findModelEditorForModelFile(modelFile);
        	if ( modelEditor != null ) {
        		log(" ModelEditor instance found for file : " + modelFile.getName()  );
        		log(" Calling the ModelEditor refresh() method ..." );
        		modelEditor.refresh();
        	}
        	else {
        		log(" No ModelEditor for this file : " + modelFile.getName() );
        	}
    	}
    	else {
    		log(" The model file doesn't exist (deleted) : " + modelFile.getName() );
    	}
    	
	}

}
