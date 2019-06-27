package com.blockchain.utils;

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

	public static <T> String successResponse(T data)
	{
		var response = new JSONObject();
		response.put("status", 1);
		response.put("msg", "Success");
		response.put("data", data);
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

	public static <T extends ToJSON> JSONObject[] arrayToJSONs(T[] ts)
	{
		var res = new JSONObject[ts.length];
		for (int i = 0; i< ts.length; i++) {
			res[i] = ts[i].toJSON();
		}
		return res;
	}
}