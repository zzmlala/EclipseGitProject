package com.jt.manage.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jt.manage.mapper.UserMapper;
import com.jt.manage.pojo.User;
@Service
public class UserServiceImpl implements UserService {
	//@Resource  javax.annotation.Resource;
	//说明:@Resource在单个项目中使用时没有问题的,但是
	//如果是分布式项目,那么可能会出现依赖注入为null的现象
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> findAll() {
		
		return userMapper.select(null);
		//return userMapper.findAll();
	}
	
	
	
	
	
	
	
	
}
