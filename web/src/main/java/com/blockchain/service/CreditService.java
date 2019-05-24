package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import com.blockchain.dao.CreditMapper;
import com.blockchain.dao.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreditService
{

	@Autowired
	private final CreditMapper creditMapper;
	@Autowired
	private final PaymentMapper paymentMapper;
	@Autowired
	private final AccountMapper accountMapper;

	public CreditService()
	{
		accountMapper = null;
		creditMapper = null;
		paymentMapper = null;
	}
	public CreditService(CreditMapper creditMapper, PaymentMapper paymentMapper,
			AccountMapper accountMapper)
	{
		this.creditMapper = creditMapper;
		this.paymentMapper = paymentMapper;
		this.accountMapper = accountMapper;
	}

/*	public int create(int partyA,int partyB, )
	{

	}*/

}
