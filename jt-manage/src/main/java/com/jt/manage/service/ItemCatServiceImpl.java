package com.jt.manage.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.RedisService;
import com.jt.common.vo.ItemCatData;
import com.jt.common.vo.ItemCatResult;
import com.jt.manage.mapper.ItemCatMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemCat;
import com.jt.manage.vo.EasyUITree;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import sun.net.www.content.text.plain;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private ItemCatMapper itemCatMapper;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	//@Autowired
	//private Jedis jedis;
	
	//注入redis集群工具类
	@Autowired
	private JedisCluster jedisCluster;
	
	//private RedisService redisService;
	
	
	//根据商品分类Id查询数据
	public List<ItemCat> findItemCatList(Long parentId){
		ItemCat itemCat = new ItemCat();
		itemCat.setParentId(parentId);
		List<ItemCat> itemCatList = itemCatMapper.select(itemCat);
		
		return itemCatList;
	}
	

	@Override
	public List<EasyUITree> findItemCatById(Long parentId) {
		
		List<ItemCat> itemCatList = findItemCatByCache(parentId);
		
		List<EasyUITree> treeList = new ArrayList<EasyUITree>();
		for (ItemCat cat : itemCatList) {
			EasyUITree tree = new EasyUITree();
			tree.setId(cat.getId());
			tree.setText(cat.getName());
			String result = cat.getIsParent() ? "closed" : "open";
			tree.setState(result);
			treeList.add(tree);
		}
		return treeList;
	}
	
	//通过缓存查询数据信息
	/*
	 * 1.先查询缓存数据
	 * 2.如果没有数据则查询数据库信息
	 * 		2.1将数据库数据保存到redis中 将数据转化为JSON串
	 * 3.如果查询的数据有结果
	 * 		3.1将json数据转化List集合信息之后返回		
	 */
	public List<ItemCat> findItemCatByCache(Long parentId){
		
		String key = "ITEM_CAT_"+parentId;
		String jsonData = jedisCluster.get(key);
		List<ItemCat> itemCatList = new ArrayList<ItemCat>();
		
		try {
			if(StringUtils.isEmpty(jsonData)){
				//数据为空,缓存中没有该数据
				itemCatList = findItemCatList(parentId);
				
				//将list集合转化为JOSN数据
				String jsonResult = 
				objectMapper.writeValueAsString(itemCatList);
				
				//将数据保存到redis中
				jedisCluster.set(key, jsonResult);
				System.out.println("第一次调用,数据存入缓存");
			}else{
				//缓存中有数据,将json数据转化为List集合信息
				ItemCat[] itemCats = objectMapper.readValue(jsonData,ItemCat[].class);
				itemCatList = Arrays.asList(itemCats);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemCatList;
	}

	
	/**
	 * 难点:
	 * 	1.数据如何查询???
	 *    解决方法:采用Map数据结构.进行数据的封装
	 *   
	 */
	@Override
	public ItemCatResult findItemCatResult() {
		ItemCat tempItemCat = new ItemCat();
		tempItemCat.setStatus(1);
		//1.查询全部的商品分类信息
		List<ItemCat> tempItemCatList = 
				itemCatMapper.select(tempItemCat);
		
		//2.定义商品分类的Map集合 主要作用层级结构封装
		Map<Long,List<ItemCat>> map = new HashMap<Long,List<ItemCat>>();
		
		//3.循环遍历实现商品分类的划分
		for (ItemCat itemCat : tempItemCatList) {
			
			if(map.containsKey(itemCat.getParentId())){
				//证明map中已经存在父级Id
				map.get(itemCat.getParentId()).add(itemCat);
			}else{
				//表示map中没有父级,我是第一个父级元素的子类
				List<ItemCat> pList = new ArrayList<ItemCat>();
				pList.add(itemCat);
				map.put(itemCat.getParentId(), pList);
			}
		}
		
		//4准备一级商品分类List集合  14
		List<ItemCatData> itemCatList1 = new ArrayList<ItemCatData>();
		
		//5.封装一级商品分类信息
		for (ItemCat itemCat1 : map.get(0L)) {
			ItemCatData itemCatData1 = new ItemCatData();
			itemCatData1.setUrl("/products/"+itemCat1.getId()+".html");
			itemCatData1.setName("<a href='"+itemCatData1.getUrl()+"'>"+itemCat1.getName()+"</a>");
			
			//准备二级商品分类集合信息
			List<ItemCatData> itemCatList2 = new ArrayList<ItemCatData>();
			
			for (ItemCat itemCat2 : map.get(itemCat1.getId())) {
				ItemCatData itemCatData2 = new ItemCatData();
				itemCatData2.setUrl("/products/"+itemCat2.getId());
				itemCatData2.setName(itemCat2.getName());
				
				//定义三级商品分类菜单
				List<String> itemCatList3 = new ArrayList<String>();
				for (ItemCat itemCat3 : map.get(itemCat2.getId())) {
					itemCatList3.add("/products/"+itemCat3.getId()+"|"+itemCat3.getName());
				}
				
				itemCatData2.setItems(itemCatList3);
				itemCatList2.add(itemCatData2);
			}
			
			itemCatData1.setItems(itemCatList2);
			itemCatList1.add(itemCatData1);
			if(itemCatList1.size()>13){
				break;
			}
		};
		
		ItemCatResult itemCatResult = new ItemCatResult();
		itemCatResult.setItemCats(itemCatList1);
		return itemCatResult;
	}
	
	/**
	 * 1.数据先从缓存中获取
	 * 2.如果有数据
	 * 		则利用工具类将json转化为对象
	 * 3.如果没有数据
	 * 		则第一次查询数据库,之后将数据转化为JSON数据保存到
	 * 		redis中.
	 * @return
	 */
	public ItemCatResult findItemCatByCache(){
		String key = "ITEM_CAT_ALL";
		String jsonData = jedisCluster.get(key);
		try {
			if(StringUtils.isEmpty(jsonData)){
				
				ItemCatResult result = findItemCatResult();
				
				String json = objectMapper.writeValueAsString(result);
				jedisCluster.set(key, json);
				return result;
			}else{
				//缓存中有数据
				ItemCatResult result = 
				objectMapper.readValue(jsonData,ItemCatResult.class);
				return result;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
}
