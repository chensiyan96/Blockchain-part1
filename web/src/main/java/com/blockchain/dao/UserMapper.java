package com.blockchain.dao;

import com.blockchain.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper
{
	@Options(useGeneratedKeys = true)
	@Insert("INSERT INTO Users "
			+ "(Email,CompanyName,PasswordHash,Role,Profile) "
			+ "VALUES (#{email},#{companyName},#{passwordHash},#{role},#{profile})")
	int insertUser(User user);

	@Select(" SELECT * FROM users WHERE Id = #{id} ")
	User getUserById(@Param("id") int id);

	@Select(" SELECT * FROM users WHERE Email = #{email} ")
	User getUserByEmail(@Param("email") String email);

	@Update(" UPDATE Users " +
			" SET CompanyName = #{name}, Profile = #{profile}" +
			" WHERE Id = #{id} ")
	void updateUserInfo(@Param("id") int id, @Param("name") String name, @Param("profile") String profile);

	@Update(" UPDATE Users "
			+ "SET PasswordHash = #{password} "
			+ "WHERE Id = #{id} ")
	void updatePassword(@Param("id") int id, @Param("password") String password);

	@Select(" SELECT count(*) from Users where email = #{email}")
	int isEmailExist(@Param("email") String email);

	@Select(" SELECT Id from Users where email = #{email} and PasswordHash = #{psw}")
	int verifyUser(@Param("email") String email, @Param("psw") String psw);
}