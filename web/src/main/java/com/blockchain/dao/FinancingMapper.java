package com.blockchain.dao;

import com.blockchain.model.Financing;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FinancingMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into `Financing`(Id,Sid,Cid,Mid,CreateTime,Status) "
			+ "values(#{id},#{sid},#{cid},#{mid},#{createTime},#{status})")
	long insertFinancing(Financing.DataBase fin);

	@Select("select * from `Financing` where Id = #{id}")
	Financing.DataBase getFinancingById(@Param("id") long id);

	@Select("select * from `Financing` where Sid = #{sid} and Status = #{status}")
	Financing.DataBase[] getFinancingBySidAndStatus(@Param("sid") long sid, @Param("status") byte status);

	@Select("select * from `Financing` where Cid = #{cid} and Status = #{status}")
	Financing.DataBase[] getFinancingByCidAndStatus(@Param("cid") long cid, @Param("status") byte status);

	@Select("select * from `Financing` where Mid = #{mid} and Status = #{status}")
	Financing.DataBase[] getFinancingByMidAndStatus(@Param("mid") long mid, @Param("status") byte status);

	@Update("update `Financing` set Status = #{status} where Id = #{id}")
	void updateFinancingState(@Param("id") long id, @Param("id") byte status);

	@Delete("delete from `Financing` where Id = #{id}")
	void deleteFinancingById(@Param("id") long id);
}
