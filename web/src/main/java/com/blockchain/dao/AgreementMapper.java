package com.blockchain.dao;

import com.blockchain.model.AgreeStatus;
import com.blockchain.model.Agreement;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AgreementMapper
{
	@Options(useGeneratedKeys = true, keyProperty = "Id")
	@Insert("insert into Agreements (Terms,CreateTime,PartyA,PartyB,Status,CreditId) "
			+ "values(#{terms},#{createTime},#{partyA},#{partyB},#{status},#{creditId})")
	int insertAgreement(Agreement c);


	@Select("select * from Agreements where Id = #{id}")
	Agreement  getAgreement(int id);

	@Update("update Agreements set Status = #{status} where Id = #{id}")
	void updateStatus(int id, AgreeStatus status);
}
