package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import com.blockchain.model.User;
import com.blockchain.dao.UserMapper;
import com.blockchain.utils.AESToken;
import com.blockchain.utils.MStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//todo : 用到了事务，所以异常是必须抛出的
@Service
@Transactional
public class UserService
{

	@Autowired
	private final UserMapper userMapper;

	@Autowired
	private final AccountMapper accountMapper;

	public UserService()
	{
		userMapper = null;
		accountMapper = null;
	}

	public UserService(UserMapper userMapper, AccountMapper accountMapper)
	{
		this.userMapper = userMapper;
		this.accountMapper = accountMapper;
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
	public int register(User user)
	{
		try
		{
			userMapper.insertUser(user);
			accountMapper.insertUserAccount(user.id);
			return user.id;
		} catch (Exception e)
		{
			System.out.println("--------------");
			e.printStackTrace();
			System.out.println("--------------");
		}
		return 0;
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
	public int signIn(String email, String psw)
	{
		try
		{
			return userMapper.verifyUser(MStringUtils.normalize(email), AESToken.encrypt(psw));
		} catch (Exception e)
		{
			System.out.println("--------------");
			e.printStackTrace();

			System.out.println("--------------");
			return 0;
		}
	}

	/**
	 * 更新信息
	 */
	public void updateUserinfo(String name, String profile, int uid)
	{
		try
		{
			userMapper.updateUserInfo(name, profile, uid);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 更新密码
	 */
	public void updatePassword(String psw, int uid)
	{
		try
		{
			userMapper.updatePassword(uid, AESToken.encrypt(psw));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
