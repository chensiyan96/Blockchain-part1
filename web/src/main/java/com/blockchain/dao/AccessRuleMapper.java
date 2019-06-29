package com.blockchain.dao;

import com.blockchain.model.AccessRule;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccessRuleMapper extends MapperBase
{
    @Options(useGeneratedKeys = true)
    @Insert("insert into `AccessRule`(Content) values (#{content})")
    boolean insertAccessRule(@Param("content") String content);

    @Select("select * from `AccessRule` where Id = #{id} ")
    AccessRule getAccessRuleById(@Param("id") long id);

    @Select("select * from `AccessRule` ")
    AccessRule[] getAllAccessRules();

    @Update("update `AccessRule` set Content = #{content} where Id = #{id} ")
    void updateAccessRule(@Param("id") long id, @Param("content") String content);

    @Delete("delete from `AccessRule` where Id = #{id} ")
    void deleteAccessRule(@Param("id") long id);
}
