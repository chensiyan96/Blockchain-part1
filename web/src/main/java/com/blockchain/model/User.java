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
        CoreBusiness,
        Supplier,
        MoneyGiver,
        Admin
    }

	public long id;
	public String email;
	public String name;
	public String passwordHash;
	public Roles role;
	public Profile profile;
	public String additional;

	@Override
	public JSONObject toJSON()
	{
		var user = new JSONObject();
		user.put("email", email);
		user.put("name", name);
		user.put("role", role);
		if (profile != null) {
            user.put("profile", profile.toJSON());
        }
        user.put("additional", additional);
		return user;
	}

	public String hashAndSetPassword(String password)
    {
        try	{
            passwordHash = AESToken.encrypt(password);
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
            if (this.passwordHash.equals(passwordHash)) {
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