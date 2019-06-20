package com.blockchain.controller;

import com.blockchain.model.User;
import com.blockchain.service.MessageService;
import com.blockchain.service.UserService;
import com.blockchain.utils.Authorization;
import com.blockchain.utils.CurrentUser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@CrossOrigin
@RestController
@RequestMapping(value = "api/message")
public class MessageController
{

	@Autowired
	public final MessageService messageService;

	@Autowired
	public final UserService userService;

	public MessageController()
	{
		messageService = null;
		userService = null;
	}

	public MessageController(MessageService messageService, UserService userService)
	{
		this.messageService = messageService;
		this.userService = userService;
	}

	@Authorization
	@RequestMapping(value = "getMessages", method = {RequestMethod.GET})
	public String getMessages(@CurrentUser User user)
	{
		JSONObject response = new JSONObject();
		try
		{
			var messages = messageService.getMessageRec(user.id);
			var lj = new LinkedList<JSONObject>();
			for (var i : messages)
			{
				lj.add(i.toJson());
			}
			response.put("status", 1);
			response.put("msg", "Success");
			response.put("messages", lj);
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}

	@Authorization
	@RequestMapping(value = "updateMsgStatus", method = {RequestMethod.POST})
	public String updateMsgStatus(@CurrentUser User user, @RequestBody String request)
	{

		JSONObject req = new JSONObject(request);
		JSONObject response = new JSONObject();
		try
		{
			var mid = req.getInt("mid");
			var messages = messageService.getMessage(mid);
			if (messages.partyB == user.id)
			{
				messageService.updateStatus(mid);
			}else
			{
				throw new Exception("用户不匹配");
			}
			response.put("status", 1);
			response.put("msg", "Success");
		} catch (Exception e)
		{
			response.put("status", 0);
			response.put("msg", e.getMessage());
		}
		return response.toString();
	}
}
