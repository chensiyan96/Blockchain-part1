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
	@Autowired
	private UserService userService;

	public long insertFinancing(Financing fin)
	{
		if (financingMapper.insertFinancing(fin.db)) {
			return fin.db.id = financingMapper.getlastInsertId();
		}
		return 0;
	}

	public Financing getFinancingByID(long id)
	{
		var db = financingMapper.getFinancingById(id);
		if (db == null) {
			return null;
		}
		var fin = new Financing(db);
		fin.setSupplier(userService.getUserByID(fin.db.sid));
		fin.setCoreBusiness(userService.getUserByID(fin.db.cid));
		fin.setMoneyGiver(userService.getUserByID(fin.db.mid));
		return fin;
	}

	public void updateFinancingStatus(Financing fin)
	{
		financingMapper.updateFinancingStatus(fin.db.id, fin.db.status);
	}

	public Financing[] getFinancingBySid(long sid)
	{
		return constructFinancingArray(financingMapper.getFinancingBySid(sid));
	}

	public Financing[] getFinancingByCid(long cid)
	{
		return constructFinancingArray(financingMapper.getFinancingByCid(cid));
	}

	public Financing[] getFinancingByMid(long mid)
	{
		return constructFinancingArray(financingMapper.getFinancingByMid(mid));
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

	public int getFinancingCountBySidAndStatus(long sid, byte status)
	{
		return financingMapper.getFinancingCountBySidAndStatus(sid, status);
	}

	public int getFinancingCountByCidAndStatus(long cid, byte status)
	{
		return financingMapper.getFinancingCountByCidAndStatus(cid, status);
	}

	public int getFinancingCountByMidAndStatus(long mid, byte status)
	{
		return financingMapper.getFinancingCountByMidAndStatus(mid, status);
	}

	private Financing[] constructFinancingArray(Financing.DataBase[] dbs)
	{
		var fins = new Financing[dbs.length];
		for (int i = 0; i < dbs.length; i++) {
			fins[i] = new Financing(dbs[i]);
			fins[i].setSupplier(userService.getUserByID(fins[i].db.sid));
			fins[i].setCoreBusiness(userService.getUserByID(fins[i].db.cid));
			fins[i].setMoneyGiver(userService.getUserByID(fins[i].db.mid));
		}
		return fins;
	}
}
