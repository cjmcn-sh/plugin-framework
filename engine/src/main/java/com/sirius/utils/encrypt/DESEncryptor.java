/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/*
 * Copyright 2011 sdo.com, Inc. All rights reserved.
 * sdo.com PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sirius.utils.encrypt;

import java.security.Key;
import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * DES Encryptor
 * 
 * @author <a href="mailto:liangwenzheng@snda.com">liangwenzheng</a>
 */
public class DESEncryptor implements Encryptor {

	private static Provider security_provider = new BouncyCastleProvider();

	private static final String ALGORITHM = "DES";

	@Override
	public byte[] encrypt(Object key, byte[] data) throws EncryptException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, security_provider);
			cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public byte[] decrypt(Object key, byte[] data) throws EncryptException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM, security_provider);
			cipher.init(Cipher.DECRYPT_MODE, getKey(key));
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public String encryptHex(Object key, String data) throws EncryptException {
		byte[] result = encrypt(key, data.getBytes());
		return new String(Hex.encodeHex(result));
	}

	@Override
	public String decryptHex(Object key, String data) throws EncryptException {
		try {
			byte[] target = Hex.decodeHex(data.toCharArray());
			byte[] result = decrypt(key, target);
			return new String(result);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	private Key getKey(Object key) throws Exception {
		if (key instanceof String) {
			DESKeySpec dks = new DESKeySpec(((String) key).getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM, security_provider);
			return skf.generateSecret(dks);
		} else {
			throw new EncryptException("Invilid key.");
		}
	}

	@Override
	public String encryptBase64(Object key, String data) throws EncryptException {
		byte[] result = encrypt(key, data.getBytes());
		return new String(Base64.encodeBase64(result));
	}

	@Override
	public String decryptBase64(Object key, String raw) throws EncryptException {
		try {
			byte[] target = Base64.decodeBase64(raw.getBytes());
			byte[] result = decrypt(key, target);
			return new String(result);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}
}
