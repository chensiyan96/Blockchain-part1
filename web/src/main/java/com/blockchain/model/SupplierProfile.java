package com.blockchain.model;

import org.json.JSONObject;

import java.math.BigDecimal;

public class SupplierProfile extends Profile
{
    public String rank;
    public BigDecimal applied;
    public BigDecimal approved;
    public int orderCount;
    public int[] finCounts;

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("rank", rank);
        res.put("applied", applied);
        res.put("approved", approved);
        res.put("orderCount", orderCount);
        res.put("finCounts", finCounts);
        return res;
    }
}
