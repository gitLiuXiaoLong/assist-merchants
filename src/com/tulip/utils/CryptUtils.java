package com.tulip.utils;

import java.security.Key;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加密
 * 
 */
public class CryptUtils {
	
	//算法名
	public static final String SHA = "SHA";
	public static final String MD5 = "MD5";
	public static final String MAC = "HmacMD5";
	public static final String ALGORITHM = "DES";
	public static final String AES = "AES";


	/**
	 * 用户uuid加密算法
	 * @param key：密钥
	 * @param uuid：用户uuid
	 * @param invalidTime：有效时间（分），
	 * @return 密文
	 * @throws Exception
	 */
	public static String encryptUuidSecond(String key, String uuid, int invalidTime) throws Exception {
		// 获取时间戳
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, invalidTime);
		String time = Long.toHexString(cal.getTime().getTime()/1000).toLowerCase();
		return encryptUuid(key,uuid, 0, time, false);
	}

	/**
	 * 用户uuid加密算法
	 * @param key：密钥
	 * @param uuid：用户uuid
	 * @param exNum：扩展编号（0～25，0：不实用扩展编号，1～25使用扩展编号）
	 * @param invalidTime：有效时间（分），
	 * @param free：是否为超级码
	 * @return 密文
	 * @throws Exception
	 */
	public static String encryptUuid(String key, String uuid, int exNum,int invalidTime, boolean free) throws Exception {
		// 获取时间戳
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, invalidTime);
		String time = Long.toHexString(cal.getTime().getTime()/1000).toLowerCase();
		return encryptUuid(key,uuid, exNum, time, free);
	}
	
	/**
	 * 用户uuid加密算法
	 * @param key：密钥
	 * @param uuid：用户uuid
	 * @param exNum：扩展编号（0～25，0：不实用扩展编号，1～25使用扩展编号）
	 * @param invalidTime：有效时间（分），
	 * @param free：是否为超级码
	 * @return 密文
	 * @throws Exception
	 */
	public static String encryptUuid(String key, String uuid, int exNum, String invalidTime, boolean free) throws Exception {
		/*
		 * 加密内容生成
		 * 开始位（1）：21，22位（时间戳后两位）异或
		 * uuid（16）：32位用户uuid转换位16位byte数组
		 * 中间位（1）：ff
		 * 时间位（4）：根据时间戳获得8位16进制字符串，再将字符串转为4位byte数组
		 * 中间位（1）：ff
		 * 补位（9）：00
		 */
		// 待加密内容
		byte[] contentBytes = new byte[32];

		byte[] timeBytes = StringUtils.hexStringToBytes(invalidTime);
		// 开始位（1）：21，22位（时间戳后两位）异或
		byte[] startbyte = new byte[1]; 
		startbyte[0] = StringUtils.getByteXor(timeBytes[2], timeBytes[3]);
		// 拼接uuid（16）：32位用户uuid转换位16位byte数组
		// dec3e402e4f6c446c79ca5a720b46294caff5dd3f729ff000000000000000000
		contentBytes = StringUtils.byteMerger(startbyte, StringUtils.hexStringToBytes(uuid.toLowerCase()));
		// 拼接中间位（1）：ff
		contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("ff"));
		// 拼接时间位（4）：根据时间戳获得8位16进制字符串，再将字符串转为4位byte数组
		contentBytes = StringUtils.byteMerger(contentBytes, timeBytes);
		// 拼接中间位（1）：ff
		contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("ff"));
		if (free) {
			// 兼容控制器1.3.1以前版本控制位
			contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("ff"));
			// 控制器1.3.2以后版本控制位
			contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("01"));
		} else {
			// 扩展门编号（1）：00 不使用扩展编号，01~19 使用扩展编号控制（1~25）
			contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes(StringUtils.leftPad(Integer.toHexString(exNum), 2, '0')));
			// 控制器1.3.2以后版本控制位
			contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("00"));
		}
		// 拼接补位（7）：00
		contentBytes = StringUtils.byteMerger(contentBytes, StringUtils.hexStringToBytes("00000000000000"));
		//System.out.println(StringUtils.bytesTohexString(contentBytes));
		return encryptAES(key, contentBytes);
	}
	
	/**
	 * 检验明文有效期
	 * @param content
	 * @return
	 */
	public static boolean checkEffectiveTime(String content) {
		Calendar cal = Calendar.getInstance();
		long now = cal.getTime().getTime()/1000;
		String timeString = content.substring(36, 44);
		long time = Long.parseLong(timeString, 16);
		return time > now;
	}

	/**
	 * 用户uuid解密算法
	 * @param key：密钥
	 * @param content：密文
	 * @return 明文
	 * @throws Exception
	 */
	public static String decryptUuid(String key, String content) throws Exception {
		String empty = CryptUtils.encryptAES(key, StringUtils.hexStringToBytes(""));
		//System.out.println(empty);
		return decryptAES(key, content + empty);
	}

	/**
	 * 解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptDES(byte[] data, String key) throws Exception  	{
		
		Key k = toKey(decryptBASE64(key));

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);

		return cipher.doFinal(data);
	}
	
	/**
	 * 加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptDES(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);

		return cipher.doFinal(data);
	}
	
	/**
	 * 解码BASE64到字符串
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBASE64(String key) throws Exception {
		return Base64.getDecoder().decode(key);
	}
	
	/**
	 * 转换密钥<br>
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey(byte[] key) throws Exception {
		
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);

		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

		return secretKey;
	}

	/**
	 * BAES64编码到字符串
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBASE64(byte[] key) throws Exception {
		return Base64.getEncoder().encodeToString(key);
	}
	
	/**
	 * 加密MD5
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptMD5(String value) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");     
		byte []bytes=md5.digest(value.getBytes("utf8"));
		StringBuilder ret=new StringBuilder(bytes.length<<1);
		for(int i=0;i<bytes.length;i++){
		  ret.append(Character.forDigit((bytes[i]>>4)&0xf,16));
		  ret.append(Character.forDigit(bytes[i]&0xf,16));
		}
		return ret.toString();
	}

	/**
	 * 用户uuid加密算法
	 * @param key：密钥
	 * @param conent：明文
	 * @return 密文
	 * @throws Exception
	 */
	public static String encryptAES(String key, byte[] conent) throws Exception {
		// 密钥生成
//		KeyGenerator kgen = KeyGenerator.getInstance(AES);
//		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//		random.setSeed(key.getBytes("UTF-8"));
//		kgen.init(128, random);
//		SecretKey secretKey = kgen.generateKey();
//		byte[] enCodeFormat = secretKey.getEncoded();
//		SecretKeySpec keyAes = new SecretKeySpec(enCodeFormat, AES);
		byte keyBytes[] = key.getBytes();
		SecretKeySpec keyAes = new SecretKeySpec(keyBytes, AES);
		// 创建密码器
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.ENCRYPT_MODE, keyAes);
		byte[] encrypted = cipher.doFinal(conent);
		int length = encrypted.length > 32 ? 32 : encrypted.length;
		byte[] result = new byte[length];
		System.arraycopy(encrypted, 0, result, 0, length);
		//System.out.println(StringUtils.bytesTohexString(encrypted));
		return StringUtils.bytesTohexString(result);
	}
	
	/**
	 * AES解密（未完成，如要使用还需调试）
	 * @param key
	 * @param conent：16进制密文字符串
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES(String key, String conent) throws Exception {
		// 密钥生成
//		KeyGenerator kgen = KeyGenerator.getInstance(AES);
//		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
//		random.setSeed(key.getBytes("UTF-8"));
//		kgen.init(128, random);
//		SecretKey secretKey = kgen.generateKey();
//		byte[] enCodeFormat = secretKey.getEncoded();
//		SecretKeySpec keyAes = new SecretKeySpec(enCodeFormat, AES);
		byte keyBytes[] = key.getBytes();
		SecretKeySpec keyAes = new SecretKeySpec(keyBytes, AES);
		// 创建密码器
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(Cipher.DECRYPT_MODE, keyAes);
		byte[] encrypted = cipher.doFinal(StringUtils.hexStringToBytes(conent));
		return StringUtils.bytesTohexString(encrypted);
	}

	
	/**
	 * 哈希SHA1
	 */
	public static String encodeSHA1(String value) throws Exception {
		String signature = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(value.getBytes("UTF-8"));
			signature = StringUtils.byteToHex(crypt.digest());
			return signature;
		}catch (Exception e){
			return null;
		}
	}
	
	/**
	 * 签名
	 */
	public static String getSign(Map<String, String> param, String secret) throws Exception {
		List<String> signList = new ArrayList<String>();
		for (String key: param.keySet()) {
			signList.add(param.get(key));
		}
		signList.add(secret);
		signList.sort((item1, item2) -> item1.compareTo(item2));
		return encodeSHA1(String.join("", signList));
	}

}