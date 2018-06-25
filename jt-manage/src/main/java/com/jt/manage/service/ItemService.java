package com.jt.manage.service;

import java.util.List;

import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.vo.EasyUIItem;

public interface ItemService {
	
	List<Item> findAll();

	EasyUIItem findItemByPage(Integer page, Integer rows);

	String findItemCatName(Long id);
	//新增商品
	void saveItem(Item item, String desc);
	//修改商品
	void updateItem(Item item, String desc);

	void deleteItems(Long[] ids);

	void updateStatus(Long[] ids, int status);

	ItemDesc findItemDescById(Long itemId);

	Item findItemById(Long itemId);
}
