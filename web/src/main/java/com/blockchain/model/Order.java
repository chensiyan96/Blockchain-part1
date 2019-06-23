package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order implements ToJSON
{
    public static class DataBase
    {
        public long id;
        public long sid; // 供应商外键
        public long cid; // 核心企业外键
        public BigDecimal money;
        public Timestamp createTime;
        public Timestamp endTime;
        public byte status;
    }

    public Order.DataBase db;
    public User supplier;
    public User coreBusiness;

    public Order()
    {
        db = new Order.DataBase();
    }

    public Order(Order.DataBase db)
    {
        this.db = db;
    }

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", db.id);
        res.put("money", db.money);
        res.put("createTime", db.createTime);
        res.put("endTime", db.endTime);
        res.put("status", db.status);
        if (supplier != null) {
            res.put("Supplier", supplier.toJSON());
        }
        if (coreBusiness != null) {
            res.put("CoreBusiness", coreBusiness.toJSON());
        }
        return res;
    }

    public void setCoreBusiness(User src)
    {
        assert src.db.role == User.Roles.CoreBusiness;
        supplier = src;
        db.cid = src.db.id;
    }

    public void setSupplier(User src)
    {
        assert src.db.role == User.Roles.Supplier;
        coreBusiness = src;
        db.sid = src.db.id;
    }
}
