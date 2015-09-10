package org.telosys.tools.eclipse.plugin.commons.dialogbox;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Laurent GUERIN
 *
 */
public class GenerationTaskMsgBox
{
	//----------------------------------------------------------------------------------
	public static boolean error(String title, String message, Throwable e) 
	{
		MessageDialog dialog = new MessageDialog(null, 
				title, null, 
				message, MessageDialog.ERROR, 
				new String[]{
				"Continue", "Cancel" }, 0); 
		int r = dialog.open();
		return r == 0; // TRUE if "continue", else FALSE
	}
}
