package com.jt.sso.service;

import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.sso.mapper.UserMapper;
import com.jt.sso.pojo.User;

import redis.clients.jedis.JedisCluster;
import sun.security.provider.MD5;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	//校验用户名信息是否存在
	@Override
	public boolean findCheckUser(String param, String cloumn) {
		
		int count = userMapper.findCheckUser(param,cloumn);
		
		//如果存在返回true,如果不存在 返回false
		return count == 0 ? false : true;
	}

	//封装user入库数据 需要对密码进行加密处理
	@Override
	public void saveUser(User user) {
		String md5PassWord = DigestUtils.md5Hex(user.getPassword());
		user.setPassword(md5PassWord); //将密码进行加密处理
		user.setEmail(user.getPhone());//使用电话代替email使数据库不报错
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		
		userMapper.insert(user);
	}

	
	/**
	 * 1.判断用户名和密码是否正确
	 * 2.如果不正确则抛出异常即可
	 * 3.如果用户名和密码正确,则生成秘钥/将用户信息转化为
	 *  JSON数据,保存到redis中 设定超时时间2天
	 */
	@Override
	public String findUserByUP(User user) {
		
		//将密码加密处理
		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		User userDB = userMapper.findUserByUP(user);
		
		if(userDB == null){
			//证明用户名和密码错误
			//throw new RuntimeException();
			return null;
		}
		
		//用户名和密码正确 生成秘钥
		String ticket = "JT_TICKET_"+System.currentTimeMillis() + user.getUsername();
		ticket = DigestUtils.md5Hex(ticket);
		
		String userJSON = null;
		try {
			userJSON = objectMapper.writeValueAsString(userDB);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		//将数据保存redis中
		jedisCluster.setex(ticket,60*60*48,userJSON);
		return ticket;
	}

}
