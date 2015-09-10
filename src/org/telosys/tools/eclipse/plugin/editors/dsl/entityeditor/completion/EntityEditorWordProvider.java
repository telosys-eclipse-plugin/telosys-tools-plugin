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
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorContext;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorException;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorUtil;

/**
 * Word provider for auto-completion.
 * 
 */
public class EntityEditorWordProvider {

	private final static String DOT_ENTITY = ".entity";
	
//    public List<String> suggest(String word, int context)
    public List<String> suggest(String word, EntityEditorContext context)
            throws EntityEditorException {
        ArrayList<String> suggestedWords = new ArrayList<String>();
        switch (context) {
//        case EditorsUtils.TYPE:
        case TYPE: // suggest for a "type"
//            for (String str : EditorsUtils.getProperty("entity.types").split(
//                    ",")) {
        	for ( String str : EntityEditorUtil.getEntityFieldTypes() ) {
                if (str.startsWith(word)) {
                    suggestedWords.add(str + " ");
                }
            }
            for (String str : getListOfDefinedEntities()) {
                if (str.startsWith(word)) {
                    suggestedWords.add(str + " ");
                }
            }
            break;

//        case EditorsUtils.ANNOTATION:
        case ANNOTATION: // suggest for an "annotation"
//            for (String str : EditorsUtils.getProperty("validation.rules")
//                    .split(",")) {
        	for ( String str : EntityEditorUtil.getEntityFieldAnnotations() ) {
                if (str.startsWith(word)) {
                    suggestedWords.add(str);
                }
            }
            break;

        default:
            break;
        }

        return suggestedWords;
    }

    /**
     * 
     * @return List of '.entity' files located in the same directory
     */
    private List<String> getListOfDefinedEntities() {

        List<String> fileList = new ArrayList<String>();
        
        FilenameFilter entitiesFilter = new FilenameFilter() {
            public boolean accept(File arg0, String arg1) {
                //return arg1.endsWith(".enum") || arg1.endsWith(".entity");
                return arg1.endsWith(DOT_ENTITY);
            }
        };

        IWorkbench wb = PlatformUI.getWorkbench();
        IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        IEditorInput input = editor.getEditorInput();
        IPath path = ((FileEditorInput) input).getPath();

        File currentFolder = new File(path.toFile().getParent());
        for (String str : currentFolder.list(entitiesFilter)) {
//            if (str.contains(".enum")) {
//                str = str.replace(".enum", "");
//                str = str.substring(0, 1).toUpperCase() + str.substring(1);
//                str = "#" + str;
//            } else {
//                str = str.replace(".entity", "");
//                str = str.substring(0, 1).toUpperCase() + str.substring(1);
//            }
        	// transform "foo.entity" or "Foo.entity" to "Foo"
            str = str.replace(DOT_ENTITY, "");
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
            fileList.add(str);
        }

        return fileList;
    }

}
