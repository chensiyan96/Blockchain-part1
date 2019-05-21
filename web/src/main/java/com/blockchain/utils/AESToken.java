package com.blockchain.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

	public String getToken(JSON userinfo) throws Exception
	{
		try
		{
			var bearer = new JSON();
			bearer.put("KEY", KEY);
			bearer.put("User", userinfo);
			Date now = new Date();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, 7);
			bearer.put("Lifetime", sdf.format(calendar.getTime()));

			return encrypt(bearer.toString());
		} catch (Exception e)
		{
			throw e;
		}
	}

	public JSON verifyToken(String token)
	{
		var res = new JSON();
		try
		{
			var bearer = new JSON(decrypt(token));
			var user = bearer.getJSONObject("User");
			var time = sdf.parse(bearer.getString("Lifetime"));
			var now = new Date();
			if (now.compareTo(time) > 0)
			{
				res.put("status", 0);
				res.put("msg", "token过期");
			} else
			{
				res.put("status", 1);
				res.put("msg", "Success");
				res.put("user", user);
			}

		} catch (Exception e)
		{
			res.put("status", 0);
			res.put("msg", "token错误");
		}
		return res;
	}

}
