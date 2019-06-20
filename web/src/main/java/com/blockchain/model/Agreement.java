package com.blockchain.model;

import com.blockchain.utils.MDateCmp;
import org.json.JSONObject;

import java.util.Date;

public class Agreement
{
	public int id;
	public String terms;
	public Date createTime;

	//A发起合同
	public int partyA;
	public int partyB;

	public AgreeStatus status;

	public int creditId;

	public JSONObject toJSON()
	{
		var res = new JSONObject();
		res.put("aid", id);
		res.put("cid", creditId);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("status", status);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		return res;
	}
}
