/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : oasp
 * user created : pippo
 * data created : 2010-7-23 - 上午09:42:03
 */
package com.sirius.utils.encrypt;


/**
 * @since 2010-7-23
 * @author pippo
 */
public interface Encryptor {

	byte[] encrypt(Object key, byte[] data) throws EncryptException;

	byte[] decrypt(Object key, byte[] data) throws EncryptException;

	String encryptHex(Object key, String data) throws EncryptException;

	String decryptHex(Object key, String data) throws EncryptException;

	String encryptBase64(Object key, String data) throws EncryptException;

	String decryptBase64(Object key, String raw) throws EncryptException;

}
