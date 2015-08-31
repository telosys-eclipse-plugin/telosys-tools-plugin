package test.dialogbox;

import java.util.LinkedList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.eclipse.plugin.commons.dialogbox.TemplatesBundleDialogBox;

public class TemplatesBundleDialogBoxTest 
{	
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.open();
		
		LinkedList<String> itemsList = new LinkedList<String>();
		for ( int i = 0 ; i < 18 ; i++ ) {
			itemsList.add("Item #"+i + " qlskdkqdl"); 
		}
		String bundlesLocation = "TelosysTools/mytemplates" ;
		
		TemplatesBundleDialogBox dialogBox = new TemplatesBundleDialogBox(shell, itemsList, bundlesLocation, 0)  ;
		
		int r = dialogBox.open();
		
		System.out.println("Return : " + r);
		
		if ( r == 0 ) {
			System.out.println("OK ");
			String selection = dialogBox.getSelectedItem();
			System.out.println("Selection = " + selection );
		}
		else {
			System.out.println("CANCEL or CLOSE");
		}
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
