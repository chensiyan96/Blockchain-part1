package provider.fabricSdk;

import io.netty.util.internal.StringUtil;
import org.bouncycastle.util.Strings;
import org.bouncycastle.util.encoders.Hex;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.*;
import java.util.Set;

class FabricUser implements User, Serializable {

    private static final long serialVersionUID = 5695080465408336815L;
    /**
     * 注册登记操作
     */
    Enrollment enrollment = null; // 需要在测试env中访问
    /**
     * 名称
     */
    private String name;
    /**
     * 规则
     */
    private Set<String> roles;
    /**
     * 账户
     */
    private String account;
    /**
     * 从属联盟
     */
    private String affiliation;
    /**
     * 组织
     */
    private String organization;
    /**
     * 注册操作的密码
     */
    private String enrollmentSecret;
    /**
     * 会员id
     */
    private String mspId;
    /**
     * 存储配置对象
     */
    private transient FabricStore keyValStore;
    private String keyValStoreName;

    public FabricUser(String name, String org, FabricStore store) {
        this.name = name;
        this.keyValStore = store;
        this.organization = org;
        this.keyValStoreName = toKeyValStoreName(this.name, org);

        String memberStr = keyValStore.getValue(keyValStoreName);
        if (null != memberStr) {
            saveState();
        } else {
            restoreState();
        }
    }

    public static String toKeyValStoreName(String name, String org) {
        System.out.println("toKeyValStoreName = " + "user." + name + org);
        return "user." + name + org;
    }

    @Override
    public String getAccount() {
        return this.account;
    }

    /**
     * 设置账户信息并将用户状态更新至存储配置对象
     *
     * @param account 账户
     */
    public void setAccount(String account) {
        this.account = account;
        saveState();
    }

    @Override
    public String getAffiliation() {
        return this.affiliation;
    }

    /**
     * 设置从属联盟信息并将用户状态更新至存储配置对象
     *
     * @param affiliation 从属联盟
     */
    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
        saveState();
    }

    @Override
    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    /**
     * 设置注册登记操作信息并将用户状态更新至存储配置对象
     *
     * @param enrollment 注册登记操作
     */
    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
        saveState();
    }

    @Override
    public String getMspId() {
        return this.mspId;
    }

    /**
     * 设置会员id信息并将用户状态更新至存储配置对象
     *
     * @param mspID 会员id
     */
    public void setMspId(String mspID) {
        this.mspId = mspID;
        saveState();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<String> getRoles() {
        return this.roles;
    }

    /**
     * 设置规则信息并将用户状态更新至存储配置对象
     *
     * @param roles 规则
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
        saveState();
    }

    public String getEnrollmentSecret() {
        return enrollmentSecret;
    }

    /**
     * 设置注册操作的密钥信息并将用户状态更新至存储配置对象
     *
     * @param enrollmentSecret 注册操作的密码
     */
    public void setEnrollmentSecret(String enrollmentSecret) {
        this.enrollmentSecret = enrollmentSecret;
        saveState();
    }

    /**
     * 确定这个名称是否已注册
     *
     * @return 与否
     */
    public boolean isRegistered() {
        return !StringUtil.isNullOrEmpty(enrollmentSecret);
    }

    /**
     * 确定这个名字是否已经注册
     *
     * @return 与否
     */
    public boolean isEnrolled() {
        return this.enrollment != null;
    }

    /**
     * 将用户状态保存至存储配置对象
     */
    public void saveState() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            keyValStore.setValue(keyValStoreName, Strings.fromByteArray(Hex.encode(bos.toByteArray())));
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从键值存储中恢复该用户的状态(如果找到的话)。如果找不到，什么也不要做
     *
     * @return 返回用户
     */
    private FabricUser restoreState() {
        String memberStr = keyValStore.getValue(keyValStoreName);
        if (null != memberStr) {
            // 用户在键值存储中被找到，因此恢复状态
            byte[] serialized = Hex.decode(memberStr);
            ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                FabricUser state = (FabricUser) ois.readObject();
                if (state != null) {
                    this.name = state.name;
                    this.roles = state.roles;
                    this.account = state.account;
                    this.affiliation = state.affiliation;
                    this.organization = state.organization;
                    this.enrollmentSecret = state.enrollmentSecret;
                    this.enrollment = state.enrollment;
                    this.mspId = state.mspId;
                    return this;
                }
            } catch (Exception e) {
                throw new RuntimeException(String.format("Could not restore state of member %s", this.name), e);
            }
        }
        return null;
    }
}