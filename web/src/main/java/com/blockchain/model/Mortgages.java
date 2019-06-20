package com.blockchain.model;

import com.blockchain.utils.MDateCmp;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

public class Mortgages
{
	public int id;
	public int cid;
	public BigDecimal money;
	public Date createTime;
	public int partyA;
	public int partyB;
	public int status;

	public JSONObject toJSON()
	{
		var res = new JSONObject();
		res.put("mid", id);
		res.put("cid", cid);
		res.put("money", money);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("status", status);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		return res;
	}
}
