package com.blockchain.utils;

public class MStringUtils
{
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

	public  static boolean confirmEmail(String str)
	{
		if(str == null || str.isEmpty())
		{
			return false;
		}
		return true;
	}

}
