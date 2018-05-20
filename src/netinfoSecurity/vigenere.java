package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
	/**
	 * 将字符串转换为ascii码
	 * @param value
	 * @return
	 */
	public static ArrayList<Integer> stringToAscii(String value) {
		ArrayList<Integer> sbu = new ArrayList<Integer>();
		char[] chars = value.toCharArray();
			for(int i = 0;i < chars.length; i++) {
			if(i!=chars.length-1) {
				sbu.add((int)chars[i]);
			}else {
				sbu.add((int)chars[i]);
			}
			}
			return sbu;
 	}
	public static ArrayList<Integer> vigenere1(ArrayList<Integer> plain,ArrayList<Integer> key) {
		int rows;
		int cols;
		if(plain.size() > key.size()) {
			cols = key.size();
			if(plain.size() % key.size() == 0) {
				rows = plain.size()/key.size();
			}else {
				rows = plain.size()/key.size()+1;
			}
		}else {
			cols = plain.size();
			rows = 1;
		}
		System.out.println("rows"+rows);
		System.out.println("cols"+cols);
		ArrayList<Integer> a = new ArrayList<Integer>();
		int e = 0;
		for(int i=0;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				if(i*cols+j < plain.size()) { 
					 e = (plain.get(i*cols+j)+key.get(j))%256;
				     a.add(e);
				}
			}
		}
		System.out.println("密文的ascii:"+a);
		return a;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 File file =new File("vigenerecoding.txt");  
		 if(!file.exists()) {
			 file.createNewFile();
		 }
	     System.out.println("明文:"+readFile(file));  
	     ArrayList<Integer> plain_ascii = stringToAscii(readFile(file));
	     String key = "xiaopeng";
	     ArrayList<Integer> keyascii = stringToAscii(key);
	     System.out.println(plain_ascii);
	     System.out.println(keyascii);
	     System.out.println();
	     ArrayList<Integer> outcome = vigenere1(plain_ascii, keyascii);
	     ArrayList<Character> charset = new ArrayList<Character>();
	     for(int i=0;i<outcome.size();i++) {
	    	 charset.add((char)((int)outcome.get(i)));
	     }
	     StringBuffer finalstr = new StringBuffer();
	     for(int j =0;j<charset.size();j++) {
	    	 System.out.print(charset.get(j));
	    	 finalstr.append(charset.get(j));
	     }
	     File finalfile = new File("vigenerefinal.txt");
	     if(!file.exists()) {
	    	 finalfile.createNewFile();
	     }
	     saveFile(finalfile, finalstr.toString());
	}

}
