package com.blockchain.model;

import org.json.JSONObject;

public class MoneyGiverProfile extends Profile
{
    public int[] finCounts;

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("finCounts", finCounts);
        return res;
    }
}
