/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : ctu-utils
 * user created : liyu
 * date created : 2012-12-20 - 上午10:59:56
 */
package com.sirius.utils.encrypt;

import java.security.MessageDigest;

import org.apache.commons.lang3.CharEncoding;

/**
 * @since 2012-12-20
 * @author liyu
 */
public class MD5Encryptor implements Encryptor {

	private static final String MD5 = "MD5";

	@Override
	public byte[] encrypt(Object key, byte[] data) throws EncryptException {
		return encryptHex(key, new String(data)).getBytes();
	}

	@Override
	public byte[] decrypt(Object key, byte[] data) throws EncryptException {
		throw new UnsupportedOperationException("md5 not support decode.");
	}

	@Override
	public String encryptHex(Object key, String data) throws EncryptException {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(MD5);

			messageDigest.reset();

			messageDigest.update(data.getBytes(CharEncoding.UTF_8));

		} catch (Exception e) {
			throw new EncryptException(e);
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	@Override
	public String decryptHex(Object key, String data) throws EncryptException {
		throw new UnsupportedOperationException("md5 not support decode.");

	}

	@Override
	public String encryptBase64(Object key, String data) throws EncryptException {
		throw new UnsupportedOperationException("md5 not support base64");
	}

	@Override
	public String decryptBase64(Object key, String raw) throws EncryptException {
		throw new UnsupportedOperationException("md5 not support decode.");
	}

}
