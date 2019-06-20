package com.blockchain.model;

import com.blockchain.utils.MDateCmp;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

public class Credit implements ToJSON
{
	public int id;
	public BigDecimal money;
	public Date createTime;
	public Date deadline;

	//A掏钱给B
	public int partyA;
	public int partyB;

	//打借条的时候时0，还了是1，确认以及还了是2,超期未还是3
	public int status;

	public JSONObject toJSON()
	{
		var res = new JSONObject();
		res.put("cid", id);
		res.put("money", money);
		res.put("partyA", partyA);
		res.put("partyB", partyB);
		res.put("status", status);
		res.put("createTime", MDateCmp.timeFormat(createTime));
		res.put("deadline", MDateCmp.timeFormat(deadline));
		return res;
	}
}
