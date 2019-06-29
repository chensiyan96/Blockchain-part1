package com.blockchain.dao;

import com.blockchain.model.Credit;
import com.blockchain.model.CreditRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface CreditMapper extends MapperBase
{
	@Insert("insert into `Credit` (Sid,Rank,Applied,Approved) "
			+ "values(#{sid},#{rank},#{applied},#{approved})")
	boolean insertCredit(Credit.DataBase credit);

	@Select("select * from `Credit` where Sid = #{sid}")
	Credit.DataBase getCreditById(@Param("sid")long sid);

	@Update("update `Credit` set Rank = #{rank}, Applied = #{applied}, Approved = #{approved} where Sid = #{sid}")
	void updateCreditInfo(@Param("sid")long sid, @Param("rank")String rank, @Param("applied") BigDecimal applied, @Param("approved")BigDecimal approved);

	@Insert("insert into `CreditRecord` (Sid,Applied,Approved,CreateTime) "
			+ "values(#{sid},#{applied},#{approved},#{createTime})")
	boolean insertCreditRecord(CreditRecord credit);

	@Select("select * from `CreditRecord` where Sid = #{sid}")
	public CreditRecord[] getRecordCredit(@Param("sid")long sid);
}
