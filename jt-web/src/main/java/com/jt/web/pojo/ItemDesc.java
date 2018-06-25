package com.jt.web.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

import org.jboss.logging.Property;

import com.jt.common.po.BasePojo;
public class ItemDesc extends BasePojo{
	
	private Long itemId; //与商品表中的数据是一致的
	private String itemDesc;
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	
	
}
