package com.blockchain.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AESToken
{

	private static final String SECRET_KEY_1 = "751f621ea5c8f930";
	private static final String SECRET_KEY_2 = "2624750004598718";

	private IvParameterSpec ivParameterSpec;
	private SecretKeySpec secretKeySpec;
	private Cipher cipher;

	static
	{
		Security.addProvider(new BouncyCastleProvider());
	}

	public AESToken()
	{
		ivParameterSpec = new IvParameterSpec(SECRET_KEY_2.getBytes());
		secretKeySpec = new SecretKeySpec(SECRET_KEY_1.getBytes(), "AES");
		try
		{
			cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING", "BC");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public String encrypt(String toBeEncrypt)
			throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
		return Base64.encodeBase64String(encrypted);
	}


	public String decrypt(String encrypted)
			throws InvalidAlgorithmParameterException, InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException
	{
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
		return new String(decryptedBytes);
	}

}
