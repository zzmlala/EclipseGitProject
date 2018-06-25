package com.jt.web.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private HttpClientService httpClient;
	
	private static ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void saveUser(User user) {
		//1.定义uri
		String uri = "http://sso.jt.com/user/register";
		
		Map<String,String> params = new HashMap<String,String>();
		
		params.put("username", user.getUsername());
		params.put("password", user.getPassword());
		params.put("phone", user.getPhone());
		params.put("email", user.getEmail());
		
		//返回sysResultJSON数据
		String jsonData = 
				httpClient.doPost(uri, params);
		
		SysResult sysResult = null;
		try {
			sysResult = objectMapper.readValue(jsonData, SysResult.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(sysResult.getStatus() != 200){
			throw new RuntimeException();
		}
	}

	@Override
	public String findUserByUP(String username, String password) {
		//定义uri
		String uri = "http://sso.jt.com/user/login";
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("u", username);
		map.put("p", password);
		//返回的是sysResultJSON数据
		String jsonData = httpClient.doPost(uri,map);
		
		SysResult sysResult = null;
		
		try {
			sysResult = objectMapper.readValue(jsonData,SysResult.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		//开始解析参数
		if(sysResult.getStatus() !=200){
			
			throw new RuntimeException();
		}
		//返回秘钥	
		return (String) sysResult.getData();
	}
}





