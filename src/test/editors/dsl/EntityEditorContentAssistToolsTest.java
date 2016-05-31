package test.editors.dsl;

import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion.EntityEditorContentAssistTools;
import org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor.completion.EntityEditorSuggestContext;

public class EntityEditorContentAssistToolsTest {
	
	public static void main(String arg[]) {

//		System.out.println(" void ? " + isVoidChar(' ') );
//		System.out.println(" void ? " + isVoidChar('\t') );
//		System.out.println(" void ? " + isVoidChar(';') );
		
		callGetContext("");
		callGetContext(":");
		callGetContext(" aaa");
		callGetContext(" : str");
		callGetContext(" : str ");
		callGetContext(":str");
		callGetContext(": string  ");
		callGetContext(": string ; ");
		callGetContext(": string {  ");
		callGetContext(": string { aa");
		callGetContext(": string { aa ");
		callGetContext(": string { @aa");
		callGetContext(": string { @aa ");
		callGetContext(": string { @aa,");
		callGetContext(": string { @aa ,");
		callGetContext(": string { @aa , ");
		callGetContext(": string \n { \t@aa");
		callGetContext(": string \n { \t@aa, \n @bb");
		callGetContext(": string { @aa , @bb}");
		callGetContext(": string { @aa , @bb;");
		callGetContext("Foo {");

	}
	private static void callGetContext(String s) {
		System.out.println("getContext('" + s +"')");
		EntityEditorSuggestContext suggestContext = EntityEditorContentAssistTools.getSuggestContext(s);
		System.out.println("  word    = '" + suggestContext.getWord() + "'");
		System.out.println("  context = " + suggestContext.getContext() );
	}
	
}
