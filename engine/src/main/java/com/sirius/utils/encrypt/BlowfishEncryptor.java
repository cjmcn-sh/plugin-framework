/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : oasp
 * user created : pippo
 * date created : 2010-7-23 - 上午09:58:57
 */
package com.sirius.utils.encrypt;

import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @since 2010-7-23
 * @author pippo
 */
public class BlowfishEncryptor implements Encryptor {

	//	private static Logger logger = LoggerFactory.getLogger(BlowfishEncryptor.class);

	private static String algorithm = "Blowfish/ECB/ZeroBytePadding";

	Provider security_provider = new BouncyCastleProvider();

	@Override
	public byte[] encrypt(Object key, byte[] target) throws EncryptException {
		try {
			Validate.notNull(target, "the encrypt target can not be null");
			Validate.notNull(key, "the encrypt key can not be null");
			//			Validate.isTrue((key.toString().length() % 2) == 0, "the encrypt key size must be even number");
			String _key = ((String) key).length() % 2 == 0 ? (String) key : ((String) key) + "0";

			SecretKeySpec secretKey = new SecretKeySpec(_key.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm, security_provider);
			//		cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(ivBytes));
			//		Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(target);
			return result;
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public String encryptHex(Object key, String data) throws EncryptException {
		byte[] result = this.encrypt(key, data.getBytes());
		return new String(Hex.encodeHex(result));
	}

	@Override
	public byte[] decrypt(Object key, byte[] data) throws EncryptException {
		try {
			Validate.notNull(data, "the encrypt target can not be null");
			Validate.notNull(key, "the encrypt key can not be null");
			//			Validate.isTrue((key.toString().length() % 2) == 0, "the encrypt key size must be even number");
			String _key = ((String) key).length() % 2 == 0 ? (String) key : ((String) key) + "0";

			SecretKeySpec secretKey = new SecretKeySpec(_key.getBytes(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm, security_provider);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] result = cipher.doFinal(data);

			return result;
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public String decryptHex(Object key, String data) throws EncryptException {
		try {
			byte[] result = this.decrypt(key, Hex.decodeHex(data.toCharArray()));
			return new String(result);
		} catch (DecoderException e) {
			throw new EncryptException(e);
		}
	}

	//	public static void main(String[] args) throws Exception {
	//		System.out.println((byte) 0);
	//
	//		BlowfishEncryptor encryptor = new BlowfishEncryptor();
	//
	//		String target = "1234567812345678344dfsdsrerwerwer123";
	//		String key = "12345678";
	//
	//		String encrypt = encryptor.encryptHex(key, target);
	//		System.out.println(encrypt);
	//		System.out.println("083c5c143ed83d51083c5c143ed83d519d1493f3bd7700a962373abf12bf3f402d4cc2bb470b216c".equals(encrypt));
	//		System.out.println(encryptor.decryptHex(key, encrypt));
	//
	//		//		System.out.println(encryptor.decrypt("78dd92637fb4b2d278dd92637fb4b2d2520034769b162b4ed8b016a6c3dd8bed38b788e7a91b9690", key));
	//	}

	@Override
	public String encryptBase64(Object key, String data) throws EncryptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decryptBase64(Object key, String raw) throws EncryptException {
		// TODO Auto-generated method stub
		return null;
	}
}
