package com.blockchain.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MapperBase
{
    @Select("select last_insert_id()")
    long getlastInsertId();
}
