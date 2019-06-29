package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CreditRecord implements ToJSON
{
    public long sid;
    public BigDecimal applied;
    public BigDecimal approved;
    public Timestamp createTime;

    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("applied", applied);
        res.put("approved", approved);
        res.put("createTime", createTime);
        return res;
    }
}
