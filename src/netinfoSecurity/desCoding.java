package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
public class desCoding {
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
	 * 
	 * @param datasource byte[]
	 * @param password String
	 * @return byte[]
	 */
	public static byte[] encode(byte[] datasource,String password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			//创建一个密匙工厂
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = kf.generateSecret(desKey);
			//使用Cipher对象完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			//使用密匙初始化Cipher对象,ENCRYPT_MODE用于将Cipher初始化为加密模式的常量
			cipher.init(Cipher.ENCRYPT_MODE, securekey,random);
			//获取数据并加密
			//正式执行加密操作
			return cipher.doFinal(datasource);
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param src byte[]
	 * @param password String
	 * @return byte[]
	 */
	public static byte[] decode(byte[] src,String password) {
		try {
			//DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			//创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			//创建一个密匙工厂
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
			//将DESKeySpec对象转换为SecretKey对象
			SecretKey securekey = kf.generateSecret(desKey);
			//Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			//用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey,random);
			return cipher.doFinal(src);
			
		}catch(Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 File file =new File("descoding.txt");  
		 if(!file.exists()) {
			 file.createNewFile();
		 }
		 //需要加密的文本，从txt文件当中读取
		 String str = readFile(file);
		 System.out.println("明文是:");
		 System.out.println(str);
		 //密码，长度需要是8的倍数
		 String password = "95880288";
		 byte[] result = encode(str.getBytes(), password);
		 System.out.println("通过DES算法加密之后的密文为:");
		 System.out.println(new String(result));
		 //[B@47fd17e3
		 System.out.println("解密之后为:");
		 String finalstr = new String(decode(result,password));
		 System.out.println(finalstr);
		 //创建保存密文的文件
		 File savefile = new File("desfinal.txt");
		 if(!savefile.exists()) {
			 file.createNewFile();
		}
		 saveFile(savefile, new String(result));
	}
}
