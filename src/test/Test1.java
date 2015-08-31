package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {

    private final static SimpleDateFormat DATE_FORMAT   = new SimpleDateFormat("yyyyMMdd.HHmmss");

    private static String getUpdateLogFile(String sRepoFile)
    {
    	Date now = new Date();
    	String suffix = ".update." + DATE_FORMAT.format( now ) + ".log";
    	if ( sRepoFile.endsWith(".dbrep") )
    	{
    		int last = sRepoFile.length() - 6 ; 
    		return sRepoFile.substring(0,last) + suffix ;
    	}
    	else
    	{
    		return sRepoFile + suffix ;
    	}
    }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println( getUpdateLogFile("toto.dbrep") );
		System.out.println( getUpdateLogFile("myFile.dbrep") );

	}

}
