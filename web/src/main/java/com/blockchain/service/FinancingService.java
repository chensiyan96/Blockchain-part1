package com.blockchain.service;

import com.blockchain.dao.FinancingMapper;
import com.blockchain.model.Financing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FinancingService
{
	@Autowired
	private FinancingMapper financingMapper;

	public long insertFinancing(Financing fin)
	{
		return fin.db.id = financingMapper.insertFinancing(fin.db);
	}

	public Financing getFinancingByID(long id)
	{
		var db = financingMapper.getFinancingById(id);
		return db == null ? null : new Financing(db);
	}

	public void updateFinancingStatus(Financing fin)
	{
		financingMapper.updateFinancingState(fin.db.id, fin.db.status);
	}

	public Financing[] getFinancingBySidAndStatus(long sid, byte status)
	{
		return constructFinancingArray(financingMapper.getFinancingBySidAndStatus(sid, status));
	}

	public Financing[] getFinancingByCidAndStatus(long cid, byte status)
	{
		return constructFinancingArray(financingMapper.getFinancingByCidAndStatus(cid, status));
	}

	public Financing[] getFinancingByMidAndStatus(long mid, byte status)
	{
		return constructFinancingArray(financingMapper.getFinancingByMidAndStatus(mid, status));
	}

	private static Financing[] constructFinancingArray(Financing.DataBase[] dbs)
	{
		var fins = new Financing[dbs.length];
		for (int i = 0; i < dbs.length; i++) {
			fins[i] = new Financing(dbs[i]);
		}
		return fins;
	}
}
