package com.blockchain.service;

import com.blockchain.dao.UserMapper;
import com.blockchain.model.User;
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

	public int signIn(String email, String psw)
	{
		return userMapper.verifyUser(email, psw);
	}

	public User getUserInfoByID(int userID) throws RuntimeException
	{
		return userMapper.getUserById(userID);
	}

	public User getUserInfoByEmail(String email) throws RuntimeException
	{
		return userMapper.getUserByEmail(email);
	}

	public void updateUserinfo(int id, String name, String profile)
	{
		userMapper.updateUserInfo(id, name, profile);
	}

	public void updatePassword(int id, String psw)
	{
		userMapper.updatePassword(id, psw);
	}
}
