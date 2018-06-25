package com.jt.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Item findItemById(Long itemId) {
		//1.定义远程访问的uri
		String uri = "http://manage.jt.com/web/item/findItemById/"+itemId;
		//2.发起请求获取远程服务端数据
		String jsonData = httpClient.doGet(uri);
		Item item = null;
		try {
			item = objectMapper.readValue(jsonData, Item.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		//1.定义远程访问的uri
		String uri = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		//2.发起请求获取远程服务端数据
		String jsonData = httpClient.doGet(uri);
		ItemDesc itemDesc = null;
		try {
			itemDesc = objectMapper.readValue(jsonData, ItemDesc.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
}
