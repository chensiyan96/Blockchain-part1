package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Product implements ToJSON
{
    public static class DataBase
    {
        public long id;
        public String name;
        public int days;
        public BigDecimal rate;
        public String additional;
    }

    public DataBase db;

    public Product()
    {
        db = new DataBase();
    }

    public Product(DataBase db)
    {
        this.db = db;
    }

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", db.id);
        res.put("name", db.name);
        res.put("days", db.days);
        res.put("rate", db.rate);
        res.put("additional", db.additional);
        return res;
    }
}
