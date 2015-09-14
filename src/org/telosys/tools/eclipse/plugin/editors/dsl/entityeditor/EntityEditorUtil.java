package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

import java.util.Collections;
import java.util.List;

import org.telosys.tools.dsl.KeyWords;

/**
 * This utility class provides : <br>
 * - the neutral types ( "boolean", "date", "string", etc ) <br>
 * - the annotations without parenthesis ( "@Id", "@Max", "@Min", etc ) <br>
 * - the annotations with parenthesis ( "@Id", "@Max()", "@Min()", etc ) <br>
 * <br>
 * The original list of types and annotation is provided by the DSL parser <br>
 * 
 * @author L. Guerin
 *
 */
public class EntityEditorUtil {

	//--------------------------------------------------------------------------------
	// PRIVATE FIELDS AND METHODS
	//--------------------------------------------------------------------------------

	//--- TYPES MANAGEMENT
//	private final static String[] TYPES = {
//		"binary", // BLOB
//		"boolean",
//		"date",
//		"decimal",
//		"integer",
//		"longtext", // CLOB
//		"string",
//		"time",
//		"timestamp",
//	} ;
//	private final static int NUMBER_OF_TYPES = KeyWords.getNeutralTypes().size() ;
	private final static String[] TYPES ;  //= new String[NUMBER_OF_TYPES] ;
	// TYPES INITIALIZATION 
	static {
		List<String> types = KeyWords.getNeutralTypes();		
		TYPES = new String[types.size()] ;
		Collections.sort(types);
		int i = 0 ;
		for ( String s : types ) {
			TYPES[i++] = s;
		}
	}
	
	//--- ANNOTATIONS MANAGEMENT
//	private final static int NUMBER_OF_ANNOTATIONS = KeyWords.getAnnotations().size() ;
	
//	private final static String[] ANNOTATIONS = {
//		"@Future",
//		"@Id",
//		"@Max",
//		"@Min",
//		"@NotNull",
//		"@Past",
//		"@SizeMax",
//		"@SizeMin",
//	} ;
//
//	private final static String[] ANNOTATIONS_WITH_PARENTHESIS = {
//		"@Future",
//		"@Id",
//		"@Max()",
//		"@Min()",
//		"@NotNull",
//		"@Past",
//		"@SizeMax()",
//		"@SizeMin()",
//	} ;
	
	private final static String[] ANNOTATIONS ; // = new String[NUMBER_OF_ANNOTATIONS] ;
	private final static String[] ANNOTATIONS_WITH_PARENTHESIS ; //= new String[NUMBER_OF_ANNOTATIONS] ;
	
	// ANNOTATIONS INITIALIZATION 
	static {
		List<String> annotations = KeyWords.getAnnotations();
		int numberOfAnnotations = annotations.size();
		Collections.sort(annotations);
		ANNOTATIONS = new String[numberOfAnnotations] ;
		ANNOTATIONS_WITH_PARENTHESIS = new String[numberOfAnnotations] ;
		int i = 0 ;
		for ( String s : annotations ) {
			ANNOTATIONS[i] = transformAnnotation(s, false);
			ANNOTATIONS_WITH_PARENTHESIS[i] = transformAnnotation(s, true);
			i++;
		}
	}
	
	/**
	 * Transforms the annotation name given by the parser ( "Id", "NotNull", "Min#", "Max#", etc )
	 * into the real annotation name with or without parenthesis 
	 * eg : "@Id", "@NotNull", "@Min", etc  
	 * or : "@Id", "@NotNull", "@Min()", "@Max()"
	 * 		
	 * @param s
	 * @return
	 */
	private final static String transformAnnotation(String s, boolean parenthesis ) {
		String s2 = "@" + s ;
		if ( s.endsWith("#") ) {
			s2 = "@" + s.substring(0, s.length()-1 ) ;
			if ( parenthesis ) {
				s2 = s2 + "()" ;
			}
		}
		return s2;
	}
	
	//--------------------------------------------------------------------------------
	// PUBLIC METHODS
	//--------------------------------------------------------------------------------
	public final static String[] getEntityFieldTypes() {
		return TYPES ;
	}

	public final static String[] getAnnotations() {
		return ANNOTATIONS ;
	}
	public final static String[] getAnnotationsWithParenthesis() {
		return ANNOTATIONS_WITH_PARENTHESIS ;
	}
}
