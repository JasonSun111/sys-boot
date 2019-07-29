package com.sunys.core.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.DigestUtils;

/**
 * 对称加密、解密
 * SymmetricEncoder
 * @author sunys
 * @date 2019年1月7日
 */
public class SymmetricEncoder {

	//默认加密、解密的密钥
	private static final String key = "s65h4rsths56re4jd6rty";
	
	//签名算法
	private static final String SIGN_ALGORITHMS = "SHA1PRNG";
	
	/**
	 * 加密过程：
	 * 1.构造密钥生成器
	 * 2.根据ecnodeRules规则初始化密钥生成器
	 * 3.产生密钥
	 * 4.创建和初始化密码器
	 * 5.内容加密
	 * 6.返回字符串
	 * @param encodeRules
	 * @param content
	 * @return
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String AESEncode(String encodeRules, String content) throws GeneralSecurityException, UnsupportedEncodingException {
		if(encodeRules==null){
			encodeRules = key;
		}
		// 1.构造密钥生成器，指定为AES算法,不区分大小写
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		// 2.根据ecnodeRules规则初始化密钥生成器
		// 生成一个128位的随机源,根据传入的字节数组
		SecureRandom secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
		secureRandom.setSeed(encodeRules.getBytes("utf-8"));
		keygen.init(128, secureRandom);
		// 3.产生原始对称密钥
		SecretKey originalKey = keygen.generateKey();
		// 4.获得原始对称密钥的字节数组
		byte[] raw = originalKey.getEncoded();
		// 5.根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, "AES");
		// 6.根据指定算法AES自成密码器
		Cipher cipher = Cipher.getInstance("AES");
		// 7.初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密解密(DECRYPT_MODE)操作，第二个参数为使用的KEY
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
		byte[] byteEncode = content.getBytes("utf-8");
		// 9.根据密码器的初始化方式--加密：将数据加密
		byte[] byteAES = cipher.doFinal(byteEncode);
		// 10.将加密后的数据转换为字符串
		String AESEncode = Base64.getEncoder().encodeToString(byteAES);
		// 11.将字符串返回
		return AESEncode;
	}

	/**
	 * 解密过程：
	 * 1.同加密1-4步
	 * 2.将加密后的字符串反纺成byte[]数组
	 * 3.将加密内容解密
	 * @param encodeRules
	 * @param content
	 * @return
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String AESDncode(String encodeRules, String content) throws GeneralSecurityException, UnsupportedEncodingException {
		if(encodeRules==null){
			encodeRules = key;
		}
		// 1.构造密钥生成器，指定为AES算法,不区分大小写
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		// 2.根据ecnodeRules规则初始化密钥生成器
		// 生成一个128位的随机源,根据传入的字节数组
		SecureRandom secureRandom = SecureRandom.getInstance(SIGN_ALGORITHMS);
		secureRandom.setSeed(encodeRules.getBytes("utf-8"));
		keygen.init(128, secureRandom);
		// 3.产生原始对称密钥
		SecretKey originalKey = keygen.generateKey();
		// 4.获得原始对称密钥的字节数组
		byte[] raw = originalKey.getEncoded();
		// 5.根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, "AES");
		// 6.根据指定算法AES自成密码器
		Cipher cipher = Cipher.getInstance("AES");
		// 7.初始化密码器，第一个参数为加密(ENCRYPT_MODE)或者解密(DECRYPT_MODE)操作，第二个参数为使用的KEY
		cipher.init(Cipher.DECRYPT_MODE, key);
		// 8.将加密并编码后的内容解码成字节数组
		byte[] byteContent = Base64.getDecoder().decode(content);
		// 9.解密
		byte[] byteDecode = cipher.doFinal(byteContent);
		String AESDecode = new String(byteDecode, "utf-8");
		return AESDecode;
	}

	/**
	 * md5加密
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		String md5code = DigestUtils.md5DigestAsHex(text.getBytes());
        return md5code;
    }

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		/*
		 * 加密
		 */
		System.out.println("使用AES对称加密，请输入加密的规则");
		String encodeRules = scanner.next();
		System.out.println("请输入要加密的内容:");
		String content = scanner.next();
		System.out.println("根据输入的规则" + encodeRules + "加密后的密文是:" + SymmetricEncoder.AESEncode(encodeRules, content));

		/*
		 * 解密
		 */
		System.out.println("使用AES对称解密，请输入加密的规则：(须与加密相同)");
		encodeRules = scanner.next();
		System.out.println("请输入要解密的内容（密文）:");
		content = scanner.next();
		System.out.println("根据输入的规则" + encodeRules + "解密后的明文是:" + SymmetricEncoder.AESDncode(encodeRules, content));
		scanner.close();
	}
}
