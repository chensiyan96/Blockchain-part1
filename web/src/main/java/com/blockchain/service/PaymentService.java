package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import com.blockchain.dao.PaymentMapper;
import com.blockchain.model.Payment;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService
{

	@Autowired
	private final PaymentMapper paymentMapper;
	@Autowired
	private final AccountMapper accountMapper;

	public PaymentService()
	{
		paymentMapper = null;
		accountMapper = null;
	}

	public PaymentService(PaymentMapper paymentMapper, AccountMapper accountMapper)
	{
		this.paymentMapper = paymentMapper;
		this.accountMapper = accountMapper;
	}

	/**
	 * A 向 B 转账
	 */
	public void transfer(int partyA, int partyB, BigDecimal money) throws Exception
	{
		try
		{
			if (partyA != 1)
			{
				var m = accountMapper.getUserMoney(partyA);
				if (m.compareTo(money) < 0)
				{
					throw new Exception("A的账户余额不足");
				}
				accountMapper.updateUserMoney(partyA, money.negate());
			}
			Payment p = new Payment();
			p.createTime = new Date();
			p.money = money;
			p.partyA = partyA;
			p.partyB = partyB;
			paymentMapper.insertPayment(p);
			accountMapper.updateUserMoney(partyB, money);
		} catch (Exception e)
		{
			throw e;
		}


	}

}
