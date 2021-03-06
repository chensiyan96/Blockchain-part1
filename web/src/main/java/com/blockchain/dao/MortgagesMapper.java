package com.blockchain.dao;

import com.blockchain.model.Mortgages;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MortgagesMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into Mortgages (Cid,Money,CreateTime,PartyA,PartyB,Status) "
			+ "values(#{cid},#{money},#{createTime},#{partyA},#{partyB},0)")
	void insertMortgage(Mortgages c);

	@Select("select * from Mortgages where Id = #{id}")
	Mortgages getMortgage(@Param("id")int id);

	@Select("select * from Mortgages where Cid = #{cid}")
	List<Mortgages> getMortgages(@Param("id")int cid);

	@Update("update Mortgages set Status = #{s} where Id = #{id}")
	void updateStatus(@Param("s")int status,@Param("id")int id);

}
