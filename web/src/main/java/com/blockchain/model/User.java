package com.blockchain.model;

import com.blockchain.utils.AESToken;
import com.blockchain.utils.ToJSON;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class User implements ToJSON
{
    public enum Roles
    {
        Supplier,
        CoreBusiness,
        MoneyGiver,
        Admin
    }

    public static class DataBase
    {
        public long id;
        public String email;
        public String name;
        public String passwordHash;
        public Roles role;
        public String additional;
        public byte frozen;
        public byte autoPass;
    }

	public DataBase db;
	public Profile profile;

	public User()
    {
        db = new DataBase();
    }

    public User(DataBase db)
    {
        this.db = db;
    }

	@Override
	public JSONObject toJSON()
	{
		var res = new JSONObject();
        res.put("id", db.id);
		res.put("email", db.email);
		res.put("name", db.name);
		res.put("role", db.role);
        res.put("additional", db.additional);
        res.put("frozen", db.frozen);
        res.put("autoPass", db.autoPass);
        if (profile != null) {
            res.put("profile", profile.toJSON());
        }
		return res;
	}

	public String hashAndSetPassword(String password)
    {
        try	{
            db.passwordHash = AESToken.encrypt(password);
        } catch (InvalidKeyException e) {
            return e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            return e.getMessage();
        } catch (IllegalBlockSizeException e) {
            return e.getMessage();
        } catch (BadPaddingException e) {
            return e.getMessage();
        }
        return null;
    }

    public String checkPassword(String password)
    {
        try	{
            var passwordHash = AESToken.encrypt(password);
            if (this.db.passwordHash.equals(passwordHash)) {
                return null;
            }
        } catch (InvalidKeyException e) {
            return e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            return e.getMessage();
        } catch (IllegalBlockSizeException e) {
            return e.getMessage();
        } catch (BadPaddingException e) {
            return e.getMessage();
        }
        return "密码错误";
    }
}