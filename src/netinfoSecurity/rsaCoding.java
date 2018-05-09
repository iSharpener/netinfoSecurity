package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.Cipher;

public class rsaCoding {
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
	 * 生成密钥对
	 * @return keypair KeyPair
	 * @throws Exception
	 */
	public static KeyPair getKeyPair() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	/**
	 * 获取私钥(Base64编码)
	 * @param keypair
	 * @return
	 */
	public static String getPrivateKey(KeyPair keypair) {
		PrivateKey privateKey = keypair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		//定义一个BASE64Encoder
		Base64.Encoder encode = Base64.getEncoder();
		//将byte[]转换为base64
		byte[] base64 = encode.encode(bytes);
		return new String(base64);
	}
	/**
	 * 获取公钥(Base64编码)
	 * @param keypair
	 * @return
	 */
	public static String getPublicKey(KeyPair keypair) {
		PublicKey publicKey = keypair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		//定义一个BASE64Encoder
		Base64.Encoder encode = Base64.getEncoder();
		//将byte[]转换为base64
		byte[] base64 = encode.encode(bytes);
		return new String(base64);
	}
	/**
	 * 将私钥转换为PrivateKey类型
	 * @param pristr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey string2privatekey(String pristr) throws Exception {
		Base64.Decoder decode = Base64.getDecoder();
		//将base64转换为byte[]
		byte[] bytes = decode.decode(pristr.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey pk = kf.generatePrivate(keySpec);
		return pk;
	}
	/**
	 * 将公钥转换为PublicKey类型
	 * @param pristr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey string2publickey(String pubstr) throws Exception {
		Base64.Decoder decode = Base64.getDecoder();
		//将base64转换为byte[]
		byte[] bytes = decode.decode(pubstr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pk = kf.generatePublic(keySpec);
		return pk;
	}
	/**
	 * 使用公钥进行加密
	 * @param content
	 * @param publickey
	 * @return
	 * @throws Exception
	 */
	public static byte[] publiccode(byte[] content,PublicKey publickey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publickey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	/**
	 * 使用私钥进行解密
	 * @param content
	 * @param privatekey
	 * @return
	 * @throws Exception
	 */
	public static byte[] privatecode(byte[] content,PrivateKey privatekey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privatekey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	public static void main(String[] args) throws Exception {
		File file = new File("rsacoding.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		//生成RSA公钥和私钥,并base64编码
		KeyPair keyPair = getKeyPair();
		String pubks = getPublicKey(keyPair);
		String priks = getPrivateKey(keyPair);
		System.out.println("RSA公钥的base64编码:\n"+pubks);
		System.out.println("RSA私钥的base64编码:\n"+priks);
		//将编码后的公钥转换为PublicKey对象
		PublicKey publickey = string2publickey(pubks);
		//使用公钥加密
		byte[] publicbyte = publiccode(readFile(file).getBytes(), publickey);
		//定义一个BASE64Encoder
		Base64.Encoder encode = Base64.getEncoder();
		//将byte[]转换为base64
		byte[] base64 = encode.encode(publicbyte);
		String pubstr = new String(base64);
		System.out.println("公钥加密并且base64编码之后的结果为:\n"+pubstr);
		//将加密后的密文存入到文件当中
		File finalfile = new File("rsafinal.txt");
		if(!finalfile.exists()) {
			finalfile.createNewFile();
		}
		saveFile(finalfile, pubstr);
		
		//将编码后的私钥转换为PrivateKey对象
		PrivateKey privatekey = string2privatekey(priks);
		//定义一个BASE64Encoder
		Decoder decode = Base64.getDecoder();
		//将byte[]转换为base64
		byte[] bytes = decode.decode(readFile(finalfile));
		//使用私钥解密
		byte[] privatebyte = privatecode(bytes, privatekey);
		String pristr = new String(privatebyte);
		System.out.println("私钥解密之后并且转换为byte[]的结果为:"+pristr);
	
	}
}
