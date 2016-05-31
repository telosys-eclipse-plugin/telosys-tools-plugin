package test.dialogbox;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.FolderSelectionDialogBox;

public class FolderSelectionDialogBoxTest {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		
		String folder = FolderSelectionDialogBox.chooseFolder(shell, "My dialog box", "D:\\") ;
		
		System.out.println("Selected folder = " + folder);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
