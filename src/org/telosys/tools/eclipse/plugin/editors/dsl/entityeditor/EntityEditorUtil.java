package org.telosys.tools.eclipse.plugin.editors.dsl.entityeditor;

public class EntityEditorUtil {

	private final static String[] TYPES = {
		"binary", // BLOB
		"boolean",
		"date",
		"decimal",
		"integer",
		"longtext", // CLOB
		"string",
		"time",
		"timestamp",
	} ;
	
	private final static String[] ANNOTATIONS = {
		"@Future",
		"@Id",
		"@Max",
		"@Min",
		"@NotNull",
		"@Past",
		"@SizeMax",
		"@SizeMin",
	} ;
	
	public final static String[] getEntityFieldTypes() {
		return TYPES ;
	}

	public final static String[] getEntityFieldAnnotations() {
		return ANNOTATIONS ;
	}
}
