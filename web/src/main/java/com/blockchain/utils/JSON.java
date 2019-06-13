package com.blockchain.utils;

import com.blockchain.model.Interface.ToJSON;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class JSON extends JSONObject
{

	public JSON()
	{
		super();
	}

	public JSON(String value)
	{
		super(value);
	}

	public Integer getIntegerObject(String key)
	{
		try
		{
			return super.getInt(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Double getDoubleObject(String key)
	{
		try
		{
			return super.getDouble(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Float getFloatObject(String key)
	{
		try
		{
			return super.getFloat(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Boolean getBooleanObject(String key)
	{
		try
		{
			return super.getBoolean(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	public Long getLongObject(String key)
	{
		try
		{
			return super.getLong(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public String getString(String key)
	{
		try
		{
			return super.getString(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public JSONArray getJSONArray(String key)
	{
		try
		{
			return super.getJSONArray(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	@Override
	public JSONObject getJSONObject(String key)
	{
		try
		{
			return super.getJSONObject(key);
		} catch (Exception e)
		{
			return null;
		}
	}

	public static <T extends ToJSON> List<JSON> list2Json(List<T> list)
	{
		List<JSON> res = new ArrayList<>();
		for (T i : list)
		{
			res.add(i.toJSON());
		}
		return res;
	}

}