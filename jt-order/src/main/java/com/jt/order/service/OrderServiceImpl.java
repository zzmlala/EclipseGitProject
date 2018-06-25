package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;
import com.jt.order.pojo.Order;
import com.jt.order.pojo.OrderItem;
import com.jt.order.pojo.OrderShipping;
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	
	@Override
	public String saveOrder(Order order) {
		String orderId = order.getUserId()+""+System.currentTimeMillis();
		
		Date date = new Date();
		//1.入库order表
		order.setOrderId(orderId);
		order.setStatus(1); //表示未付款
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单表入库成功!!!");
		
		//入库订单物流信息
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(date);
		shipping.setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("订单物流入库成功!!!");
		
		//订单商品入库 insert into 表名(....字段名) value(....),(.....),(.....)
		List<OrderItem> orderItems = order.getOrderItems();
		//orderItemMapper.saveItems(orderItems,orderId);
		
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单入库全部成功!!");
		
		return orderId;
	}


	@Override
	public Order findOrderById(String orderId) {
		
		Order order = orderMapper.selectByPrimaryKey(orderId);
		
		//查询订单的物流信息
		OrderShipping shipping = orderShippingMapper.selectByPrimaryKey(orderId);
		
		//查询orderItem数据
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		List<OrderItem> orderItems = orderItemMapper.select(orderItem);
		
		order.setOrderShipping(shipping);
		order.setOrderItems(orderItems);
		return order;
	}

}
