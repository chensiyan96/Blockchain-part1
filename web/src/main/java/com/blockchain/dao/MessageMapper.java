package com.blockchain.dao;

import com.blockchain.model.Message;
import com.blockchain.model.Mortgages;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MessageMapper
{
	@Options(useGeneratedKeys = true, keyProperty = "Id")
	@Insert("insert into Message (Msg,CreateTime,PartyA,PartyB,Status) "
			+ "values(#{msg},#{createTime},#{partyA},#{partyB},0)")
	void insertMessage(Message c);

	@Select("select * from Message where Id = #{id}")
	Message getMessage(int id);

	@Select("select * from Mortgages where partyB = #{id}")
	List<Message> getMessages(int id);

	@Update("update Message set Status = #{status} where Id = #{id}")
	void updateStatus(int status,int id);
}
