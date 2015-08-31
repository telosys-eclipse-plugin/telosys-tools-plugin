package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.commons.cfg.TelosysToolsCfg;
import org.telosys.tools.eclipse.plugin.commons.EclipseProjUtil;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;
import org.telosys.tools.eclipse.plugin.commons.PluginLogger;
import org.telosys.tools.eclipse.plugin.commons.Util;
import org.telosys.tools.eclipse.plugin.config.ProjectConfigManager;
import org.telosys.tools.eclipse.plugin.editors.dbrep.RepositoryEditor;

public class TemplateBundleUtil {

	private static final String DEFAULT = " (default) " ;
	
	private static void log(String s) 
	{
		PluginLogger.log( TemplateBundleUtil.class.getName() + " : " + s );
	}

	/**
	 * Opens a dialog box for bundle selection and return the selected bundle
	 * @param eclipseProject
	 * @return the bundle name (or null if nothing selected)
	 */
	public static void selectBundle(RepositoryEditor editor, IProject eclipseProject) {
		String bundlesLocation = getBundlesLocation( eclipseProject ) ;
		log("bundlesLocation = '" + bundlesLocation + "'");
		if ( bundlesLocation != null ) {
			List<String> bundles = getBundlesFromTemplatesFolder( eclipseProject );
			if ( bundles != null ) {
				bundles.add(0, DEFAULT);
				int current = 0 ;
				String currentBundleName = editor.getCurrentBundleName() ;
				if ( currentBundleName != null ) {
					int i=0 ;
					for ( String s : bundles ) {
						if ( currentBundleName.equals(s) ) {
							current = i;
						}
						i++;
					}
				}
				Shell shell = Util.getActiveWindowShell();
				TemplatesBundleDialogBox dialogBox = new TemplatesBundleDialogBox(shell, bundles, bundlesLocation, current ) ;
				int r = dialogBox.open();
				if ( r == 0 ) {
					// OK button
					String selectedBundle = dialogBox.getSelectedItem();
					if ( DEFAULT.equals(selectedBundle) ) {
						selectedBundle = null ;
					}
					if ( StrUtil.different( selectedBundle, editor.getCurrentBundleName() )) {
						// only if the bundle name has changed : to avoid refresh (visual list effect) if unchanged
		            	editor.setCurrentBundleName(selectedBundle);
		            	editor.refreshAllTargetsTablesFromConfigFile();					
					}
					
				}
				else {
					// CANCEL button or CLOSE [x]
					// No change
				}
			}
			else {
				MsgBox.info("No bundle for this project");
			}
		}
		else {
			MsgBox.error("Cannot get bundles location for this project");
		}
	}
	
	public static String getBundlesLocation( IProject eclipseProject ) {
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( eclipseProject );
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( eclipseProject ); // v 3.0.0
//		if ( projectConfig != null ) {
//			//return  projectConfig.getTemplatesFolder();
//			return  projectConfig.getTelosysToolsCfg().getTemplatesFolder();
//		}
		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( eclipseProject ); // v 3.0.0		
		if ( telosysToolsCfg != null ) {
			return  telosysToolsCfg.getTemplatesFolder();
		}
		return null ;
	}
	
	public static List<String> getBundlesFromTemplatesFolder( IProject eclipseProject ) {
		List<String> bundles = new LinkedList<String>();
		
		//log( "getFileFromTemplatesFolder(..)..." + fileName );
//		ProjectConfig projectConfig = ProjectConfigManager.getProjectConfig( eclipseProject );
//		ProjectConfig projectConfig = ProjectConfigManager.loadProjectConfig( eclipseProject ); // v 3.0.0
		TelosysToolsCfg telosysToolsCfg = ProjectConfigManager.loadProjectConfig( eclipseProject ); // v 3.0.0		
//		if ( projectConfig != null ) {
		if ( telosysToolsCfg != null ) { // v 3.0.0	
			//String templatesFolder = projectConfig.getTemplatesFolder();
//			String templatesFolder = projectConfig.getTelosysToolsCfg().getTemplatesFolder();
			String templatesFolder = telosysToolsCfg.getTemplatesFolder(); // v 3.0.0
			log( "  templates folder = " + templatesFolder );
			File dir = EclipseProjUtil.getResourceAsFile( eclipseProject, templatesFolder);			
			if ( dir != null) {
				if ( dir.isDirectory() ) {
					File[] entries = dir.listFiles();
					for ( File f : entries ) {
						if ( f.isDirectory() ) {
							bundles.add(f.getName());
						}
					}
					return bundles ;
				}
				else {
					MsgBox.error("Templates folder '" + templatesFolder + "' is not a folder !");
				}
			}
			else {
				MsgBox.error("Templates folder '" + templatesFolder + "' not found !");
			}
		}
		return null ;
	}
	
	
}
