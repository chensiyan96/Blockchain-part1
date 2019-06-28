package com.blockchain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountService
{
	// 创建账户
	public void createAccount(long uid)
	{

		// todo：在区块链中将该账户余额初始化为0
	}

	// 余额查询
	public BigDecimal queryMoney(long uid)
	{
		// todo：从区块链里查询余额并返回
		return BigDecimal.ZERO;
	}

	// 充值
	public boolean investMoney(long uid, BigDecimal money)
	{
		if (true) { // 伪：判断用户银行卡里余额是否够用
			// todo：在区块链里增加余额
			// 伪：从用户银行账户转账到平台银行账户
			return true;
		}
		return false;
	}

	// 提现
	public boolean withdrawMoney(long uid, BigDecimal money)
	{
		if (queryMoney(uid).compareTo(money) >= 0){
			// todo：在区块链里减少余额
			// 伪：从平台银行账户转账到用户银行账户
			return true;
		}
		return false;
	}

	// 转账
	public boolean transferMoney(long uid_dst, long uid_src, BigDecimal money)
	{
		if (queryMoney(uid_dst).compareTo(money) >= 0){
			// todo：在区块链里变更余额
			return true;
		}
		return false;
	}
}
