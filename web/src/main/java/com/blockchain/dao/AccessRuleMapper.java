package com.blockchain.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccessRuleMapper
{
    @Insert("insert into `AccessRule`(Id,Content) values (#{id},#{content})")
    long insertAccessRule(@Param("id") long id, @Param("content") String content);

    @Select("select Content from `AccessRule` where Id = #{id} ")
    String getAccessRuleById(@Param("id") long id);

    @Delete("delete from `AccessRule` where Id = #{id} ")
    void deleteAccessRuleById(@Param("id") long id);
}
