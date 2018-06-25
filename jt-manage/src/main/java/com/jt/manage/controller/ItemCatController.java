package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.manage.service.ItemCatService;
import com.jt.manage.vo.EasyUITree;
@Controller
@RequestMapping("/item/cat")
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	
	/**
	 * 实现商品分类目录查询
	 * 步骤:
	 * 	1.准备返回数据的vo对象
	 * 		{id:1,text:'目录名称',"state":"closed/open"}
	 * 	2.实现根据parentId查询数据
	 *  3.将数据按照格式进行封装
	 *  4.将数据转化为json串后返回
	 *  
	 *  @RequestParam
	 *  说明:springmvc接收参数时使用的,并且参数不一致时使用 
	 *  value=""  表示接收的参数
	 *  required=true 表示该参数必须传递
	 *  defaultValue="" 指定默认的数据
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<EasyUITree> findItemCatById(@RequestParam(value="id",required=true,defaultValue="0") Long parentId){
		
		return itemCatService.findItemCatById(parentId);
	}
}
