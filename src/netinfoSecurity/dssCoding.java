package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

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
	/**
	 * 	获取密钥对
	 * @return	KeyPair
	 */
	public static KeyPair getKeyPairs() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
			keyPairGenerator.initialize(512);
			KeyPair keypair = keyPairGenerator.genKeyPair();
			return keypair;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 从密钥对里面获取公钥
	 * @param keypair
	 * @return
	 */
	public static PublicKey getPublicKey(KeyPair keypair){
		return keypair.getPublic();
	}
	/**
	 * 从密钥对里面获取私钥
	 * @param keypair
	 * @return
	 */
	public static PrivateKey getPrivateKey(KeyPair keypair) {
		return keypair.getPrivate();
	}
	/**
	 * 使用私钥进行签名
	 * @param info
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws SignatureException 
	 */
	public static byte[] SignatureData(String info){
		try {
			KeyPair keypair = getKeyPairs();
			PrivateKey privatekey = getPrivateKey(keypair);
			//初始化签名对明文进行签名
			Signature signature = Signature.getInstance("DSA");
			signature.initSign(privatekey);
			signature.update(info.getBytes());
			//对信息的数字签名
			byte[] signedbytes = signature.sign();
			String signaturestr = new String(signedbytes); 
			System.out.println("进行签名成功，签名为:"+signaturestr);
			return signedbytes;
		}catch(Exception e) {
			System.out.println("进行签名失败");
			return null;
		}
	}
	/**
	 * 签名校验
	 * @param info
	 * @param bytes
	 * @return
	 */
	public static boolean checkSignature(String info,byte[] bytes) {
		try {
			//System.out.println("asdafd");
			KeyPair keypair = getKeyPairs();
			PublicKey publickey = getPublicKey(keypair);
			Signature signature = Signature.getInstance("DSA");
			signature.initVerify(publickey);
			signature.update(info.getBytes());
			if(signature.verify(bytes)) {
				System.out.println("签名的内容为:"+info);
				System.out.println("签名文件校验正常");
				return true;
			}else {
				System.out.println("校验签名失败");
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("校验签名失败");
			return false;
		}
	}
	public static void main(String[] args) throws IOException {
		KeyPair keypair = getKeyPairs();
		System.out.println("生成的公钥为:"+getPublicKey(keypair));
		System.out.println("生成的私钥为:"+getPrivateKey(keypair));
		File file = new File("dsscoding.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		byte[] bytes = SignatureData(readFile(file));
		File signfile = new File("dssfinal.txt");
		if(!signfile.exists()) {
			signfile.createNewFile();
		}
		saveFile(signfile, new String(bytes));
		//对签名进行校验
		checkSignature(readFile(file), bytes);
		
	}
}
