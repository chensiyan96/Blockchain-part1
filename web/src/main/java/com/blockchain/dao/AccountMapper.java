package com.blockchain.dao;

import com.blockchain.model.Transfer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AccountMapper extends MapperBase
{
    @Options(useGeneratedKeys = true)
    @Insert("insert into `Transfer`(Dst,Src) values (#{dst},#{src})")
    boolean insertTransfer(@Param("dst") long dst, @Param("src") long src);

    @Select("select * from `Transfer` where Id = #{id} ")
    Transfer.DataBase getTransferById(@Param("id") long id);

    @Select("select * from `Transfer` where dst = #{uid} or src = #{uid} ")
    Transfer.DataBase[] getTransferByUid(@Param("uid") long uid);
}
