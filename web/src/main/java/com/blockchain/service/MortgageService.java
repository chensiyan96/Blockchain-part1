package com.blockchain.service;

import com.blockchain.dao.MortgagesMapper;
import com.blockchain.model.Mortgages;
import com.blockchain.utils.JSON;
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
	private final MortgagesMapper mortgagesMapper;

	public MortgageService()
	{
		mortgagesMapper = null;
	}

	public MortgageService(MortgagesMapper mortgagesMapper)
	{
		this.mortgagesMapper = mortgagesMapper;
	}

	public int create(JSON j) throws Exception
	{
		Mortgages m = new Mortgages();
		try
		{
			m.cid = j.getInt("cid");
			m.createTime = new Date();
			m.money = j.getBigDecimal("money");
			m.partyA = j.getInt("partyA");
			m.partyB = j.getInt("partyB");
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
