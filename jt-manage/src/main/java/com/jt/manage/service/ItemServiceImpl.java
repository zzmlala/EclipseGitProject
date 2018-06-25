package com.jt.manage.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.common.vo.EasyUIResult;
import com.jt.manage.mapper.ItemDescMapper;
import com.jt.manage.mapper.ItemMapper;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.vo.EasyUIItem;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private ItemDescMapper itemDescMapper;
	
	@Override
	public List<Item> findAll() {
		
		return itemMapper.findItemAll();
	}

	@Override
	public EasyUIItem findItemByPage(Integer page, Integer rows) {
		//1.获取记录的总数
		int total = itemMapper.findItemCount();
		
		/**
		 * select * from tb_item order by updated desc  limit 0,20    第1页
		   select * from tb_item order by updated desc  limit 20,20  第2页
           select * from tb_item order by updated desc  limit 40,20 第3页
		 */
		int start = (page - 1) * rows ; //起始页数
		List<Item> itemList = 
				itemMapper.findItemBypage(start,rows);
		
		EasyUIItem easyUIItem = new EasyUIItem();
		easyUIItem.setTotal(total);
		easyUIItem.setRows(itemList);
		
		return easyUIItem;
	}

	@Override
	public String findItemCatName(Long id) {
		
		return itemMapper.findItemCatName(id);
	}
	
	/**
	 * 补全item数据 
	 * 1.状态码 1 表示正常
	 * 2.创建时间/更新时间
	 */
	@Override
	public void saveItem(Item item,String desc) {
		 item.setStatus(1);//表示正常
		 item.setCreated(new Date());
		 item.setUpdated(item.getCreated());
		 itemMapper.insert(item);
		 
		 ItemDesc itemDesc = new ItemDesc();
		 //根据通用Mapper实现数据自动查询
		 itemDesc.setItemId(item.getId());
		 itemDesc.setItemDesc(desc);
		 itemDesc.setCreated(item.getCreated());
		 itemDesc.setUpdated(item.getCreated());
		 itemDescMapper.insert(itemDesc);
		 
	}

	@Override
	public void updateItem(Item item,String desc) {
		item.setUpdated(new Date());
		itemMapper.updateByPrimaryKeySelective(item);
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
	}

	@Override
	public void deleteItems(Long[] ids) {
		//关联删除
		itemDescMapper.deleteByIDS(ids);
		itemMapper.deleteByIDS(ids);
	}

	@Override
	public void updateStatus(Long[] ids, int status) {
		/*	效率低
		 * for (Long id : ids) {
			Item item = new Item();
			item.setId(id);
			item.setStatus(status);
			itemMapper.updateByPrimaryKeySelective(item);
		}*/
		
		itemMapper.updateStatus(ids,status);
		
	}

	@Override
	public ItemDesc findItemDescById(Long itemId) {
		
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

	@Override
	public Item findItemById(Long itemId) {
		
		return itemMapper.selectByPrimaryKey(itemId);
	}

	
	
	
	
	
	
	
	
	
}
