package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.eclipse.plugin.commons.Util;

public class FolderSelectionDialogBox {

	public static String chooseFolder(String title, String filterPath) {
		Shell shell = Util.getActiveWindowShell();
		return chooseFolder(shell, title, filterPath);
	}
	
	public static String chooseFolder(Shell shell, String title, String filterPath) {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		
		// Initial filter path
		//dialog.setFilterPath("c:\\"); 
		dialog.setFilterPath(filterPath); 
		
		// Change the title bar text
		//dialog.setText("SWT's DirectoryDialog");
		dialog.setText(title);
        
		// Customizable message displayed in the dialog
		dialog.setMessage("Select a directory");
        
		String selectedFolder = dialog.open();
		
		return selectedFolder ;
	}
	
//	public static void main(String[] args) {
//		Display display = new Display();
//		Shell shell = new Shell(display);
//		shell.open();
//		
//		String folder = chooseFolder(shell, "My dialog box", "D:\\") ;
//		
//		//System.out.println("Selected folder = " + folder);
//		
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch()) {
//				display.sleep();
//			}
//		}
//		display.dispose();
//	}
}
