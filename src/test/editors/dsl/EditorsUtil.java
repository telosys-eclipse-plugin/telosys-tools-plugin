package test.editors.dsl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class EditorsUtil {

	private static void print(String msg) {
		System.out.println( msg );
	}
	
    public static void printEditors()  {
    
	    Display.getDefault().asyncExec(new Runnable() {
	        @Override
	        public void run() {
	            //IWorkbenchWindow iw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	    		IEditorReference[] editors = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
	    		print("Editors ( count = " + editors.length + " )");
	    		for ( IEditorReference editor : editors ) {
	    			print(" . getId()        = " + editor.getId()  );
	    			print(" . getName()      = " + editor.getName() );
	    			print(" . getClass()     = " + editor.getClass() );
	    			print(" . getFactoryId() = " + editor.getFactoryId() );
	    			
	    			//--------------------------------------------------------
	    			IEditorInput editorInput;
					try {
						editorInput = editor.getEditorInput();
		    			if ( editorInput != null ) {
			    			print(" . editorInput.getName()  = " + editorInput.getName() );
			    			print(" . editorInput.getClass() = " + editorInput.getClass() );
		    			}
		    			else {
			    			print(" . editorInput is null !");	    				
		    			}
					} catch (PartInitException e) {
		    			print(" . getEditorInput() : ERROR - PartInitException !");	    				
					}
	    			
	    			//--------------------------------------------------------
	    			// NB : The editor can be visible in Eclipse and not yet initialized => use 'true' to force init
	    			//IEditorPart editorPart = editor.getEditor(true);
	    			IEditorPart editorPart = editor.getEditor(false); 
	    			//Returns null if the editor was not instantiated or it failed to be restored.
	    			if ( editorPart != null ) {
		    			print(" . editorPart.getClass() = " + editorPart.getClass() );
		    			print(" . editorPart.getTitle() = " + editorPart.getTitle() );
	    			}
	    			else {
		    			print(" . editor part is null !");	    				
	    			}
	    			print(" ----- " );
	    			
	    		}
	        }
	    });
    }
}
