package com.blockchain.model;

import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import java.sql.Timestamp;

public class Contract implements ToJSON
{
    public static class DataBase
    {
        public long id;
        public long sid; // 供应商外键
        public long cid; // 核心企业外键
        public Timestamp createTime;
        public String content;
    }

    public DataBase db;
    public User supplier;
    public User coreBusiness;

    public Contract()
    {
        db = new DataBase();
    }

    public Contract(DataBase db)
    {
        this.db = db;
    }

    @Override
    public JSONObject toJSON()
    {
        var res = new JSONObject();
        res.put("id", db.id);
        res.put("createTime", db.createTime);
        res.put("content", db.content);
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
