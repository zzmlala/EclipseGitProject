package com.jt.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.Cart;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		String uri = "http://cart.jt.com/cart/query/"+userId;
		
		String jsonData = httpClient.doGet(uri);
		
		SysResult sysResult = null;
		try {
			sysResult = objectMapper.readValue(jsonData, SysResult.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Cart> cartList = (List<Cart>) sysResult.getData();
		
		return cartList;
	}

	@Override
	public void saveCart(Cart cart) {
		//定义uri
		String uri = "http://cart.jt.com/cart/save";
		
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId",cart.getUserId()+"");
		params.put("itemId", cart.getItemId()+"");
		params.put("itemTitle",cart.getItemTitle());
		params.put("itemImage", cart.getItemImage());
		params.put("itemPrice", cart.getItemPrice()+"");
		params.put("num",cart.getNum()+"");
		
		httpClient.doPost(uri, params);
	}

	@Override
	public void updateCartNum(Long userId, Long itemId, Integer num) {
		String uri = "http://cart.jt.com/cart/update/num/"+userId+"/"+itemId +"/"+num;
		
		httpClient.doGet(uri);
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		
		String uri = "http://cart.jt.com/cart/delete/"+userId+"/"+itemId;
		
		httpClient.doGet(uri);
	}
	
	
	
	
	
	
	
	
}
