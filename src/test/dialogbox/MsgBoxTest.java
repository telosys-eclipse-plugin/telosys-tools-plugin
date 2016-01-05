package test.dialogbox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.telosys.tools.eclipse.plugin.commons.MsgBox;

public class MsgBoxTest 
{	
	  public static void main(String[] args) {
		  
		    Display display = new Display();
		    Shell shell = new Shell(display);
		    shell.setSize(600, 200);
		    shell.setText("MsgBox examples");
		    shell.setLayout(new RowLayout());

		    createContent(shell);
		    
		    shell.open();
		    while (!shell.isDisposed()) {
		      if (!display.readAndDispatch())
		        display.sleep();
		    }
		    display.dispose();
		  }
	  
	  private static void createContent(Composite parent) {
		  
		  createButton1(parent)  ;
		  createButton2(parent)  ;
		  createButton3(parent)  ;
	  }

	  private static void createButton1(Composite parent) {
		    Button button = new Button(parent, SWT.PUSH);
		    button.setText("MsgBox.error(msg)");
		    button.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent event) {
		    	  MsgBox.error("My error message");
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {
		      }
		    });
	  }
	  
	  private static void createButton2(Composite parent) {
		    Button button = new Button(parent, SWT.PUSH);
		    button.setText("MsgBox.error(msg, exception)");
		    button.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent event) {
		    	  RuntimeException e = new RuntimeException("My exception message");
		    	  MsgBox.error("My error message", e);
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {
		      }
		    });
	  }

	  private static void createButton3(Composite parent) {
		    Button button = new Button(parent, SWT.PUSH);
		    button.setText("MsgBox.error(title, message)");
		    button.addSelectionListener(new SelectionListener() {
		      public void widgetSelected(SelectionEvent event) {
		    	  MsgBox.error("My title", "My error message");
		      }
		      public void widgetDefaultSelected(SelectionEvent event) {
		      }
		    });
	  }
}
