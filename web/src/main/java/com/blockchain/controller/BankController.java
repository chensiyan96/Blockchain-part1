package com.blockchain.controller;

import com.blockchain.service.FinancingService;
import com.blockchain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class BankController
{
	@Autowired
	private UserService userService;
	@Autowired
	private FinancingService financingService;
	//todo:获取所有金融机构

	//todo: 资金方获取所有有业务往来的供应商

	//todo: 产品的添加 名称 业务 借款金额 借款期限 还款方式 状态
	//todo: 查看所有可以显示的产品
	//todo: 修改产品状态
	//todo: 供应商评级
}
