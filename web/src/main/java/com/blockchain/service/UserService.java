package com.blockchain.service;

import com.blockchain.model.User;
import com.blockchain.dao.UserMapper;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.MStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService
{

	@Autowired
	private final UserMapper userMapper;

	public UserService()
	{
		userMapper = null;
	}

	public UserService(UserMapper userMapper)
	{
		this.userMapper = userMapper;
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
		User user = userMapper.getUserInfoByEmail(MStringUtils.normalize(email));
		if (user == null)
		{
			throw new RuntimeException("用户信息不存在");
		}
		return user;
	}

	/**
	 * 用户注册
	 */
	public int register(User user) throws Exception
	{
		int id = userMapper.insertUser(user);
		if (id == 0)
		{
			throw new Exception("用户信息错误");
		}
		return id;
	}

	/**
	 * 判断邮箱是否已经注册
	 */
	public boolean isEmailExist(String email)
	{

		return userMapper.isEmailExist(MStringUtils.normalize(email)) > 0;
	}

	/**
	 * 登录验证
	 */
	public int signIn(String email, String psw) throws Exception
	{
		try
		{
			return userMapper.verifyUser(MStringUtils.normalize(email), AESToken.encrypt(psw));
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
