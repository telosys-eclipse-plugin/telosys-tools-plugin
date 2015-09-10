package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.telosys.tools.eclipse.plugin.editors.dsl.common.EditorsUtils;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorContext;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorException;

/*
 * TODO : establishment of the context system to improve / replace the actual method
 */
public class EntityEditorContentAssistProcessor implements
        IContentAssistProcessor {

    private final EntityEditorWordProvider wordProvider;
    private String lastError;

    /**
     * Constructor
     */
    public EntityEditorContentAssistProcessor() {
        this.wordProvider = new EntityEditorWordProvider();
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(ITextViewer textViewer, int documentOffset) {
    	
    	
        IDocument document = textViewer.getDocument();
        int currOffset = documentOffset > 0 ? documentOffset - 1
                : documentOffset;

        String currWord = "";

        try {
            char currChar = document.getChar(currOffset);

            while (currOffset > 0 && !(currChar == ';' || currChar == '\n')) {
                currWord = currChar + currWord;
                currOffset--;
                currChar = document.getChar(currOffset);
            }
        } catch (BadLocationException e1) {
            throw new EntityEditorException("Error while proposing a word : "
                    + e1);
        }

        /*
         * // First part of Method which could replace the metho to get context
         * 
         * try { while (currOffset > 0 && !(Character.isWhitespace(currChar =
         * document .getChar(currOffset)) || currChar == ';')) { currWord =
         * currChar + currWord; currOffset--; }
         * 
         * int nbLign = document.getLineOfOffset(currOffset); int taille =
         * document.getLineLength(nbLign); context =
         * chooseContext(document.get(documentOffset-taille, taille));
         * 
         * } catch (BadLocationException e1) { throw new
         * EditorsException("Error while proposing a word : " + e1); }
         */
//        int context = chooseContext(currWord);
        EntityEditorContext context = chooseContext(currWord);

        String oldCurrWord = currWord;
        String wordInProgressRev = "";

        while (oldCurrWord.length() != 0) {
            char lastChar = oldCurrWord.charAt(oldCurrWord.length() - 1);
            if (lastChar != ' ' && lastChar != '{' && lastChar != ':'
                    && lastChar != ',') {
                wordInProgressRev += lastChar;
                oldCurrWord = oldCurrWord
                        .substring(0, oldCurrWord.length() - 1);
            } else {
                break;
            }
        }

        currWord = new StringBuilder(wordInProgressRev).reverse().toString();

        /*
         * // Second part of Method which could replace the metho to get context
         * 
         * try { int nbLign = document.getLineOfOffset(currOffset); int taille =
         * document.getLineLength(nbLign); context =
         * chooseContext(document.get(documentOffset-taille, taille)); } catch
         * (BadLocationException e1) { // TODO Auto-generated catch block
         * e1.printStackTrace(); }
         */

        List<String> suggestions = wordProvider.suggest(currWord, context);
        ICompletionProposal[] proposals = null;

        if (suggestions.size() > 0) {
            try {
                proposals = buildProposals(suggestions, currWord,
                        documentOffset - currWord.length());
            } catch (Exception e) {
                throw new EntityEditorException(
                        "Error while proposing a word : " + e);
            }
        }
        return proposals;
    }

    private ICompletionProposal[] buildProposals(List<String> suggestions,
            String replacedWord, int offset) throws Exception {
        ICompletionProposal[] proposals = new ICompletionProposal[suggestions
                .size()];

        int index = 0;

        for (Iterator<String> i = suggestions.iterator(); i.hasNext();) {
            String currSuggestion = (String) i.next();
            CompletionProposal cp = new CompletionProposal(currSuggestion,
                    offset, replacedWord.length(), currSuggestion.length(),
                    null, currSuggestion, null, null);
            proposals[index] = cp;
            index++;
        }

        return proposals;
    }

    @Override
    public IContextInformation[] computeContextInformation(
            ITextViewer itextviewer, int i) {
        lastError = "No Context Information available";
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return lastError;
    }

    /***
     * 
     * @param line
     *            : the line of the current word
     * @return the type of context DEFAULT, ANNOTATION, TYPE
     */
//    public int chooseContext(String line) {
      public EntityEditorContext chooseContext(String line) {

        // empty line
        if (line.length() == 0) {
//            return EditorsUtils.DEFAULT;
            return EntityEditorContext.DEFAULT;
        } else {
            String reverseWord = new StringBuilder(line).reverse().toString();
            int indexType = reverseWord.indexOf(':');
            int indexAnnotation = reverseWord.indexOf('{');

            if (reverseWord.charAt(0) == '[' || indexType == -1
                    && indexAnnotation == -1) {
//                return EditorsUtils.DEFAULT;
                return EntityEditorContext.DEFAULT;
                
            } else if (indexType == -1 && indexAnnotation != -1) {
//                return EditorsUtils.ANNOTATION;
                return EntityEditorContext.ANNOTATION;
                
            } else if (indexAnnotation == -1 && indexType != -1) {
//                return EditorsUtils.TYPE;
                return EntityEditorContext.TYPE;
                
            } else {
//                return indexAnnotation < indexType ? EditorsUtils.ANNOTATION
//                        : EditorsUtils.TYPE;
                if ( indexAnnotation < indexType ) {
                	return EntityEditorContext.ANNOTATION;
                }
                else {
                	return EntityEditorContext.TYPE;
                }
            }
        }
    }
}