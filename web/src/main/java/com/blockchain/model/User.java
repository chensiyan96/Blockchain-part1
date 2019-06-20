package com.blockchain.model;

import org.json.JSONObject;

public class User implements ToJSON
{
    public enum Roles
    {
        CoreBusiness,
        Supplier,
        Bank,
        Admin
    }

	public int id;
	public String email;
	public String companyName;
	public String passwordHash;
	public Roles role;
	public String profile;

	@Override
	public JSONObject toJSON()
	{
		var user = new JSONObject();
		user.put("email", email);
		user.put("name", companyName);
		user.put("role", role);
		user.put("profile", profile);
		return user;
	}
}