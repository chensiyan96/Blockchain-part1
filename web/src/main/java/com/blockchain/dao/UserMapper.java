package com.blockchain.dao;

import com.blockchain.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("INSERT INTO User "
			+ "(Email,Name,PasswordHash,Role,Additional) "
			+ "VALUES (#{email},#{name},#{passwordHash},#{role},#{additional})")
	long insertUser(User user);

	@Select(" SELECT * FROM User WHERE Id = #{id} ")
	User getUserById(@Param("id") long id);

	@Select(" SELECT * FROM User WHERE Email = #{email} ")
	User getUserByEmail(@Param("email") String email);

	@Update(" UPDATE User " +
			" SET Name = #{name}, PasswordHash = #{psw_hash}, Additional = #{additional}" +
			" WHERE Id = #{id} ")
	void updateUserInfo(@Param("id") long id, @Param("name") String name, @Param("psw_hash") String psw_hash, @Param("additional") String additional);
}