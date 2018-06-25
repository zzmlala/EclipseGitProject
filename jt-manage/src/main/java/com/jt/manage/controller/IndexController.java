package com.jt.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/index")
	public String index(){
		
		return "index";
	}
	
    /**
     * 使用restFul结构实现页面的通用跳转
     * url':'/page/item-add
     * url':'/page/item-list
     * url':'/page/item-param-list
     * 
     * restFul结构  
     * 格式要求:
     * 	1.restFul结构中不需要写参数名称
     *  2.传递的参数使用"/"分割
     *  
     * get:localhost:8091/addUser?id=1&name=tom
     * restFul:localhost:8091/addUser/1/tom
     * 
     */
    @RequestMapping("/page/{module}")
    public String Module(@PathVariable String module){
    	
    	return module;
    }
}
