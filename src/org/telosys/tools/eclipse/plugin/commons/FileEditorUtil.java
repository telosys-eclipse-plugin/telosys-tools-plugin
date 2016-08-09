package org.telosys.tools.eclipse.plugin.commons;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.telosys.tools.commons.FileUtil;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.generator.target.TargetDefinition;
import org.telosys.tools.generator.target.TargetsLoader;

public class FileEditorUtil {

	// The editor id is defined in "plugin.xml" 
	private final static String VELOCITY_EDITOR_ID     = "org.telosys.tools.eclipse.plugin.editors.velocity.VelocityEditor";
	private final static String DSL_ENTITY_EDITOR_ID   = "org.telosys.tools.eclipse.plugin.editors.dsl.entityEditor.EntityEditor";
	private final static String DSL_MODEL_EDITOR_ID    = "org.telosys.tools.eclipse.plugin.editors.dsl.model.ModelEditor";
	
	private final static String DEFAULT_TEXT_EDITOR_ID = "org.eclipse.ui.DefaultTextEditor" ;
	
	private static void log(String s) {
		PluginLogger.log( FileEditorUtil.class.getName() + " : " + s );
	}

	/**
	 * @param project
	 * @param bundleName
	 * @param fileName
	 * @return
	 */
	private static String getFilePathFromTemplatesFolder( IProject project, String bundleName, String fileName ) {
		log( "getFilePathFromTemplatesFolder(.., " + bundleName + ", "+ fileName +")");
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( project );
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( project ); // v 3.0.0
//		if ( projectConfig != null ) {
		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( project ); // v 3.0.0
		if ( telosysToolsCfg != null ) {
			//String templatesFolder = telosysToolsCfg.getTemplatesFolder(); // v 3.0.0
			String folder = telosysToolsCfg.getTemplatesFolderAbsolutePath();
			
			// use bundle folder if any
			if ( ! StrUtil.nullOrVoid(bundleName) ) {
//				templatesFolder = FileUtil.buildFilePath(templatesFolder, bundleName );
				folder = telosysToolsCfg.getTemplatesFolderAbsolutePath(bundleName);
			}

//			String templateFile = FileUtil.buildFilePath(templatesFolder, fileName );
			String templateFile = FileUtil.buildFilePath(folder, fileName );
			
//			log("Templates file (path in project) = " + templateFile );		
			log("Template file full path = " + templateFile );		
			
////			File f = EclipseProjUtil.getResourceAsFile( project, templateFile);
////			if ( f != null) {
////				return EclipseWksUtil.toIFile(f);
////			}
////			else {
////				MsgBox.error("Template file '" + templateFile + "' not found !");
////			}
//			File f = new File(templateFile);
//			return EclipseWksUtil.toIFile(f);
			return templateFile;
		}
		return null ;
	}
	
//	/**
//	 * Returns the template file instance for the given target
//	 * @param project
//	 * @param target
//	 * @return
//	 */
//	private static IFile getTemplateFile( IProject project, String bundleName, TargetDefinition target ) {
//		
//		log( "getTemplateFile(target)..." + target );
//		return getFileFromTemplatesFolder( project, bundleName, target.getTemplate() );
//	}
	
