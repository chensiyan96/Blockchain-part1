package com.blockchain.service;

import com.blockchain.dao.CreditMapper;
import com.blockchain.model.Credit;
import com.blockchain.model.CreditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class CreditService
{
	@Autowired
	private CreditMapper creditMapper;

	public boolean insertCredit(Credit credit)
	{
		var record = new CreditRecord();
		record.sid = credit.db.sid;
		record.applied = credit.db.approved;
		record.approved = credit.db.approved;
		record.createTime = new Timestamp(System.currentTimeMillis());
		creditMapper.insertCreditRecord(record);
		return creditMapper.insertCredit(credit.db);
	}

	public Credit getCreditById(long id)
	{
		var db = creditMapper.getCreditById(id);
		return db == null ? null : new Credit(db);
	}

	public void updateCreditInfo(Credit credit)
	{
		creditMapper.updateCreditInfo(credit.db.sid, credit.db.rank, credit.db.applied, credit.db.approved);
	}

	public CreditRecord[] getRecordCredit(long sid)
	{
		return creditMapper.getRecordCredit(sid);
	}
}
