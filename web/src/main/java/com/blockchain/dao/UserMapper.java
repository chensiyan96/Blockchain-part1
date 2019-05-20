package com.blockchain.dao;

import com.blockchain.model.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {
	@Select(" SELECT * FROM user WHERE id = #{uid} ")
	User getUserInfo(@Param("uid") int uid);

	@Update(" UPDATE user " +
			" SET name = #{name}, avatar = #{avatar}, phone = #{phone}" +
			" WHERE id = #{id} ")
	void updateUserInfo(User user);

	@Update(" UPDATE user SET password = #{password} WHERE id = #{userID} ")
	void updatePassword(@Param("userID") int userID, @Param("password") String password);

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO user (name,email,password,phone,avatar) VALUES (#{name},#{email},#{password},#{phone},#{avatar})")
	void insertUser(User user);
}