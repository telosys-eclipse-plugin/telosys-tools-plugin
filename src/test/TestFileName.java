package test;

import java.io.File;

public class TestFileName {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		test("D:/aaa/bbb/ccc.txt");
		test("D:\\aaa\\bbb\\ccc.txt");
		test("D:\\aaa/bbb/ccc.txt");
		test("D:/aaa/bbb");
	}
	
	public static void test(String fileName ) {
		File file = new File(fileName);
		System.out.println("------" );
		System.out.println("path = " + file.getPath() );
		System.out.println("name = " + file.getName() );
		System.out.println("parent = " + file.getParent() );
		
	}

}
