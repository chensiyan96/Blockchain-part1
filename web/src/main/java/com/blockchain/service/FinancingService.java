package com.blockchain.service;

import com.blockchain.dao.FinancingMapper;
import com.blockchain.model.Financing;
import com.blockchain.model.FinancingStatus;
import com.blockchain.utils.JSON;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FinancingService
{

	@Autowired
	private final FinancingMapper financingMapper;

	public FinancingService()
	{
		financingMapper = null;
	}

	public FinancingService(FinancingMapper financingMapper)
	{
		this.financingMapper = financingMapper;
	}

	public int create(JSON j) throws Exception
	{
		Financing f = new Financing();
		f.aid = j.getInt("aid");
		f.createTime = new Date();
		f.mid = j.getInt("mid");
		f.partyA = j.getInt("partyA");
		f.partyB = j.getInt("partyB");
		f.terms = j.getString("terms");
		f.status = FinancingStatus.init;
		financingMapper.insertFinancing(f);

		if (f.id == 0)
		{
			throw new Exception("合同创建错误");
		}
		return f.id;
	}

	public Financing getFinancing(int id) throws Exception
	{
		var res = financingMapper.getFinancing(id);
		if (res == null)
		{
			throw new Exception("id错误");
		}
		return res;
	}

	public void updateStatus(FinancingStatus status, int id)
	{
		financingMapper.updateStatus(id, status);
	}
}
