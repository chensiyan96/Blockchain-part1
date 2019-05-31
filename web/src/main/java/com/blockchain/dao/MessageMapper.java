package com.blockchain.dao;

import com.blockchain.model.Message;
import java.util.List;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into Message (Msg,CreateTime,PartyA,PartyB,Status) "
			+ "values(#{msg},#{createTime},#{partyA},#{partyB},0)")
	void insertMessage(Message c);

	@Select("select * from Message where Id = #{id}")
	Message getMessage(@Param("id")int id);

	@Select("select * from Mortgages where partyB = #{id}")
	List<Message> getMessages(@Param("id")int id);

	@Update("update Message set Status = #{status} where Id = #{id}")
	void updateStatus(@Param("status")int status,@Param("id")int id);
}
