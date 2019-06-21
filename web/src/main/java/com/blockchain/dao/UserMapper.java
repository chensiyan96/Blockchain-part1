package com.blockchain.dao;

import com.blockchain.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into User(Email,Name,PasswordHash,Role,Additional) "
			+ "values (#{email},#{name},#{passwordHash},#{role},#{additional})")
	long insertUser(User.DataBase user_db);

	@Select("select * from User where Id = #{id} ")
	User.DataBase getUserById(@Param("id") long id);

	@Select("select * from User where Email = #{email} ")
	User.DataBase getUserByEmail(@Param("email") String email);

	@Select("select * from User where Role = 'MoneyGiver' ")
	User.DataBase[] getAllMoneyGivers();

	@Update("update User " +
			"set Name = #{name}, PasswordHash = #{psw_hash}, Additional = #{additional} " +
			"where Id = #{id} ")
	void updateUserInfo(@Param("id") long id, @Param("name") String name, @Param("psw_hash") String psw_hash, @Param("additional") String additional);
}