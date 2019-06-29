package com.blockchain.dao;

import com.blockchain.model.Financing;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
@Mapper
public interface FinancingMapper extends MapperBase
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into `Financing`(Sid,Cid,Mid,Money,Days,Rate,CreateTime,Status) "
			+ "values(#{sid},#{cid},#{mid},#{money},#{days},#{rate},#{createTime},#{status})")
	boolean insertFinancing(Financing.DataBase fin);

	@Select("select * from `Financing` where Id = #{id}")
	Financing.DataBase getFinancingById(@Param("id") long id);

	@Select("select * from `Financing` where Sid = #{sid}")
	Financing.DataBase[] getFinancingBySid(@Param("sid") long sid);

	@Select("select * from `Financing` where Cid = #{cid}")
	Financing.DataBase[] getFinancingByCid(@Param("cid") long cid);

	@Select("select * from `Financing` where Mid = #{mid}")
	Financing.DataBase[] getFinancingByMid(@Param("mid") long mid);

	@Select("select * from `Financing` where Sid = #{sid} and Status = #{status}")
	Financing.DataBase[] getFinancingBySidAndStatus(@Param("sid") long sid, @Param("status") byte status);

	@Select("select * from `Financing` where Cid = #{cid} and Status = #{status}")
	Financing.DataBase[] getFinancingByCidAndStatus(@Param("cid") long cid, @Param("status") byte status);

	@Select("select * from `Financing` where Mid = #{mid} and Status = #{status}")
	Financing.DataBase[] getFinancingByMidAndStatus(@Param("mid") long mid, @Param("status") byte status);

	@Select("select count(*) from `Financing` where Sid = #{sid} and Status = #{status}")
	int getFinancingCountBySidAndStatus(@Param("sid") long sid, @Param("status") byte status);

	@Select("select count(*) from `Financing` where Cid = #{cid} and Status = #{status}")
	int getFinancingCountByCidAndStatus(@Param("cid") long cid, @Param("status") byte status);

	@Select("select count(*) from `Financing` where Mid = #{mid} and Status = #{status}")
	int getFinancingCountByMidAndStatus(@Param("mid") long mid, @Param("status") byte status);

	@Update("update `Financing` set Status = #{status} where Id = #{id}")
	void updateFinancingStatus(@Param("id") long id, @Param("status") byte status);

	@Update("update `Financing` set PayTime = #{payTime} where Id = #{id}")
	void updateFinancingPayTime(@Param("id") long id, @Param("payTime") Timestamp payTime);

	@Update("update `Financing` set RepayTime = #{repayTime} where Id = #{id}")
	void updateFinancingRepayTime(@Param("id") long id, @Param("repayTime") Timestamp repayTime);
}
