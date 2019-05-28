package com.blockchain.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import java.util.Date;
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
	private static final String KEY = "wdnmdrg";


	private static IvParameterSpec ivParameterSpec;
	private static SecretKeySpec secretKeySpec;
	private static Cipher cipher;

	static
	{
		Security.addProvider(new BouncyCastleProvider());
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

	public AESToken()
	{
	}


	public static String encrypt(String toBeEncrypt)
			throws InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] encrypted = cipher.doFinal(toBeEncrypt.getBytes());
		return Base64.encodeBase64String(encrypted);
	}


	public static String decrypt(String encrypted)
			throws InvalidAlgorithmParameterException, InvalidKeyException,
			BadPaddingException, IllegalBlockSizeException
	{
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encrypted));
		return new String(decryptedBytes);
	}

	public static String getToken(JSON userinfo) throws Exception
	{
		try
		{
			var bearer = new JSON();
			bearer.put("KEY", KEY);
			bearer.put("User", userinfo);
			Date now = new Date();

			bearer.put("Lifetime", MDateCmp.timeFormat(MDateCmp.timeAdd(7, now)));

			return encrypt(bearer.toString());
		} catch (Exception e)
		{
			throw e;
		}
	}

	public static JSON verifyToken(String token)
	{
		var res = new JSON();
		try
		{
			var bearer = new JSON(decrypt(token));
			var user = bearer.getJSONObject("User");
			var time = MDateCmp.parse(bearer.getString("Lifetime"));
			var now = new Date();
			if (now.compareTo(time) > 0)
			{
				throw new Exception("token过期");
			} else
			{
				res.put("status", 1);
				res.put("msg", "Success");
				res.put("user", user);
			}

		} catch (Exception e)
		{
			res.put("status", 0);
			res.put("msg", e.getMessage());
		}
		return res;
	}

}
