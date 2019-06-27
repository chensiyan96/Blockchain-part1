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

	public boolean insertUser(User user)
	{
		return userMapper.insertUser(user.db);
	}

	public User getUserByID(long id)
	{
		var db = userMapper.getUserById(id);
		return db == null ? null : new User(db);
	}

	public User getUserByEmail(String email)
	{
		var db = userMapper.getUserByEmail(email);
		return db == null ? null : new User(db);
	}

	public void updateUser(User user)
	{
		userMapper.updateUserInfo(user.db.id, user.db.name, user.db.passwordHash, user.db.additional, user.db.frozen, user.db.autoPass);
	}

	public User[] getAllSuppliers()
	{
		return constructUserArray(userMapper.getAllSuppliers());
	}

	public User[] getAllCoreBusinesses()
	{
		return constructUserArray(userMapper.getAllCoreBusinesses());
	}

	public User[] getAllMoneyGivers()
	{
		return constructUserArray(userMapper.getAllMoneyGivers());
	}

	private static User[] constructUserArray(User.DataBase[] dbs)
	{
		var users = new User[dbs.length];
		for (int i = 0; i < dbs.length; i++) {
			users[i] = new User(dbs[i]);
		}
		return users;
	}
}
