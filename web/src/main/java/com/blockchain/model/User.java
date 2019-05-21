package com.blockchain.model;

import com.blockchain.utils.JSON;

//todo: 完善用户信息
public class User
{

	public int id;

	public String password;
	public String name;
	public String email;
	public String phone;
	public String avatar;

	public JSON toJSON()
	{
		JSON user = new JSON();
		user.put("email", email);
		user.put("name", name);
		user.put("phone", phone);
		return user;
	}
}