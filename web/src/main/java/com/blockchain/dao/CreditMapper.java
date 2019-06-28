package com.blockchain.dao;

import com.blockchain.model.Credit;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface CreditMapper extends MapperBase
{
	@Insert("insert into Credit (Sid,Rank,Applied,Approved) "
			+ "values(#{sid},#{rank},#{applied},#{approved})")
	boolean insertCredit(Credit.DataBase credit);

	@Select("select * from Credit where Sid = #{sid}")
	Credit.DataBase getCreditById(@Param("sid")long sid);

	@Update("update Credit set Rank = #{rank}, Applied = #{applied}, Approved = #{approved} where Sid = #{sid}")
	void updateCreditInfo(@Param("sid")long sid, @Param("rank")String rank, @Param("applied") BigDecimal applied, @Param("approved")BigDecimal approved);
}
