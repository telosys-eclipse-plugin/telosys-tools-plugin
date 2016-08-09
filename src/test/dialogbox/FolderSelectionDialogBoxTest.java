package test.dialogbox;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.FolderSelectionDialogBox;

public class FolderSelectionDialogBoxTest {

	public static void main(String[] args) {
		
		File home = FileSystemView.getFileSystemView().getHomeDirectory();
		System.out.println("Home = " + home ); // e.g. X:/user/me/Desktop
		
		File[] roots = FileSystemView.getFileSystemView().getRoots();
		System.out.println("Roots : ");
		for ( File f : roots ) {
			System.out.println(" . " + f.getAbsolutePath() );
		}
		
		roots = File.listRoots();;
		System.out.println("Roots : ");
		for ( File f : roots ) {
			System.out.println(" . " + f.getAbsolutePath() );
		}

		File.listRoots();
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		
		//String folder = FolderSelectionDialogBox.chooseFolder(shell, "My dialog box", "D:\\") ;
		String folder = FolderSelectionDialogBox.chooseFolder(shell, "My dialog box" ) ;
		
		System.out.println("Selected folder = " + folder);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
