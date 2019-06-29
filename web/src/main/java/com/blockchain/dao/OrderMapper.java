package com.blockchain.dao;

import com.blockchain.model.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OrderMapper extends MapperBase
{
    @Options(useGeneratedKeys = true)
    @Insert("insert into `Order`(Sid,Cid,Number,Money,CreateTime,Days,Status) "
            + "values(#{sid},#{cid},#{number},#{money},#{createTime},#{days},#{status})")
    boolean insertOrder(Order.DataBase order);

    @Select("select * from `Order` where Id = #{id}")
    Order.DataBase getOrderById(@Param("id") long id);

    @Select("select * from `Order` where Sid = #{sid}")
    Order.DataBase[] getOrderBySid(@Param("sid") long sid);

    @Select("select * from `Order` where Cid = #{cid}")
    Order.DataBase[] getOrderByCid(@Param("cid") long cid);

    @Select("select * from `Order` where Sid = #{sid} and Status = #{status}")
    Order.DataBase[] getOrderBySidAndStatus(@Param("sid") long sid, @Param("status") byte status);

    @Select("select * from `Order` where Cid = #{cid} and Status = #{status}")
    Order.DataBase[] getOrderByCidAndStatus(@Param("cid") long cid, @Param("status") byte status);

    @Select("select count(*) from `Order` where Sid = #{sid} and Status = #{status}")
    int getOrderCountBySidAndStatus(@Param("sid") long sid, @Param("status") byte status);

    @Select("select count(*) from `Order` where Cid = #{cid} and Status = #{status}")
    int getOrderCountByCidAndStatus(@Param("cid") long cid, @Param("status") byte status);

    @Update("update `Order` set Status = #{status} where Id = #{id}")
    void updateOrderState(@Param("id") long id, @Param("id") byte status);
}
