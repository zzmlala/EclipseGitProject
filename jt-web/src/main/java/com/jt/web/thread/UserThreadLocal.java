package com.jt.web.thread;

import com.jt.web.pojo.User;

/**
 * 如果threadLocal中存储的数据不唯一,则使用Map数据
 * 结构进行获取.
 * @author LYJ
 *
 */
public class UserThreadLocal {
	
	private static ThreadLocal<User> userThreadLocal 
	= new ThreadLocal<User>();
	
	public static void set(User user){
		
		userThreadLocal.set(user);
	}
	
	public static User get(){
		
		return userThreadLocal.get();
	}
	
	public static void remove(){
		//防治内存泄漏
		userThreadLocal.remove();
	}
}
