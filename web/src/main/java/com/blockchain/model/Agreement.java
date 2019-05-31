package com.blockchain.model;

import com.blockchain.utils.JSON;
import com.blockchain.utils.MDateCmp;
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

	public JSON toJSON()
	{
		JSON res = new JSON();
		res.put("aid", id);
		res.put("cid", creditId);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("status", status);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		return res;
	}
}
