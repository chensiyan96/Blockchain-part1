package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Transfer implements ToJSON
{
    public static class DataBase
    {
        public long id;
        public long dst;
        public long src;
    }
    public DataBase db;
    public BigDecimal money;
    public BigDecimal dst_remain;
    public BigDecimal src_remain;

    public Transfer()
    {
        db = new DataBase();
    }

    public static Transfer formJSON(JSONObject json)
    {
        var transfer = new Transfer();
        transfer.db.id = json.getLong("id");
        transfer.db.dst = json.getLong("dst");
        transfer.db.src = json.getLong("src");
        transfer.money = json.getBigDecimal("money");
        transfer.dst_remain = json.has("dst_remain") ? json.getBigDecimal("dst_remain") : null;
        transfer.src_remain = json.has("src_remain") ? json.getBigDecimal("src_remain") : null;
        return transfer;
    }

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", db.id);
        res.put("dst", db.dst);
        res.put("src", db.src);
        res.put("money", money);
        if (dst_remain != null) {
            res.put("dst_remain", dst_remain);
        }
        if (src_remain != null) {
            res.put("src_remain", src_remain);
        }
        return res;
    }
}
