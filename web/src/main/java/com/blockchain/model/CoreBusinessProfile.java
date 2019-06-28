package com.blockchain.model;

import org.json.JSONObject;

public class CoreBusinessProfile extends Profile
{
    public int orderCount;
    public int[] finCounts;

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("orderCount", orderCount);
        res.put("finCounts", finCounts);
        return res;
    }
}
