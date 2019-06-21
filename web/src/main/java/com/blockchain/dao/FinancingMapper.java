package com.blockchain.dao;

import com.blockchain.model.Financing;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FinancingMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into Financing(Id,Sid,Cid,Mid,CreateTime,State) "
			+ "values(#{id},#{sid},#{cid},#{mid},#{createTime},#{state})")
	long insertFinancing(Financing.DataBase fin);

	@Select("select * from Financing where Id = #{id}")
	Financing.DataBase getFinancingById(@Param("id") long id);

	@Select("select * from Financing where Sid = #{sid}")
	Financing.DataBase[] getFinancingBySupplier(@Param("sid") long sid);

	@Select("select * from Financing where Cid = #{cid}")
	Financing.DataBase[] getFinancingByCoreBusiness(@Param("cid") long cid);

	@Select("select * from Financing where Mid = #{mid}")
	Financing.DataBase[] getFinancingByMoneyGiver(@Param("mid") long mid);

	@Update("update Financing set State = #{state} where Id = #{id}")
	void updateFinancingState(@Param("id") long id, @Param("id") byte state);

	@Delete("delete from Financing where Id = #{id}")
	void deleteFinancingById(@Param("id") long id);
}
