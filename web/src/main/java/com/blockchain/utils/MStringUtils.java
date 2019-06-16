package com.blockchain.utils;

public class MStringUtils
{
	public static String normalize(String str)
	{
		return str.toUpperCase();
	}

	public static boolean confirmPsw(String str)throws Exception
	{
		if(str == null || str.isEmpty())
		{
			throw new Exception("密码不能为空");
		}
		if(str.length() < 6)
		{
			throw new Exception("密码长度不能小于6");
		}
		return true;
	}

	public  static boolean confirmEmail(String str)throws Exception
	{
		if(str == null || str.isEmpty())
		{
			throw new Exception("邮箱不能为空");
		}
		return true;
	}

}
