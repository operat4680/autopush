package kr.co.autopush.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriteUtil {
	public static void write(String s){
		try {
		String name = "D:/result.txt";
		File file = new File(name);
	 
		FileWriter fw = new FileWriter(file);
		fw.write(s);
		fw.flush();
		fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    

	}
}
