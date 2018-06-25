package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;
import com.jt.web.pojo.Order;
import com.jt.web.service.CartService;
import com.jt.web.service.OrderService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	//http://www.jt.com/order/create.html
	@RequestMapping("/create")
	public String create(Model model){
		Long userId = UserThreadLocal.get().getId();
		//获取用户的购物信息
		List<Cart> carts = cartService.findCartByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}

	@RequestMapping("/submit")
	@ResponseBody
	public SysResult saveOrder(Order order){
		try {
			//获取userId
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			String orderId = orderService.saveOrder(order);
			
			return SysResult.oK(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"订单入库失败");
	}
	
	//根据orderId查询数据库
	@RequestMapping("/success")
	public String findOrderById(String id,Model model){
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	
	
}
