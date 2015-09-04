package org.telosys.tools.eclipse.plugin.generator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.commons.io.CopyHandler;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.eclipse.plugin.commons.EclipseWksUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.task.AbstractGenerationTask;
import org.telosys.tools.generator.task.GenerationTask;
import org.telosys.tools.generator.task.GenerationTaskResult;
import org.telosys.tools.generator.task.ITaskMonitor;
import org.telosys.tools.repository.model.RepositoryModel;


/**
 * Eclipse runnable task with a progress bar 
 * for code generation 
 *  
 * @author Laurent Guerin
 *
 */
public class GenerationTaskWithProgress extends AbstractGenerationTask implements GenerationTask, IRunnableWithProgress 
{
	
	/**
	 * Constructor
	 * @param repositoryModel
	 * @param selectedEntities
	 * @param bundleName
	 * @param selectedTargets
	 * @param resourcesTargets
	 * @param telosysToolsCfg
	 * @param logger
	 * @throws TelosysToolsException
	 */
	public GenerationTaskWithProgress(
			RepositoryModel repositoryModel,
			LinkedList<String> selectedEntities,
			String bundleName,
			LinkedList<TargetDefinition> selectedTargets,
			List<TargetDefinition> resourcesTargets,
			//GeneratorConfig generatorConfig, 
			TelosysToolsCfg telosysToolsCfg,
			TelosysToolsLogger logger)
			throws TelosysToolsException 
	{
		// Just call the super class constructor
//		super(repositoryModel, selectedEntities, selectedTargets, resourcesTargets,
//				generatorConfig, logger);
		super(repositoryModel, selectedEntities, 
				bundleName, selectedTargets, resourcesTargets, 
				telosysToolsCfg, logger);
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for super class 'AbstractGenerationTask'
	//--------------------------------------------------------------------------------------
	@Override  // Implementation for AbstractGenerationTask
	protected void showErrorMessage(String message, Throwable exception) {
		MsgBox.error( message, exception );
	}

	@Override  // Implementation for AbstractGenerationTask
	protected void showErrorMessage(String message1, String message2) {
		MsgBox.error( message1, message2 );
	}
	
	@Override  // Implementation for AbstractGenerationTask
	protected void afterFileGeneration(String fullFileName) {
		log("afterFileGeneration(" + fullFileName + ")");
		// Refresh the generated file in the Eclipse Workspace 
		EclipseWksUtil.refresh( new File(fullFileName) );	
	}

	@Override  // Implementation for AbstractGenerationTask
	public GenerationTaskResult launch() { 
		
		//-----------------------------------------------------------------------------------
		// BULK GENERATION ENTRY POINT 
		// Creates a 'ProgressMonitor (Eclipse object)' and use it to run this task instance
		//-----------------------------------------------------------------------------------

		//GenerationTaskResult generationTaskResult = null ;
		//GenerationTaskWithProgress generationTaskWithProgress = this ;
		
		//--- 1) De-activate "Build Automatically"
		boolean originalFlag = EclipseWksUtil.setBuildAutomatically(false);
		
		//--- 2) Run the generation task via the progress monitor 
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog( Util.getActiveWindowShell() ) ;
		try {
			log("Run generation task ..."  );
			//--- RUN THE ECLIPSE TASK ( 'this' task ) ....
			progressMonitorDialog.run(false, false, this);  
			log("End of generation task."  );
			
			GenerationTaskResult generationTaskResult = super.getResult() ;
			MsgBox.info("Normal end of generation." 
					+ "\n\n" + generationTaskResult.getNumberOfResourcesCopied() + " resources(s) copied."
					+ "\n\n" + generationTaskResult.getNumberOfFilesGenerated() + " file(s) generated.");
			
		} catch (InvocationTargetException invocationTargetException) {
//			showGenerationError(invocationTargetException, 
//					generationTaskWithProgress.getCurrentTemplateName(), generationTaskWithProgress.getCurrentEntityName() ); // v 2.0.7
			super.showGenerationError(invocationTargetException);
		} catch (InterruptedException e) {
			MsgBox.info("Generation interrupted");
		}
		
		//--- 3) Re-activate "Build Automatically" 
		EclipseWksUtil.setBuildAutomatically(originalFlag);
		
//    	return generationTaskResult;	
    	return super.getResult();
	}
	
	//--------------------------------------------------------------------------------------
	// Methods implementation for Eclipse interface 'IRunnableWithProgress'
	//--------------------------------------------------------------------------------------
	@Override
	public void run(IProgressMonitor progressMonitor) throws InvocationTargetException,
			InterruptedException {
		log("run");

		//---------------------------------------------------------------------------
		// BULK GENERATION STEPS ( called by the Eclipse 'ProgressMonitorDialog' )
		// It copies the required resources and generates the selected targets 
		// by calling the super class standard methods
		//---------------------------------------------------------------------------
//		
//		//Variable[] projectVariables = _generatorConfig.getTelosysToolsCfg().getAllVariables();
//		Variable[] projectVariables = super.getAllProjectVariables(); // call SUPER CLASS
//		
//		//--- 1) Copy the given resources (or do nothing if null)
//		//int numberOfResourcesCopied = copyResourcesIfAny(_resourcesTargets);
//		OverwriteChooser overwriteChooser = new OverwriteChooserDialogBox() ; 
//		CopyHandler copyHandler = new CopyHandlerForRefresh() ;
//
//		int numberOfResourcesCopied = super.copyResourcesIfAny(overwriteChooser, copyHandler); // call SUPER CLASS
//
//		//--- 2) Launch the generation
////		int numberOfFilesGenerated = generateSelectedTargets(progressMonitor, projectVariables);
//		ITaskMonitor taskMonitor = new TaskMonitor(progressMonitor);
//		int numberOfFilesGenerated = super.generateSelectedTargets(taskMonitor, projectVariables); // call SUPER CLASS
//		
//		//--- Task result
//		//_result = new GenerationTaskResult(numberOfResourcesCopied, numberOfFilesGenerated);
//		super.setResult(numberOfResourcesCopied, numberOfFilesGenerated); // call SUPER CLASS

		OverwriteChooser overwriteChooser = new OverwriteChooserDialogBox() ; 
		CopyHandler copyHandler = new CopyHandlerForRefresh() ;
		ITaskMonitor taskMonitor = new TaskMonitor(progressMonitor);
		
		super.runTask(taskMonitor, overwriteChooser, copyHandler);
	}

}
