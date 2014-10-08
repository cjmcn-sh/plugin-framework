/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : node-server
 * user created : pippo
 * date created : 2011-1-28 - 下午02:27:44
 */
package com.sirius.utils.encrypt;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @since 2011-1-28
 * @author pippo
 */
public final class RSAEncryptor implements Encryptor {

	private static Provider security_provider = new BouncyCastleProvider();

	private static final int KEY_SIZE = 1024;

	private static final String algorithm = "RSA";

	public static KeyPair generateKeyPair() throws EncryptException {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm, security_provider);
			//没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.genKeyPair();
			return keyPair;
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws EncryptException {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(algorithm, security_provider);
			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws EncryptException {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance(algorithm, security_provider);
			RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
				new BigInteger(privateExponent));
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	public static RSAPublicKey generateRSAPublicKeyByBase64(byte[] data) throws EncryptException {
		try {
			X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(data));
			KeyFactory factory = KeyFactory.getInstance(algorithm, security_provider);
			PublicKey publicKey = factory.generatePublic(encodedKeySpec);
			return (RSAPublicKey) publicKey;
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	public static RSAPrivateKey generateRSAPrivateKeyByBase64(byte[] data) throws EncryptException {
		try {
			PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(data));
			KeyFactory factory = KeyFactory.getInstance(algorithm, security_provider);
			PrivateKey privateKey = factory.generatePrivate(encodedKeySpec);
			return (RSAPrivateKey) privateKey;
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public byte[] encrypt(Object key, byte[] data) throws EncryptException {
		try {
			Cipher cipher = Cipher.getInstance(algorithm, security_provider);
			cipher.init(Cipher.ENCRYPT_MODE, (Key) key);

			//获得加密块大小，如:加密前数据为128个byte，而key_size=1024 加密块大小为127 byte,加密后为128个byte;因此共有2个加密块，第一个127 byte第二个为1个byte
			int blockSize = cipher.getBlockSize();
			//获得加密块加密后块大小
			int outputSize = cipher.getOutputSize(data.length);
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize) {
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				} else {
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				}
				i++;
			}

			return raw;
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
	public String encryptBase64(Object key, String data) throws EncryptException {
		byte[] result = encrypt(key, data.getBytes());
		return new String(Base64.encodeBase64(result));
	}

	@Override
	public byte[] decrypt(Object key, byte[] raw) throws EncryptException {
		try {
			Cipher cipher = Cipher.getInstance(algorithm, security_provider);
			cipher.init(Cipher.DECRYPT_MODE, (Key) key);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new EncryptException(e);
		}
	}

	@Override
	public String decryptHex(Object key, String raw) throws EncryptException {
		try {
			byte[] target = Hex.decodeHex(raw.toCharArray());
			byte[] result = decrypt(key, target);
			return new String(result);
		} catch (Exception e) {
			throw new EncryptException(e);
		}
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
