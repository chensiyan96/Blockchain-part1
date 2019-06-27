package com.blockchain.service;

import com.blockchain.dao.AccessRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccessRuleService
{
    @Autowired
    private AccessRuleMapper accessRuleMapper;

    public String getAccessRule()
    {
        var rule = accessRuleMapper.getAccessRuleById(0);
        return rule == null ? "还没设置准入规则" : rule;
    }

    public void setAccessRule(String rule)
    {
        if (accessRuleMapper.getAccessRuleById(0) != null) {
            accessRuleMapper.deleteAccessRuleById(0);
        }
        accessRuleMapper.insertAccessRule(0, rule);
    }
}
