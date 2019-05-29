package com.blockchain.dao;

import com.blockchain.model.Financing;
import com.blockchain.model.FinancingStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FinancingMapper
{
	@Options(useGeneratedKeys = true, keyProperty = "Id")
	@Insert("insert into Financing (Terms,CreateTime,PartyA,PartyB,Status,Mid,Aid) "
			+ "values(#{terms},#{createTime},#{partyA},#{partyB},#{status},#{mid}),#{aid}")
	int insertFinancing(Financing c);


	@Select("select * from Financing where Id = #{id}")
	Financing  getFinancing(int id);

	@Update("update Financing set Status = #{status} where Id = #{id}")
	void updateStatus(int id, FinancingStatus status);
}
