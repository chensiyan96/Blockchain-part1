package com.blockchain.dao;

import com.blockchain.model.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@Mapper
public interface ProductMapper extends MapperBase
{
    @Options(useGeneratedKeys = true)
    @Insert("insert into `Product`(Name,Days,Rate,Additional) "
            + "values (#{name},#{days},#{rate},#{additional})")
    boolean insertProduct(Product.DataBase product);

    @Select("select * from `Product` where Id = #{id} ")
    Product.DataBase getProductById(@Param("id") long id);

    @Select("select * from `Product` ")
    Product.DataBase[] getAllProducts();

    @Update("update `Product` set Name = #{name}, Days = #{days}, Rate = #{rate}, Additional = #{additional} where Id = #{id} ")
    void updateProductInfo(@Param("id") long id, @Param("name") String name, @Param("days") int days,
                           @Param("rate") BigDecimal rate, @Param("additional") String additional);

    @Delete("delete from `Product` where Id = #{id} ")
    void deleteProductById(@Param("id") long id);
}
