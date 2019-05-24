package com.blockchain.dao;

import com.blockchain.model.Credit;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface CreditMapper
{
	@Options(useGeneratedKeys = true, keyProperty = "Id")
	@Insert("insert into Credits (Money,CreateTime,Deadline,PartyA,PartyB,Status) "
			+ "values(#{money},#{createTime},#{deadline},#{partyA},#{partyB},0)")
	void insertCredit(Credit c);

	@Select("select * from Credits where Id = #{id}")
	Credit getCredit(@Param("id")int id);

	@Update("update Credits set Status = #{s} where UserId = #{id}")
	void updateStatus(@Param("s")int status,@Param("id")int id);

	@Update("update Credits set PartyB = #{PartyB} where UserId = #{id}")
	void updatePartyB(@Param("PartyB")int PartyB,@Param("id")int id);
}
