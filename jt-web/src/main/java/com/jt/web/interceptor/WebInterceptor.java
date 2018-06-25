package com.jt.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.web.pojo.User;
import com.jt.web.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class WebInterceptor implements HandlerInterceptor{
	
	@Autowired
	private JedisCluster jedisCluster;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * 实现思路:
	 *  1.通过request对象获取Cookie信息
	 *  	1.1 Cookie中有数据 则证明用户已经登陆成功
	 *  	1.2 Cookie中没有数据.则证明用户没有登陆.则跳转到登陆页面
	 *  2.如果用户已经登陆
	 *  	2.1 获取Cookie中ticket的值
	 *      2.2 从redis中查找用户json数据
	 *      	2.2.1 如果查询的数据为null.则应该跳转到登陆页面
	 *      	2.2.2 如果查询到用户数据.则将json转化为User对象.
	 *  3.未完待续
	 *      	
	 */
	@Override	   //业务处理前执行(controller代码执行前)
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String jt_ticket = null;
		
		Cookie[] cookies = request.getCookies();
		
		for (Cookie cookie : cookies) {
			
			if("JT_TICKET".equals(cookie.getName())){
				
				jt_ticket = cookie.getValue();
				break;
			}
		}
		
		//判断ticket是否 不为null;
		if(!StringUtils.isEmpty(jt_ticket)){
			
			//从缓存中获取userJSON数据
			String userJSON = jedisCluster.get(jt_ticket);
			
			if(!StringUtils.isEmpty(userJSON)){
				//表示userJSON数据不为空
				User user = 
				objectMapper.readValue(userJSON, User.class);
				
				/*如果将user信息存入域对象中,则每个Controller方法中
				必须添加域对象的参数.这样的写法不是特别好,代码复杂
				耦合性高
					解决方案: 使用ThreadLocal实现user对象共享
					本地线程变量作用:在当前线程内实现数据的共享.
				*/
				UserThreadLocal.set(user);
				return true; //表示让程序放行
			}	
		}
		
		//如果程序执行到这里,表示用户数据为空,则跳转到 登陆页面
		response.sendRedirect("/user/login.html");
		//表示用户的请求是否放行. 
		//false 必须添加路径的转向,否则程序卡死
		return false;
		
		
	}

	@Override //业务处理后,返回浏览器之前
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//返回浏览器时执行.
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//System.out.println("用户信息删除");
		UserThreadLocal.remove();
	}
}
