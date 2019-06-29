package provider.fabricSdk.bean;

public class Chaincode {

    /**
     * 当前将要访问的智能合约所属频道名称
     */
    private String channelName;
    /**
     * 智能合约名称
     */
    private String chaincodeName;
    /**
     * 智能合约安装路径
     */
    private String chaincodePath;
    /**
     * 智能合约版本号
     */
    private String chaincodeVersion;
    /**
     * 执行智能合约操作等待时间
     */
    private int invokeWatiTime = 100000;
    /**
     * 执行智能合约实例等待时间
     */
    private int deployWatiTime = 120000;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChaincodeName() {
        return chaincodeName;
    }

    public void setChaincodeName(String chaincodeName) {
        this.chaincodeName = chaincodeName;
    }

    public String getChaincodePath() {
        return chaincodePath;
    }

    public void setChaincodePath(String chaincodePath) {
        this.chaincodePath = chaincodePath;
    }

    public String getChaincodeVersion() {
        return chaincodeVersion;
    }

    public void setChaincodeVersion(String chaincodeVersion) {
        this.chaincodeVersion = chaincodeVersion;
    }

    public int getInvokeWatiTime() {
        return invokeWatiTime;
    }

    public void setInvokeWatiTime(int invokeWatiTime) {
        this.invokeWatiTime = invokeWatiTime;
    }

    public int getDeployWatiTime() {
        return deployWatiTime;
    }

    public void setDeployWatiTime(int deployWatiTime) {
        this.deployWatiTime = deployWatiTime;
    }
}