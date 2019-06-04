package com.blockchain.model;

import com.blockchain.utils.JSON;
import com.blockchain.utils.MDateCmp;
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

	public JSON toJSON()
	{
		JSON res = new JSON();
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
