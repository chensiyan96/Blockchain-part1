package com.blockchain.model;

import com.blockchain.utils.MDateCmp;
import org.json.JSONObject;

import java.util.Date;

public class Message
{

	public int id;
	public String msg;
	public Date createTime;
	public int partyA;
	public int partyB;
	public int status;

	public JSONObject toJson()
	{
		var res = new JSONObject();
		res.put("id", id);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		res.put("status", status);
		res.put("msg", msg);
		return res;
	}

}
