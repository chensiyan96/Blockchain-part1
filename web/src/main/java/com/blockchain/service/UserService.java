package com.blockchain.service;

import com.blockchain.dao.UserMapper;
import com.blockchain.model.User;
import com.blockchain.utils.AESToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService
{

	@Autowired
	private UserMapper userMapper;

	public int register(User user)
	{
		return userMapper.insertUser(user);
	}

	public boolean isEmailExist(String email)
	{
		return userMapper.isEmailExist(email) > 0;
	}

	/**
	 * 获取用户的信息
	 */
	public User getUserInfoByID(int userID) throws RuntimeException
	{
		User user = userMapper.getUserInfo(userID);
		if (user == null)
		{
			throw new RuntimeException("用户信息不存在");
		}
		return user;
	}

	public User getUserInfoByEmail(String email) throws RuntimeException
	{
		User user = userMapper.getUserInfoByEmail(email);
		if (user == null)
		{
			throw new RuntimeException("用户信息不存在");
		}
		return user;
	}

	/**
	 * 登录验证
	 */
	public int signIn(String email, String psw) throws Exception
	{
		try
		{
			return userMapper.verifyUser(email, AESToken.encrypt(psw));
		} catch (Exception e)
		{
			throw new Exception("验证密码错误");
		}

	}

	/**
	 * 更新信息
	 */
	public void updateUserinfo(String name, String profile, int uid) throws Exception
	{
		try
		{
			userMapper.updateUserInfo(name, profile, uid);
		} catch (Exception e)
		{
			throw new Exception("用户信息错误");
		}
	}

	/**
	 * 更新密码
	 */
	public void updatePassword(String psw, int uid) throws Exception
	{
		try
		{
			userMapper.updatePassword(uid, AESToken.encrypt(psw));
		} catch (Exception e)
		{
			throw new Exception("用户信息错误");
		}
	}
}
