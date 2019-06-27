package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

public class AccessRule implements ToJSON
{
    public long id;
    public String content;

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", id);
        res.put("content", content);
        return res;
    }
}
