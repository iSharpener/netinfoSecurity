package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class dssCoding {
	/**
	 * 	文件读取函数
	 * @param file File
	 * @return	result.toString().replaceAll("\n", "") String
	 */
	public static String readFile(File file) {  
        StringBuilder result =new StringBuilder();  
        try {  
            BufferedReader br =new BufferedReader(new FileReader(file));  
            String s =null;  
            while((s =br.readLine()) != null) { //一次读一行内容  
                result.append(System.lineSeparator() +s);  
            }  
            br.close();  
        } catch (FileNotFoundException e) {  
          e.printStackTrace();  
        } catch (IOException e) {  
          e.printStackTrace();  
        }     
        return result.toString().replaceAll("\n", "");  
    } 
	/**
	 * 文件写入函数
	 * @param file File
	 * @param str String
	 * @return String
	 */
	public static String saveFile(File file,String str) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		
	}
}
