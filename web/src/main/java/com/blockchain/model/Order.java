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
        public long number; // 前端用的，后端没什么用
        public BigDecimal money;
        public Timestamp createTime;
        public int days;
        public byte status;
    }

    public DataBase db;
    public User supplier;
    public User coreBusiness;

    public Order()
    {
        db = new DataBase();
    }

    public Order(DataBase db)
    {
        this.db = db;
    }

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", db.id);
        res.put("number", db.number);
        res.put("money", db.money);
        res.put("createTime", db.createTime);
        res.put("days", db.days);
        res.put("endTime", new Timestamp(db.createTime.getTime() + db.days * (24 * 60 * 60 * 1000)));
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
        coreBusiness = src;
        db.cid = src.db.id;
    }

    public void setSupplier(User src)
    {
        assert src.db.role == User.Roles.Supplier;
        supplier = src;
        db.sid = src.db.id;
    }
}
