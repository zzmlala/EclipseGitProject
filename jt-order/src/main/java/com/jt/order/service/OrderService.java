package com.jt.order.service;

import com.jt.order.pojo.Order;

public interface OrderService {
	
	public String saveOrder(Order order);

	public Order findOrderById(String orderId);
}
