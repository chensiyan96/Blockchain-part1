package com.blockchain.service;

import com.blockchain.dao.CreditMapper;
import com.blockchain.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreditService
{
	@Autowired
	private CreditMapper creditMapper;

	public boolean insertCredit(Credit credit)
	{
		return creditMapper.insertCredit(credit.db);
	}

	public Credit getCreditById(int id)
	{
		var db = creditMapper.getCreditById(id);
		return db == null ? null : new Credit(db);
	}

	public void updateCreditInfo(Credit credit)
	{
		creditMapper.updateCreditInfo(credit.db.id, credit.db.rank, credit.db.applied, credit.db.approved);
	}
}
