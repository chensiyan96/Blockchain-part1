package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService
{

	@Autowired
	private final AccountMapper accountMapper;

	public AccountService()
	{

		accountMapper = null;
	}

	public AccountService(AccountMapper accountMapper)
	{
		this.accountMapper = accountMapper;
	}

	public BigDecimal getMoney(int uid)
	{
		return accountMapper.getUserMoney(uid);
	}

	public void create(int uid) throws Exception
	{
		if(uid == 0)
		{
			throw new Exception("用户id错误");
		}
		accountMapper.insertUserAccount(uid);
	}

}
