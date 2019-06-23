package com.blockchain.service;

import com.blockchain.dao.OrderMapper;
import com.blockchain.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService
{
    @Autowired
    private OrderMapper orderMapper;

    public long insertOrder(Order order)
    {
        return order.db.id = orderMapper.insertOrder(order.db);
    }

    public Order getOrderByID(long id)
    {
        var db = orderMapper.getOrderById(id);
        return db == null ? null : new Order(db);
    }

    public void updateOrderStatus(Order order)
    {
        orderMapper.updateOrderState(order.db.id, order.db.status);
    }

    public Order[] getOrderBySidAndStatus(long sid, byte status)
    {
        return constructOrderArray(orderMapper.getOrderBySidAndStatus(sid, status));
    }

    public Order[] getOrderByCidAndStatus(long cid, byte status)
    {
        return constructOrderArray(orderMapper.getOrderByCidAndStatus(cid, status));
    }

    private static Order[] constructOrderArray(Order.DataBase[] dbs)
    {
        var orders = new Order[dbs.length];
        for (int i = 0; i < dbs.length; i++) {
            orders[i] = new Order(dbs[i]);
        }
        return orders;
    }
}
