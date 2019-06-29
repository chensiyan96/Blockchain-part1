package provider;

import api.BlockChainService;
import org.apache.log4j.Logger;
import provider.fabricSdk.ChaincodeManager;
import utils.exceptions.ReadFailureException;
import utils.exceptions.WriteFailureException;

import java.util.Map;

public class BlockChainServiceImpl implements BlockChainService {
    private static String INVOKE_USER_INFO = "insertUserInfo";
    private static String QUERY_USER_INFO = "queryUserInfo";
    private static String INVOKE_FINANCING_APPLY = "insertFinancingApplyRecord";
    private static String QUERY_FINANCING_APPLY = "queryFinancingApplyRecord";
    private static String INVOKE_CONTRACT = "insertContractRecord";
    private static String QUERY_CONTRACT = "queryContractRecord";
    private static String INVOKE_LOAN = "insertTransactionRecord";
    private static String QUERY_LOAN = "queryTransactionRecord";

    private FabricManager fabricmanager;
    private ChaincodeManager chaincodeManager;

    private static Logger log = Logger.getLogger(BlockChainServiceImpl.class);

    @Override
    public void invokeUserInformation(String userName, String encrypted_message) throws WriteFailureException {
        boolean result = insert(INVOKE_USER_INFO, userName, encrypted_message);
        if (result) {
            log.info("InvokeUserInformation succeed, username = " + userName);
        } else {
            log.info("InvokeUserInformation failed, username = " + userName);
            throw new WriteFailureException();
        }
    }

    @Override
    public String queryUserInformation(String userName) throws ReadFailureException {
        String result = query(QUERY_USER_INFO, userName);
        if (result != null && !result.isEmpty()) {
            log.info("QueryUserInformation succeed, username = " + userName);
        } else {
            log.info("QueryUserInformation failed, username = " + userName);
            throw new ReadFailureException();
        }
        return result;
    }

    @Override
    public void invokeFinancingApply(long recordId, String encrypted_message) throws WriteFailureException {
        boolean result = insert(INVOKE_FINANCING_APPLY, String.valueOf(recordId), encrypted_message);
        if (result) {
            log.info("InvokeFinancingApply succeed, recordId = " + recordId);
        } else {
            log.info("InvokeFinancingApply failed, recordId = " + recordId);
            throw new WriteFailureException();
        }
    }

    @Override
    public String queryFinancingApply(long recordId) throws ReadFailureException {
        String result = query(QUERY_FINANCING_APPLY, String.valueOf(recordId));
        if (result != null && !result.isEmpty()) {
            log.info("QueryFinancingApply succeed, recordId = " + recordId);
        } else {
            log.info("QueryFinancingApply failed, recordId = " + recordId);
            throw new ReadFailureException();
        }
        return result;
    }

    @Override
    public void invokeContract(long recordId, String encrypted_message) throws WriteFailureException {
        boolean result = insert(INVOKE_CONTRACT, String.valueOf(recordId), encrypted_message);
        if (result) {
            log.info("InvokeContract succeed, recordId = " + recordId);
        } else {
            log.info("InvokeContract failed, recordId = " + recordId);
            throw new WriteFailureException();
        }
    }

    @Override
    public String queryContract(long recordId) throws ReadFailureException {
        String result = query(QUERY_CONTRACT, String.valueOf(recordId));
        if (result != null && !result.isEmpty()) {
            log.info("QueryContract succeed, recordId = " + recordId);
        } else {
            log.info("QueryContract failed, recordId = " + recordId);
            throw new ReadFailureException();
        }
        return result;
    }

    @Override
    public void invokeTransaction(long recordId, String encrypted_message) throws WriteFailureException {
        boolean result = insert(INVOKE_LOAN, String.valueOf(recordId), encrypted_message);
        if (result) {
            log.info("InvokeTransaction succeed, recordId = " + recordId);
        } else {
            log.info("InvokeTransaction failed, recordId = " + recordId);
            throw new WriteFailureException();
        }
    }

    @Override
    public String queryTransaction(long recordId) throws ReadFailureException {
        String result = query(QUERY_LOAN, String.valueOf(recordId));
        if (result != null && !result.isEmpty()) {
            log.info("QueryTransaction succeed, recordId = " + recordId);
        } else {
            log.info("QueryTransaction failed, recordId = " + recordId);
            throw new ReadFailureException();
        }
        return result;
    }

    private String query(String queryFunc, String arg) {
        try {
            if (fabricmanager == null) {
                fabricmanager = FabricManager.obtain();
            }
            if (chaincodeManager == null) {
                chaincodeManager = fabricmanager.getManager();
            }
            String[] args = new String[1];
            args[0] = arg;
            Map<String, String> resultMap = chaincodeManager.query(queryFunc, args);
            if (resultMap.get("code").equals("success")) {
                return resultMap.get("data");
            } else {
                return null;
            }
        } catch (Exception e) {
            // log.error(e.getMessage());
            return null;
        }
    }

    private boolean insert(String invokeFunc, String arg, String encrypted_message) {
        try {
            if (fabricmanager == null) {
                fabricmanager = FabricManager.obtain();
            }
            if (chaincodeManager == null) {
                chaincodeManager = fabricmanager.getManager();
            }
            String[] args = new String[2];
            args[0] = arg;
            args[1] = encrypted_message;
            Map<String, String> resultMap = chaincodeManager.invoke(invokeFunc, args);
            return resultMap.get("code").equals("success");
        } catch (Exception e) {
            // log.error(e.getMessage());
            return false;
        }
    }
}