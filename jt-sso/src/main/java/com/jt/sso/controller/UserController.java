package com.jt.sso.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.sso.pojo.User;
import com.jt.sso.service.UserService;
import com.sun.script.javascript.JSAdapter;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;

	//实现用户的校验
	@RequestMapping("/check/{param}/{type}")
	@ResponseBody
	public MappingJacksonValue findCheckUser(@PathVariable String param,@PathVariable int type,
			String callback){
		String cloumn = null;
		switch (type) {
			case 1:
				cloumn = "username";break;
			case 2:
				cloumn = "phone";	break;
			case 3:
				cloumn = "email";	break;
		}
		//select * from tb_user where username = param
		//根据提供提供的参数查询数据
		boolean flag = 
				userService.findCheckUser(param ,cloumn);
		//封装返回值信息
		MappingJacksonValue jacksonValue = 
		new MappingJacksonValue(SysResult.oK(flag));
		//设置回调方法
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			userService.saveUser(user);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201,"用户新增失败");
	}
	
	//实现用户的登陆操作
	@RequestMapping("/login")
	@ResponseBody
	public SysResult login(
			@RequestParam("u")String username,
			@RequestParam("p")String password){
		try {
			User user = new User();
			user.setUsername(username);
			user.setPassword(password);
			String ticket = userService.findUserByUP(user);
			
			if(ticket ==null){
				return SysResult.build(201,"用户名或密码错误");
			}
			return SysResult.oK(ticket);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return SysResult.build(201,"查询用户失败");
	}
	
	
	//http://sso.jt.com/user/query/" + _ticket,
	/**
	 * 通过ticket获取redis中的user数据
	 * @param ticket
	 * @return
	 */
	@RequestMapping("/query/{ticket}")
	@ResponseBody
	public MappingJacksonValue findUserByTicket(@PathVariable String ticket,String callback){
		//获取用户JSON数据
		String userJSON = jedisCluster.get(ticket);
		MappingJacksonValue jacksonValue = null;
		if(!StringUtils.isEmpty(userJSON)){
			//数据存在
			jacksonValue = new MappingJacksonValue(SysResult.oK(userJSON));
		}else{
			jacksonValue = new MappingJacksonValue(SysResult.build(201,"用户查询失败"));
		}
		
		jacksonValue.setJsonpFunction(callback);
		return jacksonValue;
	}
	
	
	
	
	
	
}
