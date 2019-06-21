package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.sql.Date;

public class Financing implements ToJSON
{
	public static class DataBase
	{
		public long id;
		public long sid; // 供应商外键
		public long cid; // 核心企业外键
		public long mid; // 资金方外键
		public Date createTime;
		public byte state;
	}

	public DataBase db;
	public User supplier;
	public User coreBusiness;
	public User moneyGiver;

	public Financing()
	{
		db = new DataBase();
	}

	public Financing(DataBase db)
	{
		this.db = db;
	}

	@Override
	public JSONObject toJSON()
	{
		var res = new JSONObject();
		return res;
	}

	public void setCoreBusiness(User src)
	{
		assert src.db.role == User.Roles.CoreBusiness;
		supplier = src;
		db.cid = src.db.id;
	}

	public void setSupplier(User src)
	{
		assert src.db.role == User.Roles.Supplier;
		coreBusiness = src;
		db.sid = src.db.id;
	}

	public void setMoneyGiver(User src)
	{
		assert src.db.role == User.Roles.MoneyGiver;
		moneyGiver = src;
		db.mid = src.db.id;
	}
}
