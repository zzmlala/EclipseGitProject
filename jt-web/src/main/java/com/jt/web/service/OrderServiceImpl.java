package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpClientConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Order;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static ObjectMapper objectMapper= new ObjectMapper();

	@Override
	public String saveOrder(Order order) {
		String orderId = null;
		try {
			String orderJSON = 
					objectMapper.writeValueAsString(order);
			String uri = "http://order.jt.com/order/create";
			Map<String,String> params = new HashMap<String, String>();
			params.put("orderJSON", orderJSON);
			//获取后台返回值json数据
			String jsonData = httpClient.doPost(uri,params);
			
			SysResult sysResult = 
					objectMapper.readValue(jsonData, SysResult.class);
			
			if(sysResult.getStatus() !=200){
				//后台操作有误
				throw new RuntimeException();
			}
			orderId = (String) sysResult.getData();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
		String uri = "http://order.jt.com/order/query/"+id;
		String orderJSON = httpClient.doGet(uri);
		Order order = null;
		try {
			order = objectMapper.readValue(orderJSON, Order.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}
}
