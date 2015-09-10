package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.scanner;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordPatternRule;
import org.eclipse.jface.text.rules.WordRule;
import org.telosys.tools.eclipse.plugin.editors.dsl.common.EditorsUtils;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorColorManager;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorException;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorUtil;

/**
 * Scanner rules.
 */
public class EntityScanner extends RuleBasedScanner {

    public EntityScanner(EntityEditorColorManager manager) throws EntityEditorException {

    	int ruleIndex = 0; 
        IRule[] rules = new IRule[5];

        //----- Add generic whitespace rule.
        rules[ruleIndex++] = new WhitespaceRule(new EntityWhitespaceDetector());

        //----- Entity Rule - MAJ
        IToken entityRule = new Token(new TextAttribute(
                manager.getColor(EntityEditorColorManager.ENTITY_COLOR)));
        rules[ruleIndex++] = new WordRule(new EntityObjectDetector(), entityRule);

//        //----- Entity Rule - Enum
//        rules[ruleIndex++] = new WordPatternRule(new EntityEnumDetector(), "#", null,
//                entityRule);

        //----- Comment rule
        IToken commentRule = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.COMMENT_COLOR)));
        rules[ruleIndex++] = new EndOfLineRule("//", commentRule);

        //----- String rule
        IToken stringRule = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.STRING_COLOR)));
        rules[ruleIndex++] = new SingleLineRule("\"", "\"", stringRule);

        //----- Default Rule
        IToken defaultRule = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.DEFAULT_COLOR)) );
        WordRule wordRule = new WordRule(new EntityDefaultDetector(), defaultRule);

        // Type Rule (list of specific words)
        IToken typeToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.TYPE_COLOR)) );
//        for (String str : EditorsUtils.getProperty("entity.types").split(",")) {
        for (String str : EntityEditorUtil.getEntityFieldTypes() ) {
            wordRule.addWord(str, typeToken);
        }

        // Validation Rule
        IToken annotationToken = new Token(new TextAttribute( manager.getColor(EntityEditorColorManager.ANNOTATION_COLOR)));
//        for (String str : EditorsUtils.getProperty("validation.rules").split(
//                ",")) {
        for (String str : EntityEditorUtil.getEntityFieldAnnotations()) {
            wordRule.addWord(str, annotationToken);
        }

        rules[ruleIndex++] = wordRule;

        setRules(rules);
    }
}
