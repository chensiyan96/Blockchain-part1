package com.blockchain.model;

import com.blockchain.utils.MDateCmp;
import org.json.JSONObject;

import java.util.Date;

public class Financing
{

	public int id;
	public String terms;
	public Date createTime;
	public int partyA;
	public int partyB;
	public FinancingStatus status;
	public int mid;
	public int aid;

	public JSONObject toJSON()
	{
		var res = new JSONObject();
		res.put("fid", id);
		res.put("mid", mid);
		res.put("aid", aid);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("status", status);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		res.put("terms", terms);
		return res;
	}
}
