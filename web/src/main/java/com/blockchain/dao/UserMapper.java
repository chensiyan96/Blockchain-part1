package com.blockchain.dao;

import com.blockchain.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper
{

	@Select(" SELECT * FROM users WHERE Id = #{uid} ")
	User getUserInfo(@Param("uid") int uid);

	@Select(" SELECT * FROM users WHERE NormalizedEmail = #{email} ")
	User getUserInfoByEmail(@Param("email") String email);

	@Update(" UPDATE Users " +
			" SET CompanyName = #{name}, Profile = #{profile}" +
			" WHERE Id = #{uid} ")
	void updateUserInfo(@Param("name") String name, @Param("profile") String profile,
			@Param("uid") int uid);

	@Update(" UPDATE Users "
			+ "SET PasswordHash = #{password} "
			+ "WHERE Id = #{uid} ")
	void updatePassword(@Param("uid") int uid, @Param("password") String password);

	@Options(useGeneratedKeys = true)
	@Insert("INSERT INTO Users "
			+ "(PasswordHash,Email,NormalizedEmail,CompanyName,Profile,Role) "
			+ "VALUES (#{passwordHash},#{email},#{normalizedEmail},#{companyName},#{profile},#{role})")
	int insertUser(User user);

	@Select(" SELECT count(*) from Users where NormalizedEmail = #{email}")
	int isEmailExist(@Param("email") String email);

	@Select(" SELECT Id from Users where NormalizedEmail = #{email} and PasswordHash = #{psw}")
	int verifyUser(@Param("email") String email, @Param("psw") String psw);
}