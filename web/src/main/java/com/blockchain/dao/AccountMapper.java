package com.blockchain.dao;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper
{

	@Insert("insert into UserAccounts (UserId,Money) "
			+ "values(#{uid},0)")
	void insertUserAccount(int uid);

	@Select("select Money from UserAccounts where UserId = #{uid}")
	BigDecimal getUserMoney(int uid);

	@Update("update UserAccounts set Money = Money + #{m} where UserId = #{uid}")
	void updateUserMoney(int uid, BigDecimal m);
}
