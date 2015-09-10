package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion.EntityEditorContentAssistProcessor;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner.EntityScanner;

public class EntityEditorConfiguration extends TextSourceViewerConfiguration {
    private ITokenScanner scanner = null;

    @Override
    public IPresentationReconciler getPresentationReconciler(
            ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());

        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }

    private ContentAssistant assistant = null;

    @Override
    public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
        if (assistant == null) {
            assistant = new ContentAssistant();
            assistant.setContentAssistProcessor(
                    new EntityEditorContentAssistProcessor(),
                    IDocument.DEFAULT_CONTENT_TYPE);
            assistant
                    .setInformationControlCreator(getInformationControlCreator(sourceViewer));
        }
        return assistant;
    }

    private ITokenScanner getScanner() {
        if (scanner == null) {
            EntityEditorColorManager manager = new EntityEditorColorManager();
            scanner = new EntityScanner(manager);
        }
        return scanner;
    }
}