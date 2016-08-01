package com.thoughtworks.ketsu.domain.user;

import com.thoughtworks.ketsu.domain.order.Order;
import com.thoughtworks.ketsu.domain.product.Product;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.OrderMapper;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.ProductMapper;
import com.thoughtworks.ketsu.infrastructure.records.Record;
import com.thoughtworks.ketsu.web.jersey.Routes;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class User implements Record {

    @Inject
    OrderMapper orderMapper;

    @Inject
    ProductMapper productMapper;

    private int id;
    private String name;


    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Order createOrder(Map<String, Object> info) {
        info.put("userId", id);
        double totalPrice = 0d;
        for (Map<String, Object> item : (List<Map<String, Object>>)info.get("order_items")) {
            double amount = 0d;
            int quantity = Integer.valueOf(item.get("quantity").toString());
            double priceEach = productMapper.findById(Integer.valueOf(item.get("product_id").toString())).getPrice();
            amount = quantity * priceEach;
            item.put("amount", amount);
            totalPrice += amount;
        }
        info.put("totalPrice", totalPrice);
        orderMapper.save(info);
        Order order = orderMapper.findById(Integer.valueOf(info.get("id").toString()));
        return order;
    }

    public List<Order> findAllOrders() {
        return orderMapper.findAll();
    }

    public Optional<Order> findOrderById(int orderId) {
        return Optional.ofNullable(orderMapper.findById(orderId));
    }

    @Override
    public Map<String, Object> toJson(Routes routes) {
        return new HashMap<String, Object>() {{
            put("id", id);
            put("name", name);
            put("uri", routes.userUri(User.this));
        }};
    }

    @Override
    public Map<String, Object> toRefJson(Routes routes) {
        return toJson(routes);
    }
}
