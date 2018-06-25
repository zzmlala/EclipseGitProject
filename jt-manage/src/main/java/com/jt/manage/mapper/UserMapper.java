package com.jt.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jt.common.mapper.SysMapper;
import com.jt.manage.pojo.User;

public interface UserMapper extends SysMapper<User> {
	
	/**
	 * Mybatis中的接口的注解形式,在开发中很少遇到
	 * 简单的单表的sql可以使用注解形式完成
	 * 要求:
	 * 	1.注解形式和配置文件只能写一个
	 * @return
	 */
	//查询全部数据
	/*@Select("sql")
	@Insert("sql")
	@Update("sql")
	@Delete("sql")*/
	List<User> findAll();
	
	
	
	
	
	
	
	
}
