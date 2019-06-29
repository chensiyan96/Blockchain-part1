package com.blockchain.dao;

import com.blockchain.model.Transfer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper
{
    @Options(useGeneratedKeys = true)
    @Insert("insert into `Transfer`(Dst,Src,Money,Remain) values (#{dst},#{src},#{money},#{remain})")
    boolean insertUser(@Param("dst") long dst, @Param("src") long src, @Param("money") long money, @Param("remain") long remain);

    @Select("select * from `Transfer` where Id = #{id} ")
    Transfer getTransferById(@Param("id") long id);

    @Select("select * from `Transfer` where dst = #{uid} or src = #{uid} ")
    Transfer[] getTransferByUid(@Param("uid") long uid);
}
