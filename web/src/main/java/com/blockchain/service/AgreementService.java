package com.blockchain.service;

import com.blockchain.dao.AgreementMapper;
import com.blockchain.model.AgreeStatus;
import com.blockchain.model.Agreement;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgreementService
{

	@Autowired
	private AgreementMapper agreementMapper;

	public int create(int partyA, int partyB, Date createTime, int creditId, String terms)
			throws Exception
	{
		Agreement a = new Agreement();
		a.createTime = createTime;
		a.creditId = creditId;
		a.partyA = partyA;
		a.partyB = partyB;
		a.terms = terms;
		a.status = AgreeStatus.init;
		agreementMapper.insertAgreement(a);

		if (a.id == 0)
		{
			throw new Exception("合同创建错误");
		}
		return a.id;
	}

	public Agreement getAgreement(int id) throws Exception
	{
		var res = agreementMapper.getAgreement(id);
		if (res == null)
		{
			throw new Exception("id错误");
		}
		return res;
	}

	public List<Agreement> getAgreementsByUser(int id) throws Exception
	{
		return agreementMapper.getAgreements(id);
	}

	public void updateStatus(AgreeStatus status, int id)
	{
		agreementMapper.updateStatus(id, status);
	}


}