	/**
	 * Opens the template file (".vm") of the given target in the specialized editor
	 * @param project
	 * @param bundleName
	 * @param target
	 */
	public static void openTemplateFileInEditor(IProject project, String bundleName, TargetDefinition target) {
		log( "openTemplateFileInEditor(.., " + bundleName + ", "+ target.getTemplate() +")");

//		//IFile templateFile = getTemplateFile( project, bundleName, target );
//		IFile templateFile = getFilePathFromTemplatesFolder( project, bundleName, target.getTemplate() );

//		IEditorInput editorInput = new FileEditorInput(templateFile);
//		
//		IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
//		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
//		try {
//			// Use class TextEditor : The standard/default text editor.
//			// This editor has id "org.eclipse.ui.DefaultTextEditor". 
//			//workbenchPage.openEditor(editorInput, "org.eclipse.ui.DefaultTextEditor" );
//			workbenchPage.openEditor(editorInput, VELOCITY_EDITOR_ID );
//			
//		} catch (PartInitException e) {
//			MsgBox.error("Cannot open file in editor (PartInitException)");
//		}
		
//		openFileInEditor(templateFile, VELOCITY_EDITOR_ID );
		String absoluteFilePath = getFilePathFromTemplatesFolder( project, bundleName, target.getTemplate());
//		IFile iFile = EclipseWksUtil.toIFile(absoluteFilePath);
//		openFileInEditor(iFile, VELOCITY_EDITOR_ID );
		openFileInEditor(absoluteFilePath, VELOCITY_EDITOR_ID );
	}

	/**
	 * Opens the targets configuration file ( "templates.cfg" )in the standard text editor
	 * @param project
	 * @param bundleName
	 */
	public static void openTargetsConfigFileInEditor(IProject project, String bundleName) {
		log( "openTargetsConfigFileInEditor(.., " + bundleName +")");
		
//		IFile targetsConfigFile = getFilePathFromTemplatesFolder( project, bundleName, TargetsLoader.TEMPLATES_CFG );
		String absoluteFilePath = getFilePathFromTemplatesFolder( project, bundleName, TargetsLoader.TEMPLATES_CFG );
//		IFile iFile = EclipseWksUtil.toIFile(absoluteFilePath);		
//		openFileInEditor(iFile, DEFAULT_TEXT_EDITOR_ID ); // "org.eclipse.ui.DefaultTextEditor"
		openFileInEditor(absoluteFilePath, DEFAULT_TEXT_EDITOR_ID ); // "org.eclipse.ui.DefaultTextEditor"
	}

	/**
	 * Opens the ".entity" file in the "entity editor"
	 * @param entityAbsoluteFilePath
	 */
	public static void openEntityFileInEditor(String entityAbsoluteFilePath) {
		log( "openEntityFileInEditor(.., " + entityAbsoluteFilePath +")");
		
//		IFile iFile = EclipseWksUtil.toIFile(entityAbsoluteFilePath);
//		openFileInEditor(iFile, DSL_ENTITY_EDITOR_ID );
		openFileInEditor(entityAbsoluteFilePath, DSL_ENTITY_EDITOR_ID );
	}

	/**
	 * Opens the ".model" file in the "DSL model editor"
	 * @param modelAbsoluteFilePath
	 */
	public static void openModelFileInEditor(String modelAbsoluteFilePath) {
		log( "openModelFileInEditor(.., " + modelAbsoluteFilePath +")");
		
//		IFile iFile = EclipseWksUtil.toIFile(modelAbsoluteFilePath);
//		openFileInEditor(iFile, DSL_MODEL_EDITOR_ID );
		openFileInEditor(modelAbsoluteFilePath, DSL_MODEL_EDITOR_ID );
	}

	/**
	 * Opens the given file in the editor associated with the given editor id
	 * @param absoluteFilePath
	 * @param editorId
	 */
	//private static void openFileInEditor(IFile file, String editorId) {
	private static void openFileInEditor(String absoluteFilePath, String editorId) {
		
		IFile iFile = EclipseWksUtil.toIFile(absoluteFilePath);
		if ( iFile != null ) {
			IEditorInput editorInput = new FileEditorInput(iFile);
			
			IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			try {
				workbenchPage.openEditor(editorInput, editorId );
			} catch (PartInitException e) {
				MsgBox.error("Cannot open file in editor (PartInitException)");
			}
		}
		else {
			MsgBox.error("Cannot open file in editor \n" +
					"File '" + absoluteFilePath + "' \n " +
					"is not under the location of any existing project in the workspace. \n"
					);
		}
	}
}
