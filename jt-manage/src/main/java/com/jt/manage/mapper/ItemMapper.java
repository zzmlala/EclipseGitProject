package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.Item;

public interface ItemMapper extends SysMapper<Item>{
	
	//查询全部的商品
	List<Item> findItemAll();
	
	//查询商品的记录总数
	int findItemCount();

	/**
	 * 说明:Mybatis中的接口不能实现多值传参.需要将
	 * 多值封装为单值.
	 * 1.封装为对象(user/item....)
	 * 2.封装为集合(array/list/set)
	 * 3.封装为Map<k,v>   
	 * @Param("start")int start 将数据封装为map
	 * @param start
	 * @param rows
	 * @return
	 */
	//进行分页查询
	List<Item> findItemBypage(@Param("start")int start,@Param("rows")Integer rows);
	
	//实现商品分类查询
	String findItemCatName(Long id);

	void updateStatus(@Param("ids")Long[] ids,@Param("status")int status);
	
	
	
	
	
}
