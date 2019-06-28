package com.blockchain.service;

import com.blockchain.dao.UserMapper;
import com.blockchain.model.CoreBusinessProfile;
import com.blockchain.model.MoneyGiverProfile;
import com.blockchain.model.SupplierProfile;
import com.blockchain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService
{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CreditService creditService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private FinancingService financingService;

	public long insertUser(User user)
	{
		if (userMapper.insertUser(user.db)) {
			return user.db.id = userMapper.getlastInsertId();
		}
		return 0;
	}

	public User getUserByID(long id)
	{
		var db = userMapper.getUserById(id);
		return db == null ? null : new User(db);
	}

	public User getUserByEmail(String email)
	{
		var db = userMapper.getUserByEmail(email);
		return db == null ? null : new User(db);
	}

	public void updateUser(User user)
	{
		userMapper.updateUserInfo(user.db.id, user.db.name, user.db.passwordHash, user.db.additional, user.db.frozen, user.db.autoPass);
	}

	public User[] getAllSuppliers()
	{
		return constructUserArray(userMapper.getAllSuppliers());
	}

	public User[] getAllCoreBusinesses()
	{
		return constructUserArray(userMapper.getAllCoreBusinesses());
	}

	public User[] getAllMoneyGivers()
	{
		return constructUserArray(userMapper.getAllMoneyGivers());
	}

	public void addProfile(User user)
	{
		switch (user.db.role)
		{
			case Supplier: {
				var profile = new SupplierProfile();
				var credit = creditService.getCreditById(user.db.id);
				profile.rank = credit.db.rank;
				profile.applied = credit.db.applied;
				profile.approved = credit.db.approved;
				profile.orderCount = orderService.getOrderCountBySidAndStatus(user.db.id, (byte)0);
				profile.finCounts = new int[4];
				for (byte i = 0; i < 4; i++) {
					profile.finCounts[i] = financingService.getFinancingCountBySidAndStatus(user.db.id, i);
				}
				user.profile = profile;
				break;
			}
			case CoreBusiness: {
				var profile = new CoreBusinessProfile();
				user.profile = profile;
				profile.orderCount = orderService.getOrderCountByCidAndStatus(user.db.id, (byte)0);
				profile.finCounts = new int[4];
				for (byte i = 0; i < 4; i++) {
					profile.finCounts[i] = financingService.getFinancingCountByCidAndStatus(user.db.id, i);
				}
				break;
			}
			case MoneyGiver: {
				var profile = new MoneyGiverProfile();
				user.profile = profile;
				profile.finCounts = new int[4];
				for (byte i = 0; i < 4; i++) {
					profile.finCounts[i] = financingService.getFinancingCountByMidAndStatus(user.db.id, i);
				}
				break;
			}
			case Admin:
				user.profile = null;
				break;
		}
	}

	public void setVerification(String email, String content)
	{
//		BlockChainServiceImpl blockChainService = new BlockChainServiceImpl();
//		try {
//			blockChainService.invokeUserInformation(email, content);
//		} catch (WriteFailureException e) {
//			e.printStackTrace();
//		}
	}

	public String getVerification(String email)
	{
//		BlockChainServiceImpl blockChainService = new BlockChainServiceImpl();
//		try {
//			blockChainService.invokeUserInformation(email, "aaa");
//			Thread.sleep(100);
//		} catch (WriteFailureException e) {
//			return "WriteFailureException";
//		} catch (InterruptedException e) {
//			return "InterruptedException";
//		}
//		try {
//			return blockChainService.queryUserInformation(email);
//		} catch (ReadFailureException e) {
//			return "ReadFailureException";
//		}
		return null;
	}

	private User[] constructUserArray(User.DataBase[] dbs)
	{
		var users = new User[dbs.length];
		for (int i = 0; i < dbs.length; i++) {
			users[i] = new User(dbs[i]);
			addProfile(users[i]);
		}
		return users;
	}
}
