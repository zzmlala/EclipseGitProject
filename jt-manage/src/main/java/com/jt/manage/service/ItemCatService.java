package com.jt.manage.service;

import java.util.List;

import com.jt.common.vo.ItemCatResult;
import com.jt.manage.vo.EasyUITree;

public interface ItemCatService {
	//根据商品parentid进行查询
	List<EasyUITree> findItemCatById(Long parentId);
	
	//查询全部的三级商品分类
	ItemCatResult findItemCatResult();
	
	//实现缓存查询三级商品分类
	ItemCatResult findItemCatByCache();
}
