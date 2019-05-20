package com.blockchain.service;

import com.blockchain.model.User;
import com.blockchain.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
{
	@Autowired
	private final UserMapper userMapper;

	//不设置默认构造函数会报错。使用Spring报错：No default constructor found
	//??? smjb错误
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

	/**
	 * 用户注册
	 */
	public int register(User user)
	{
		try
		{
			userMapper.insertUser(user);
			return user.id;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
}
