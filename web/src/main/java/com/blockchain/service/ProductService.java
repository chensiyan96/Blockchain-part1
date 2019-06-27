package com.blockchain.service;

import com.blockchain.dao.ProductMapper;
import com.blockchain.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService
{
    @Autowired
    private ProductMapper productMapper;

    public long insertProduct(Product product)
    {
        if (productMapper.insertProduct(product.db)) {
            return product.db.id = productMapper.getlastInsertId();
        }
        return 0;
    }

    public Product getProductById(long id)
    {
        var db = productMapper.getProductById(id);
        return db == null ? null : new Product(db);
    }

    public Product[] getAllProducts()
    {
        return constructProductArray(productMapper.getAllProducts());
    }

    public void updateProductInfo(Product product)
    {
        productMapper.updateProductInfo(product.db.id, product.db.name, product.db.days, product.db.rate, product.db.additional);
    }

    public void deleteProductById(long id)
    {
        productMapper.deleteProductById(id);
    }

    private static Product[] constructProductArray(Product.DataBase[] dbs)
    {
        var products = new Product[dbs.length];
        for (int i = 0; i < dbs.length; i++) {
            products[i] = new Product(dbs[i]);
        }
        return products;
    }
}
