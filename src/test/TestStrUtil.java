package test;


/**
 * @author l.guerin
 *
 */
public class TestStrUtil {

	//private final static String VOID  = "" ;
	private final static String QUOTE = "\"" ;
	
	public static String addQuotes ( String s ) {
		if ( s == null ) return null ;
		if ( s.charAt(0) != '"' && s.charAt(s.length()-1) != '"' )
		{
			return QUOTE + s + QUOTE ;
		}
		return s ;
	}
	
	public static void test (String s) {
		System.out.println( " . " + s + "  --> : " + addQuotes ( s ) );
	}
	public static void main(String[] args) {
		test ("abcd");
		test (null);
		test ("'azer'");
		test ("\"azer\"");
		test ("\"azer");
		test ("azer\"");
	}
}
