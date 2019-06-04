package com.blockchain.service;

import com.blockchain.dao.FinancingMapper;
import com.blockchain.model.Financing;
import com.blockchain.model.FinancingStatus;
import com.blockchain.utils.JSON;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FinancingService
{

	@Autowired
	private FinancingMapper financingMapper;

	public int create(int aid, int mid, int partyA, int partyB, String terms) throws Exception
	{
		Financing f = new Financing();
		f.aid = aid;
		f.createTime = new Date();
		f.mid = mid;
		f.partyA = partyA;
		f.partyB = partyB;
		f.terms = terms;
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

	public List<Financing> getFinancingByUser(int uid) throws Exception
	{
		var res = financingMapper.getFinancingByUser(uid);
		return res;
	}

	public void updateStatus(FinancingStatus status, int id)
	{
		financingMapper.updateStatus(id, status);
	}
}
