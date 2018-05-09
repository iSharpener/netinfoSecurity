package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;

import javax.xml.ws.handler.MessageContext;

public class md5Coding {
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
	/**
	 * 通过加盐的方式生成32位的md5
	 * @param key
	 * @return
	 */
	public static String MD5(String key) {
		try {
			//得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(key.getBytes());
			StringBuffer buffer = new StringBuffer();
			//把每一个byte做一个与运算 0xff
			for (byte b : result) {
				//与运算
				int number = b & 0xff; //加盐
				String str = Integer.toHexString(number);
				if(str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			return buffer.toString();
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	public static void main(String[] args) throws IOException {
		File file = new File("md5coding.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		String str = readFile(file);
		String finalstr = MD5(str);
		System.out.println("通过MD5加密之后的结果是:"+finalstr);
		File finalfile = new File("md5final.txt");
		if(!finalfile.exists()) {
			finalfile.createNewFile();
		}
		saveFile(finalfile, finalstr);
	}
}
