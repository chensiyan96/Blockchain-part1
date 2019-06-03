package com.blockchain.service;

import com.blockchain.dao.MortgagesMapper;
import com.blockchain.model.Mortgages;
import com.blockchain.utils.JSON;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MortgageService
{

	@Autowired
	private MortgagesMapper mortgagesMapper;

	public int create(int cid, BigDecimal money, int partyA, int partyB) throws Exception
	{
		Mortgages m = new Mortgages();
		try
		{
			m.cid = cid;
			m.createTime = new Date();
			m.money = money;
			m.partyA = partyA;
			m.partyB = partyB;
			mortgagesMapper.insertMortgage(m);
		} catch (Exception e)
		{
			throw new Exception("参数错误");
		}
		return m.id;
	}

	public Mortgages getMortgage(int id)
	{
		return mortgagesMapper.getMortgage(id);
	}

	public List<Mortgages> getMortgages(int cid)
	{
		return mortgagesMapper.getMortgages(cid);
	}

	public void updateStatus(int status, int id)
	{
		mortgagesMapper.updateStatus(status, id);
	}

}
