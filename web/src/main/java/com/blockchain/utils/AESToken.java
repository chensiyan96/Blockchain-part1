package com.blockchain.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import java.util.Date;

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


	public static String getToken(JSONObject userinfo) throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeyException
	{
		var bearer = new JSONObject();
		bearer.put("KEY", KEY);
		bearer.put("User", userinfo);
		Date now = new Date();

		bearer.put("Lifetime", MDateCmp.timeFormat(MDateCmp.timeAdd(7, now)));

		return encrypt(bearer.toString());
	}

	public static JSONObject verifyToken(String token) throws Exception
	{
		var bearer = new JSONObject(decrypt(token));
		var user = bearer.getJSONObject("User");
		var time = MDateCmp.parse(bearer.getString("Lifetime"));
		var now = new Date();
		if (now.compareTo(time) > 0)
		{
			throw new Exception("token过期");
		}
		return new JSONObject(user.toString());
	}

}
