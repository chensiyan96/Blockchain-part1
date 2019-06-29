package com.blockchain.dao;

import com.blockchain.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends MapperBase
{
	@Options(useGeneratedKeys = true)
	@Insert("insert into `User`(Email,Name,PasswordHash,Role,Additional,Frozen,AutoPass,LastTransfer) " +
			"values (#{email},#{name},#{passwordHash},#{role},#{additional},#{frozen},#{autoPass},#{lastTransfer})")
	boolean insertUser(User.DataBase user);

	@Select("select * from `User` where Id = #{id} ")
	User.DataBase getUserById(@Param("id") long id);

	@Select("select * from `User` where Email = #{email} ")
	User.DataBase getUserByEmail(@Param("email") String email);

	@Select("select * from `User` where Role = 'Supplier' ")
	User.DataBase[] getAllSuppliers();

	@Select("select * from `User` where Role = 'CoreBusiness' ")
	User.DataBase[] getAllCoreBusinesses();

	@Select("select * from `User` where Role = 'MoneyGiver' ")
	User.DataBase[] getAllMoneyGivers();

	@Update("update `User` set Name = #{name}, PasswordHash = #{psw_hash}, Additional = #{additional}, " +
			"Frozen = #{frozen}, AutoPass = #{autoPass} where Id = #{id} ")
	void updateUserInfo(@Param("id") long id, @Param("name") String name, @Param("psw_hash") String psw_hash,
						@Param("additional") String additional, @Param("frozen") byte frozen, @Param("autoPass") byte autoPass);

	@Select("select LastTransfer from `User` where Id = #{id} ")
	long selectLastTransfer(@Param("id") long id);

	@Update("update `User` set LastTransfer = #{lastTransfer} where Id = #{id} ")
	void updateLastTransfer(@Param("id") long id, @Param("lastTransfer") long lastTransfer);
}