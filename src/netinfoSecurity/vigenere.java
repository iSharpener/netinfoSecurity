package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class vigenere {
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
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 File file =new File("test.txt");  
		 if(!file.exists()) {
			 file.createNewFile();
		 }
	     System.out.println(readFile(file));  
	}

}
