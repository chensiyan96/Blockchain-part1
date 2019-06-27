package com.blockchain.dao;

import com.blockchain.model.Credit;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface CreditMapper extends MapperBase
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into Credit (Rank,Applied,Approved) "
			+ "values(#{rank},#{applied},#{approved})")
	boolean insertCredit(Credit.DataBase credit);

	@Select("select * from Credits where Id = #{id}")
	Credit.DataBase getCreditById(@Param("id")long id);

	@Update("update Credits set Rank = #{rank}, Applied = #{applied}, Approved = #{approved} where Id = #{id}")
	void updateCreditInfo(@Param("id")long id, @Param("rank")String rank, @Param("applied") BigDecimal applied, @Param("approved")BigDecimal approved);
}
