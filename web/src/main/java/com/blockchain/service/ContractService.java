package com.blockchain.service;

import com.blockchain.model.Contract;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContractService
{
    public Contract[] getContractBySid(long sid)
    {
        // todo
        return new Contract[0];
    }

    public Contract[] getContractByCid(long cid)
    {
        // todo
        return new Contract[0];
    }
}
