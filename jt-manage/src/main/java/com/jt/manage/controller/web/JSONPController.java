package com.jt.manage.controller.web;

import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.manage.pojo.User;

@Controller
public class JSONPController {
	
	//@RequestMapping("/web/testJSONP")
	//@ResponseBody
	/*public String testJSONP(String callback){
		
		String json = "{\"id\":\"1\",\"name\":\"tom\"}";
		
		return callback +"("+ json +")";
	}*/
	
	
	
	@RequestMapping("/web/testJSONP")
	@ResponseBody
	public MappingJacksonValue testJSONP(String callback){
		User user = new User();
		user.setId(1);
		user.setName("tomcat猫");
		//创建对象 添加返回值数据
		MappingJacksonValue jacksonValue =
				new MappingJacksonValue(user);
		//设置函数名称
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	
	
	
}
