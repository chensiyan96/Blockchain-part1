package com.blockchain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PageController
{

	//默认跳转首页
	@RequestMapping("/")
	public String showIndex() throws Exception
	{
		return "index";
	}
}
