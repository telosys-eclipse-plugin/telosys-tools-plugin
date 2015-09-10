package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

public class EntityDocUtil {
	
	public static void main(String arg[]) {

		System.out.println(" void ? " + isVoidChar(' ') );
		System.out.println(" void ? " + isVoidChar('\t') );
		System.out.println(" void ? " + isVoidChar(';') );
		
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

	}
	private static void callGetContext(String s) {
		System.out.println("getContext('" + s +"')");
		EntityEditorSuggestContext suggestContext = getSuggestContext(s);
		System.out.println("  word    = '" + suggestContext.getWord() + "'");
		System.out.println("  context = " + suggestContext.getContext() );
	}
	
	private final static char[] VOID_CHARACTERS    = { ' ', '\t', '\n', '\r' };
	private final static char[] SPECIAL_CHARACTERS = { ':', ';', ',', '{', '}', '[', ']' };
	
	public static EntityEditorSuggestContext getSuggestContext(String s) {
		int bracesFound = 0 ;
		int commasFound = 0 ;
		boolean endOfWord = false ;
		char firstLeftSeparator = 0 ;
		StringBuffer sbReversedWord = new StringBuffer();
		// read from end to beginning (reversed order of characters)
		for ( int i = s.length() -1 ; i >= 0 ; i-- ) {
			char c = s.charAt(i);
//			System.out.println(c);
			if ( isVoidChar(c) ) {
				endOfWord = true ;
			}
			else if ( isSpecialChar(c) ) { // separator and not void
				endOfWord = true ;
				if ( firstLeftSeparator == 0 ) { // not yet set 
					firstLeftSeparator = c ; // set only once
				}
			}
			if ( ! endOfWord ) {
				sbReversedWord.append(c);
			}
			else {
				if ( c == ':' ) {
					break; // this is the end
				}
				else if ( c == ',' ) {
					commasFound++ ;
				}
				else if ( c == '{' ) {
					bracesFound++ ;
				}
			}
		}
		String word = sbReversedWord.reverse().toString();
//		System.out.println("  word = '" + word + "'");
		if ( firstLeftSeparator == '{' ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.ANNOTATION ) ;
		}
		else if ( firstLeftSeparator == ',' && bracesFound > 0 ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.ANNOTATION ) ;
		}
		else if ( firstLeftSeparator == ':' ) {
			return new EntityEditorSuggestContext(word, EntityEditorContext.TYPE ) ;
		}
		else {
			return new EntityEditorSuggestContext(word, EntityEditorContext.DEFAULT ) ;
		}
	}

	private static boolean isVoidChar(char c) {
		for ( char voidChar : VOID_CHARACTERS ) {
			if ( voidChar == c ) return true ;
		}
		return false ;
	}
	private static boolean isSpecialChar(char c) {
		for ( char specialChar : SPECIAL_CHARACTERS ) {
			if ( specialChar == c ) return true ;
		}
		return false ;
	}
}
