package com.blockchain.dao;

import com.blockchain.model.AgreeStatus;
import com.blockchain.model.Agreement;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgreementMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into Agreements (Terms,CreateTime,PartyA,PartyB,Status,CreditId) "
			+ "values(#{terms},#{createTime},#{partyA},#{partyB},#{status},#{creditId})")
	int insertAgreement(Agreement c);


	@Select("select * from Agreements where Id = #{id}")
	Agreement getAgreement(@Param("id")int id);

	@Update("update Agreements set Status = #{status} where Id = #{id}")
	void updateStatus(@Param("id")int id, @Param("status")AgreeStatus status);
}
