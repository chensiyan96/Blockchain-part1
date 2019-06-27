package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Financing implements ToJSON
{
	public static class DataBase
	{
		public long id;
		public long sid; // 供应商外键
		public long cid; // 核心企业外键
		public long mid; // 资金方外键
		public BigDecimal money;
		public Timestamp createTime;
		public Timestamp payTime;
		public Timestamp repayTime;
		public int days;
		public BigDecimal rate;
		public byte status;
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
		res.put("id", db.id);
		res.put("money", db.money);
		res.put("createTime", db.createTime);
		res.put("payTime", db.payTime);
		res.put("repayTime", db.repayTime);
		res.put("days", db.days);
		res.put("rate", db.rate);
		res.put("status", db.status);
		if (supplier != null) {
			res.put("Supplier", supplier.toJSON());
		}
		if (coreBusiness != null) {
			res.put("CoreBusiness", coreBusiness.toJSON());
		}
		if (moneyGiver != null) {
			res.put("MoneyGiver", moneyGiver.toJSON());
		}
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
