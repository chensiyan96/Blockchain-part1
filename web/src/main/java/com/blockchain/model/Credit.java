package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Credit implements ToJSON
{
	public static class DataBase
	{
		public long id;
		public String rank;
		public BigDecimal applied;
		public BigDecimal approved;
	}
	public DataBase db;

	public Credit()
	{
		db = new DataBase();
	}

	public Credit(DataBase db)
	{
		this.db = db;
	}

	public JSONObject toJSON()
	{
		var res = new JSONObject();
		res.put("id", db.id);
		res.put("rank", db.rank);
		res.put("applied", db.applied);
		res.put("approved", db.approved);
		return res;
	}
}
