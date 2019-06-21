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

	public long insertUser(User user)
	{
		return user.id = userMapper.insertUser(user);
	}

	public boolean isEmailExist(String email)
	{
		return userMapper.getUserByEmail(email) != null;
	}

	public User getUserByID(long userID)
	{
		return userMapper.getUserById(userID);
	}

	public User getUserByEmail(String email)
	{
		return userMapper.getUserByEmail(email);
	}

	public void updateUser(User user)
	{
		userMapper.updateUserInfo(user.id, user.name, user.passwordHash, user.additional);
	}
}
