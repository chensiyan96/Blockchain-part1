package com.blockchain.model;

public enum AgreeStatus
{
	init, //甲方创建
	sign, //乙方签署
	shipped, //乙方发货
	comfirmship,//甲方确认收货
	repaid, //甲方付钱
	complete,//乙方确认收钱


	cancel,//中途取消
	arrears // 甲方超期未付款
}
