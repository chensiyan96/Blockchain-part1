package com.blockchain.utils;

import com.blockchain.model.ToJSON;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONUtils
{
	public static String successResponse()
	{
		var response = new JSONObject();
		response.put("status", 1);
		response.put("msg", "Success");
		return response.toString();
	}

	public static String failResponse(String msg)
	{
		var response = new JSONObject();
		response.put("status", 0);
		response.put("msg", msg);
		return response.toString();
	}

	public static <T extends ToJSON> List<JSONObject> list2Json(List<T> list)
	{
		List<JSONObject> res = new ArrayList<>();
		for (T i : list) {
			res.add(i.toJSON());
		}
		return res;
	}
}