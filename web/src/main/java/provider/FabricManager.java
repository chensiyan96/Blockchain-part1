package provider;

import provider.fabricSdk.ChaincodeManager;
import provider.fabricSdk.FabricConfig;
import provider.fabricSdk.bean.Chaincode;
import provider.fabricSdk.bean.Orderers;
import provider.fabricSdk.bean.Peers;

import java.io.File;

public class FabricManager {

    //    private static Logger log = Logger.getLogger(ChaincodeManager.class);
    private ChaincodeManager manager;

    private static FabricManager instance = null;

    private FabricManager()
            throws Exception {
        manager = new ChaincodeManager(getConfig());
    }

    public static FabricManager obtain()
            throws Exception {
        if (null == instance) {
            synchronized (FabricManager.class) {
                if (null == instance) {
                    instance = new FabricManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取节点服务器管理器
     *
     * @return 节点服务器管理器
     */
    public ChaincodeManager getManager() {
        return manager;
    }

    /**
     * 根据节点作用类型获取节点服务器配置
     *
     * @return 节点服务器配置
     */
    private FabricConfig getConfig() {
        FabricConfig config = new FabricConfig();
        config.setOrderers(getOrderers());
        config.setPeers(getPeers());
        config.setChaincode(getChaincode("mychannel", "chainService", "github.com/hyperledger/fabric/peer/chaincode", "1.4"));
        config.setChannelArtifactsPath(getChannelArtifactsPath());
        config.setCryptoConfigPath(getCryptoConfigPath());
        return config;
    }

    private Orderers getOrderers() {
        Orderers orderer = new Orderers();
        orderer.setOrdererDomainName("example.com");
        orderer.addOrderer("orderer.example.com", "grpc://114.116.9.214:7050");
        return orderer;
    }

    /**
     * 获取节点服务器集
     *
     * @return 节点服务器集
     */
    private Peers getPeers() {
        Peers peers = new Peers();
        peers.setOrgName("Org1");
        peers.setOrgMSPID("Org1MSP");
        peers.setOrgDomainName("org1.example.com");
        peers.addPeer("peer0.org1.example.com", "peer0.org1.example.com", "grpc://114.116.9.214:7056", "grpc://114.116.9.214:7057", "http://114.116.9.214:7054");
        peers.addPeer("peer1.org1.example.com", "peer1.org1.example.com", "grpc://114.116.9.214:7051", "grpc://114.116.9.214:7053", "http://114.116.9.214:7054");
        return peers;
    }

    /**
     * 获取智能合约
     *
     * @param channelName      频道名称
     * @param chaincodeName    智能合约名称
     * @param chaincodePath    智能合约路径
     * @param chaincodeVersion 智能合约版本
     * @return 智能合约
     */
    private Chaincode getChaincode(String channelName, String chaincodeName, String chaincodePath, String chaincodeVersion) {
        Chaincode chaincode = new Chaincode();
        chaincode.setChannelName(channelName);
        chaincode.setChaincodeName(chaincodeName);
        chaincode.setChaincodePath(chaincodePath);
        chaincode.setChaincodeVersion(chaincodeVersion);
        chaincode.setInvokeWatiTime(100000);
        chaincode.setDeployWatiTime(120000);
        return chaincode;
    }

    /**
     * 获取channel-artifacts配置路径
     *
     * @return /WEB-INF/classes/fabric/channel-artifacts/
     */
    private String getChannelArtifactsPath() {
        //String dir = FabricManager.class.getClassLoader().getResource("basic-network").getFile();
        String dir = System.getProperty("user.dir") + "/basic-network";
//        log.debug("dir = " + dir);
        File directory = new File(dir);
//        log.debug("directory = " + directory.getPath());

//        return directory.getPath() + "/channel-artifacts/";
        return directory.getPath() + "/config/";
//        File directory = new File("");//设定为当前文件夹
//        try{
//            System.out.println(directory.getCanonicalPath());//获取标准的路径
//            System.out.println(directory.getAbsolutePath());//获取绝对路径
//        }catch(Exception e){}
//        return "../basic-network/config";
    }

    /**
     * 获取crypto-config配置路径
     *
     * @return /WEB-INF/classes/fabric/crypto-config/
     */
    private String getCryptoConfigPath() {
        //String dir = FabricManager.class.getClassLoader().getResource("basic-network").getFile();
        String dir = System.getProperty("user.dir") + "/basic-network";
//        log.debug("dir = " + dir);
        System.out.println(dir);
        File directory = new File(dir);
//        log.debug("directory = " + directory.getPath());
        return directory.getPath() + "/crypto-config/";
//        return "../basic-network/crypto-config";
    }
}