package com.blockchain.dao;

import com.blockchain.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
	@Select(" SELECT * FROM user WHERE id = #{uid} ")
	User getUserInfo(@Param("uid") int uid);

	@Select(" SELECT * FROM user WHERE id = #{email} ")
	User getUserInfoByEmail(@Param("email") String email);

	@Update(" UPDATE user " +
			" SET name = #{name}, avatar = #{avatar}, phone = #{phone}" +
			" WHERE id = #{id} ")
	void updateUserInfo(User user);

	@Update(" UPDATE user SET password = #{password} WHERE id = #{userID} ")
	void updatePassword(@Param("userID") int userID, @Param("password") String password);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO user (name,email,password,phone,avatar) VALUES (#{name},#{email},#{password},#{phone},#{avatar})")
	void insertUser(User user);

	@Select(" SELECT count(*) from user where email = #{email}")
	int isEmailExist(@Param("email") String email);

	@Select(" SELECT id from user where email = #{email} and password = #{psw}")
	int verifyUser(@Param("email") String email,@Param("psw") String psw);
}