package com.jt.manage.controller;

import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.manage.pojo.Item;
import com.jt.manage.pojo.ItemDesc;
import com.jt.manage.service.ItemService;
import com.jt.manage.vo.EasyUIItem;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/findAll")
	@ResponseBody  //将数据转化为JSON数据返回
	public List<Item> findAll(){
		
		return itemService.findAll();
	}
	
	
	//http://localhost:8091/item/query?page=1&rows=50
	//根据分页实现商品信息的查询
	@RequestMapping("/query")
	@ResponseBody
	public EasyUIItem findItemByPage
	(Integer page,Integer rows){
		
	return itemService.findItemByPage(page,rows);
	}
	
	
	/**
	 * @ResponseBody
	 * 使用该注解进行数据转化时如果返回值是对象
	 * 则以utf-8格式进行编码
	 * 如果返回值是String字符串,则以iso-8859-1格式编码
	 * public static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
	 * @param id
	 * @return
	 */
	//根据商品分类Id查询商品分类名称
	///item/cat/queryItemName
	@RequestMapping(value="/cat/queryItemName",
			produces="text/html;charset=utf-8")
	@ResponseBody
	public String findItemCatNameById(Long id){
		
		return itemService.findItemCatName(id);
	}
	
	
	
	//实现商品的新增
	@RequestMapping("/save")
	@ResponseBody
	public SysResult saveItem(Item item,String desc){
		try {
			itemService.saveItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"商品新增失败");
	}
	
	///item/update
	@RequestMapping("/update")
	@ResponseBody
	public SysResult updateItem(Item item,String desc){
		try {
			itemService.updateItem(item,desc);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品修改失败");
	}
	
	
	//id=1212,22,2222,3333
	@RequestMapping("/delete")
	@ResponseBody
	public SysResult deleteItems(Long[] ids){
		try {
			itemService.deleteItems(ids);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"删除失败");
	}
	
	
	@RequestMapping("/instock")
	@ResponseBody
	public SysResult instockItems(Long[] ids){
		try {
			int status = 2;	//下架操作
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"商品下架失败");
	}
	
	@RequestMapping("/reshelf")
	@ResponseBody
	public SysResult reshelfItems(Long[] ids){
		try {
			int status = 1;	//上架操作
			itemService.updateStatus(ids,status);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"商品上架失败");
	}
	
	//获取商品的详情信息
	@RequestMapping("/query/item/desc/{itemId}")
	@ResponseBody
	public SysResult findItemDescById(@PathVariable Long itemId){
		try {
			ItemDesc itemDesc = 
					itemService.findItemDescById(itemId);
			return SysResult.oK(itemDesc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return SysResult.build(201,"商品详情查询失败");
	}

}
