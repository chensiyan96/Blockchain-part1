package com.blockchain.dao;

import com.blockchain.model.Financing;
import com.blockchain.model.FinancingStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FinancingMapper
{

	@Options(useGeneratedKeys = true)
	@Insert("insert into Financing (Terms,CreateTime,PartyA,PartyB,Status,Mid,Aid) "
			+ "values(#{terms},#{createTime},#{partyA},#{partyB},#{status},#{mid},#{aid})")
	int insertFinancing(Financing c);


	@Select("select * from Financing where Id = #{id}")
	Financing getFinancing(@Param("id") int id);

	@Update("update Financing set Status = #{status} where Id = #{id}")
	void updateStatus(@Param("id") int id, @Param("status") FinancingStatus status);
}
