package com.blockchain.service;

import com.blockchain.dao.CreditMapper;
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
	private CreditMapper creditMapper;

	public int create(int partyA, int partyB, Date createTime, Date deadline, BigDecimal money)
			throws Exception
	{

		Credit c = new Credit();
		c.partyA = partyA;
		c.partyB = partyB;
		c.createTime = createTime;
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
}
