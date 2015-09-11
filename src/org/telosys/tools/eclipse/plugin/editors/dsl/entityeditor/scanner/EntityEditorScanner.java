package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import java.util.ArrayList;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorColorManager;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorUtil;

/**
 * A generic scanner which can be "programmed" with a sequence of rules. 
 * The scanner is used to get the next token by evaluating its rule in sequence until one is successful. 
 * If a rule returns a token which is undefined, the scanner will proceed to the next rule. 
 * Otherwise the token provided by the rule will be returned by the scanner. 
 * If no rule returned a defined token, this scanner returns a token which returns true when calling isOther, 
 * unless the end of the file is reached. In this case the token returns true when calling isEOF.
 */
public class EntityEditorScanner extends RuleBasedScanner {

    /**
     * Constructor
     * @param manager
     */
    public EntityEditorScanner(EntityEditorColorManager manager) { // throws EntityEditorException {
    	
    	final IToken DEFAULT_TOKEN = 
    		new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.DEFAULT_COLOR)) );
    	final IToken ENTITY_NAME_TOKEN = 
    		new Token(new TextAttribute(manager.getColor(EntityEditorColorManager.ENTITY_COLOR)));
        final IToken COMMENT_TOKEN = 
        	new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.COMMENT_COLOR)));
        final IToken LITERAL_STRING_TOKEN = 
        	new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.STRING_COLOR)));
        final IToken TYPE_TOKEN = 
        	new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.TYPE_COLOR)) );
        final IToken ANNOTATION_TOKEN = 
        	new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.ANNOTATION_COLOR)));

    	// rules are evaluated in sequence until one is successful
//    	int ruleIndex = 0; 
//        IRule[] rules = new IRule[6];
        ArrayList<IRule> rulesList = new ArrayList<IRule>();

        // Different kinds of rules :
        // . EndOfLineRule
        // . MultiLineRule
        // . NumberRule
        // . PatternRule
        // . SingleLineRule
        // . WhitespaceRule
        // . WordPatternRule
        // . WordRule
        // . IPredicateRule
        
        //----- Rule for white spaces 
        // "WhitespaceRule"
        // An implementation of IRule capable of detecting whitespace. 
        // A whitespace rule uses a whitespace detector in order to find out which characters are whitespace characters
//        rules[ruleIndex++] = new WhitespaceRule(new EntityWhitespaceDetector()); // uses default token : Token.WHITESPACE
        rulesList.add( new WhitespaceRule(new EntityWhitespaceDetector()) ); 

        //----- Rule for comment : // abcd 
        // "EndOfLineRule"
//        IToken commentToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.COMMENT_COLOR)));
//        rules[ruleIndex++] = new EndOfLineRule("//", commentToken);
//        rules[ruleIndex++] = new EndOfLineRule("//", COMMENT_TOKEN);
        rulesList.add( new EndOfLineRule("//", COMMENT_TOKEN) );
        
        //----- Rule for Entity Name : Book, Country, etc
        // "WordRule"
        // An implementation of IRule capable of detecting words. 
        // A word rule also allows to associate a token to a word. 
        // That is, not only can the rule be used to provide tokens for exact matches, 
        // but also for the generalized notion of a word in the context in which it is used. 
        // A word rule uses a word detector to determine what a word is.
//        IToken entityNameToken = new Token(new TextAttribute(manager.getColor(EntityEditorColorManager.ENTITY_COLOR)));
//        rules[ruleIndex++] = new WordRule(new EntityNameDetector(), entityNameToken);
//        rules[ruleIndex++] = new WordRule(new EntityNameDetector(), ENTITY_NAME_TOKEN);
        rulesList.add( new WordRule(new EntityNameDetector(), ENTITY_NAME_TOKEN) );
        

//        //----- Entity Rule - Enum
//        rules[ruleIndex++] = new WordPatternRule(new EntityEnumDetector(), "#", null,
//                entityRule);


        //----- Rule for literal string : "abcd" 
        // A specific configuration of pattern rule whereby the pattern begins with a specific sequence 
        // and may end with a specific sequence, but will not span more than a single line.
//        IToken literalStringToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.STRING_COLOR)));
//        rules[ruleIndex++] = new SingleLineRule("\"", "\"", literalStringToken);
//        rules[ruleIndex++] = new SingleLineRule("\"", "\"", LITERAL_STRING_TOKEN);
        rulesList.add( new SingleLineRule("\"", "\"", LITERAL_STRING_TOKEN) );
        
		//----- Rule for annotation names : @Id @Max
// Not restrictive enough : color every word starting with @ and mathing the detector criteria
//		WordPatternRule annotationRule = new WordPatternRule(new AnnotationDetector(), "@", null, ANNOTATION_TOKEN);
        WordRule annotationRule = new WordRule(new FieldAnnotationDetector(), DEFAULT_TOKEN);
        for (String word : EntityEditorUtil.getAnnotations()) {
            annotationRule.addWord(word, ANNOTATION_TOKEN);
        }
//		rules[ruleIndex++] = annotationRule ;
		rulesList.add(annotationRule);
		
		//----- Rule for annotation parenthesis : (20) () 
//		rules[ruleIndex++] = new SingleLineRule( "(",  ")", ANNOTATION_TOKEN);
		rulesList.add( new SingleLineRule( "(",  ")", ANNOTATION_TOKEN) );

		//----- Rule for neutral types : "integer", "string", "date", ...
//        IToken defaultRule = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.DEFAULT_COLOR)) );
        WordRule wordRule = new WordRule(new FieldTypeDetector(), DEFAULT_TOKEN); // default color 

        // Type Rule (list of specific words)
//        IToken typeToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.TYPE_COLOR)) );
//        for (String str : EditorsUtils.getProperty("entity.types").split(",")) {
        for (String str : EntityEditorUtil.getEntityFieldTypes() ) {
            wordRule.addWord(str, TYPE_TOKEN); // specific color for predefined types "string", "date", etc
        }

//        // Annotation Rule
//        IToken annotationToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.ANNOTATION_COLOR)));
////        for (String str : EditorsUtils.getProperty("validation.rules").split(
////                ",")) {
//        for (String str : EntityEditorUtil.getEntityFieldAnnotations()) {
//            wordRule.addWord(str, annotationToken);
//        }

//        rules[ruleIndex++] = wordRule;
		rulesList.add(wordRule);

		//        setRules(rules);
		
		IRule[] rulesArray = rulesList.toArray( new IRule[0] );

        setRules(rulesArray);
    }
}
