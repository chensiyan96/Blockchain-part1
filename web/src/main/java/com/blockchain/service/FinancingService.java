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
		financingMapper.updateFinancingState(fin.db.id, fin.db.state);
	}
}
