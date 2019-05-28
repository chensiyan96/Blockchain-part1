package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import com.blockchain.dao.CreditMapper;
import com.blockchain.dao.PaymentMapper;
import com.blockchain.model.Credit;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreditService
{

	@Autowired
	private final CreditMapper creditMapper;

	public CreditService()
	{
		creditMapper = null;
	}

	public CreditService(CreditMapper creditMapper)
	{
		this.creditMapper = creditMapper;
	}

	public int create(int partyA, int partyB, Date creaetTime, Date deadline, BigDecimal money)
			throws Exception
	{

		Credit c = new Credit();
		c.partyA = partyA;
		c.partyB = partyB;
		c.createTime = creaetTime;
		c.deadline = deadline;
		c.money = money;

		creditMapper.insertCredit(c);
		if (c.id == 0)
		{
			throw new Exception("借条创建错误");
		}
		return c.id;
	}

	public Credit getCredit(int id) throws Exception
	{
		var res = creditMapper.getCredit(id);
		if (res == null)
		{
			throw new Exception("id错误");
		}
		return res;
	}

	public void updateStatus(int status, int id)
	{
		creditMapper.updateStatus(status, id);
	}

	public void updatePartyB(int uid, int id)
	{
		creditMapper.updatePartyB(uid, id);
	}


}
