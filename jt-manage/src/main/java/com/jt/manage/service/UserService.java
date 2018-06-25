package com.jt.manage.service;

import java.util.List;

import com.jt.manage.pojo.User;

public interface UserService {
	
	//查询全部的用户信息
	List<User> findAll();
}
