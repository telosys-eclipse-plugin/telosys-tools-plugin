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
import org.telosys.tools.eclipse.plugin.editors.dsl.common.ColorManager;
import org.telosys.tools.eclipse.plugin.editors.dsl.common.EditorsUtils;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.EntityEditorException;

/**
 * Scanner rules.
 */
public class EntityScanner extends RuleBasedScanner {

    public EntityScanner(ColorManager manager) throws EntityEditorException {

        IRule[] rules = new IRule[6];

        // Add generic whitespace rule.
        rules[0] = new WhitespaceRule(new EntityWhitespaceDetector());

        // Entity Rule - MAJ
        IToken entityRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.ENTITY_COLOR)));
        rules[1] = new WordRule(new EntityObjectDetector(), entityRule);

        // Entity Rule - Enum
        rules[2] = new WordPatternRule(new EntityEnumDetector(), "#", null,
                entityRule);

        // Comment rule
        IToken commentRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.COMMENT_COLOR)));
        rules[3] = new EndOfLineRule("//", commentRule);

        // String rule
        IToken stringRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.STRING_COLOR)));
        rules[4] = new SingleLineRule("\"", "\"", stringRule);

        // Default Rule
        IToken defaultRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.DEFAULT_COLOR)));
        WordRule typewr = new WordRule(new EntityDefaultDetector(), defaultRule);

        // Type Rule
        IToken typeRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.TYPE_COLOR)));
        for (String str : EditorsUtils.getProperty("entity.types").split(",")) {
            typewr.addWord(str, typeRule);
        }

        // Validation Rule
        IToken validationRule = new Token(new TextAttribute(
                manager.getColor(ColorManager.VALIDATION_COLOR)));
        for (String str : EditorsUtils.getProperty("validation.rules").split(
                ",")) {
            typewr.addWord(str, validationRule);
        }

        rules[5] = typewr;

        setRules(rules);
    }
}
