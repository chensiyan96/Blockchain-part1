package api;

import utils.exceptions.ReadFailureException;
import utils.exceptions.WriteFailureException;

public interface BlockChainService {
    // 完成实名
    void invokeUserInformation(String userName, String encrypted_message) throws WriteFailureException;

    String queryUserInformation(String userName) throws ReadFailureException;

    // 申请融资
    void invokeFinancingApply(long recordId, String encrypted_message) throws WriteFailureException;

    String queryFinancingApply(long recordId) throws ReadFailureException;

    // 签署合同
    void invokeContract(long recordId, String encrypted_message) throws WriteFailureException;

    String queryContract(long recordId) throws ReadFailureException;

    // 完成放款/还款
    void invokeTransaction(long recordId, String encrypted_message) throws WriteFailureException;

    String queryTransaction(long recordId) throws ReadFailureException;
}
