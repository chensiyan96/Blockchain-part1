package com.blockchain.model;

import com.blockchain.model.Interface.ToJSON;
import com.blockchain.utils.JSON;

public class User implements ToJSON
{

	public int id;

	public String passwordHash;
	public String companyName;
	public String email;
	public String normalizedEmail;
	public String profile;
	public Roles role;

	public JSON toJSON()
	{
		JSON user = new JSON();
		user.put("email", normalizedEmail);
		user.put("name", companyName);
		//user.put("profile", profile);
		user.put("role", role);
		return user;
	}
}