package com.blockchain.service;

import com.blockchain.dao.AccountMapper;
import com.blockchain.dao.UserMapper;
import com.blockchain.model.Transfer;
import com.blockchain.model.User;
import com.blockchain.utils.GetFabricManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.exceptions.ReadFailureException;
import utils.exceptions.WriteFailureException;

import java.math.BigDecimal;

@Service
@Transactional
public class AccountService
{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AccountMapper accountMapper;

	// 创建账户
	public void createAccount(long uid)
	{
		accountMapper.insertTransfer(uid, 0);
		var transfer = new Transfer();
		transfer.db.id = accountMapper.getlastInsertId();
		transfer.db.dst = uid;
		transfer.db.src = -1;
		transfer.money = BigDecimal.ZERO;
		transfer.dst_remain = BigDecimal.ZERO;
		transfer.src_remain = null;
		userMapper.updateLastTransfer(uid, transfer.db.id);
		try {
			GetFabricManager.getBlockChainService().invokeTransaction(transfer.db.id, transfer.toJSON().toString());
		} catch (WriteFailureException e) {
			e.printStackTrace();
		}
	}

	// 余额查询
	public BigDecimal queryMoney(User user)
	{
		user.db.lastTransfer = userMapper.selectLastTransfer(user.db.id);
		if (user.db.lastTransfer == 0) {
			createAccount(user.db.id);
			return BigDecimal.ZERO;
		}
		String s;
		try {
			s = new JSONObject(GetFabricManager.getBlockChainService().queryTransaction(user.db.lastTransfer)).getString("encryped_message");
		}
		catch (ReadFailureException e) {
			return null;
		}
		var transfer = Transfer.formJSON(new JSONObject(s));
		return user.db.id == transfer.db.dst ? transfer.dst_remain : transfer.src_remain;
	}

	// 充值
	public String investMoney(User dst, BigDecimal money)
	{
		if (true) { // 伪：判断用户银行卡里余额是否够用
			var dst_remain = queryMoney(dst);
			if (dst_remain == null) {
				return "请稍后再试";
			}
			accountMapper.insertTransfer(dst.db.id, 0);
			var transfer = new Transfer();
			transfer.db.id = accountMapper.getlastInsertId();
			transfer.db.dst = dst.db.id;
			transfer.db.src = 0;
			transfer.money = money;
			transfer.dst_remain = dst_remain.add(money);
			transfer.src_remain = null;
			userMapper.updateLastTransfer(dst.db.id, transfer.db.id);
			try {
				GetFabricManager.getBlockChainService().invokeTransaction(transfer.db.id, transfer.toJSON().toString());
			} catch (WriteFailureException e) {
				return "区块链错误";
			}
			return null;
		}
		return "银行卡余额不足";
	}

	// 提现
	public String withdrawMoney(User src, BigDecimal money)
	{
		var src_remain = queryMoney(src);
		if (src_remain == null) {
			return "请稍后再试";
		}
		if (src_remain.compareTo(money) >= 0)
		{
			accountMapper.insertTransfer(0, src.db.id);
			var transfer = new Transfer();
			transfer.db.id = accountMapper.getlastInsertId();
			transfer.db.dst = 0;
			transfer.db.src = src.db.id;
			transfer.money = money;
			transfer.dst_remain = null;
			transfer.src_remain = src_remain.subtract(money);
			userMapper.updateLastTransfer(src.db.id, transfer.db.id);
			try {
				GetFabricManager.getBlockChainService().invokeTransaction(transfer.db.id, transfer.toJSON().toString());
			} catch (WriteFailureException e) {
				return "区块链错误";
			}
			return null;
		}
		return "账户余额不足";
	}

	// 转账
	public String transferMoney(User dst, User src, BigDecimal money)
	{
		var src_remain = queryMoney(src);
		if (src_remain == null) {
			return "请稍后再试";
		}
		if (src_remain.compareTo(money) >= 0)
		{
			var dst_remain = queryMoney(dst);
			accountMapper.insertTransfer(dst.db.id, src.db.id);
			var transfer = new Transfer();
			transfer.db.id = accountMapper.getlastInsertId();
			transfer.db.dst = dst.db.id;
			transfer.db.src = src.db.id;
			transfer.money = money;
			transfer.dst_remain = dst_remain.add(money);
			transfer.src_remain = src_remain.subtract(money);
			userMapper.updateLastTransfer(dst.db.id, transfer.db.id);
			userMapper.updateLastTransfer(src.db.id, transfer.db.id);
			try {
				GetFabricManager.getBlockChainService().invokeTransaction(transfer.db.id, transfer.toJSON().toString());
			} catch (WriteFailureException e) {
				return "区块链错误";
			}
			return null;
		}
		return "账户余额不足";
	}
}
