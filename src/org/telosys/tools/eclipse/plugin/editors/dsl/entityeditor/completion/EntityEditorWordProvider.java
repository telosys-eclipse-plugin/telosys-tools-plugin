package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.telosys.tools.eclipse.plugin.editors.dsl.common.EditorsUtils;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorException;

/**
 * Word provider for autocompletion.
 * 
 */
public class EntityEditorWordProvider {

    public List<String> suggest(String word, int context)
            throws EntityEditorException {
        ArrayList<String> wordBuffer = new ArrayList<String>();
        switch (context) {
        case EditorsUtils.TYPE:
            for (String str : EditorsUtils.getProperty("entity.types").split(
                    ",")) {
                if (str.startsWith(word)) {
                    wordBuffer.add(str + " ");
                }
            }
            for (String str : getFileDirectory()) {
                if (str.startsWith(word)) {
                    wordBuffer.add(str);
                }
            }
            break;

        case EditorsUtils.ANNOTATION:
            for (String str : EditorsUtils.getProperty("validation.rules")
                    .split(",")) {
                if (str.startsWith(word)) {
                    wordBuffer.add(str);
                }
            }
            break;

        default:
            break;
        }

        return wordBuffer;
    }

    /**
     * 
     * @return List of entity and enum name in the same directory
     */
    private List<String> getFileDirectory() {

        List<String> fileList = new ArrayList<String>();
        FilenameFilter telosysFilter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".enum") || arg1.endsWith(".entity");
            }
        };

        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        IEditorInput input = editor.getEditorInput();
        IPath path = ((FileEditorInput) input).getPath();

        File repertoire = new File(path.toFile().getParent());
        for (String str : repertoire.list(telosysFilter)) {
            if (str.contains(".enum")) {
                str = str.replace(".enum", "");
                str = str.substring(0, 1).toUpperCase() + str.substring(1);
                str = "#" + str;
            } else {
                str = str.replace(".entity", "");
                str = str.substring(0, 1).toUpperCase() + str.substring(1);
            }
            fileList.add(str);
        }

        return fileList;
    }

}
