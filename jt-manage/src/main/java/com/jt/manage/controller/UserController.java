package com.jt.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jt.manage.pojo.User;
import com.jt.manage.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	//url:localhost:8091/findAll 跳转到用户展现页面
	/**
	 * 扩展:
	 * 	问题1:model对象中的数据为什么页面中可以获取???
	 * 	说明:model对象中的数据最终保存到了Rquest域中
	 * @param model
	 * @return
	 */
	@RequestMapping("/findAll")
	public String findAll(Model model){
		//获取用户的信息
		List<User> userList = userService.findAll();
		
		model.addAttribute("userList", userList);
		//返回页面
		return "userList";
	}
}
