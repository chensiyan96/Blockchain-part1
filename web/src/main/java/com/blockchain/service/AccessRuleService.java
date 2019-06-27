package com.blockchain.service;

import com.blockchain.dao.AccessRuleMapper;
import com.blockchain.model.AccessRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccessRuleService
{
    @Autowired
    private AccessRuleMapper accessRuleMapper;

    public long insertAccessRule(String content)
    {
        if (accessRuleMapper.insertAccessRule(content)) {
            return accessRuleMapper.getlastInsertId();
        }
        return 0;
    }

    public AccessRule getAccessRuleById(long id)
    {
        return accessRuleMapper.getAccessRuleById(id);
    }

    public AccessRule[] getAllAccessRules()
    {
        return accessRuleMapper.getAllAccessRules();
    }

    public void updateAccessRule(long id, String content)
    {
        accessRuleMapper.updateAccessRule(id, content);
    }

    public void deleteAccessRule(long id)
    {
        accessRuleMapper.deleteAccessRule(id);
    }
}
