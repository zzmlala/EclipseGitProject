package com.jt.sso.mapper;

import org.apache.ibatis.annotations.Param;

import com.jt.common.mapper.SysMapper;
import com.jt.sso.pojo.User;

public interface UserMapper extends SysMapper<User>{

	int findCheckUser(@Param("param")String param,@Param("cloumn")String cloumn);
	
	//根据用户名和密码查询用户信息
	User findUserByUP(User user);
	
}
