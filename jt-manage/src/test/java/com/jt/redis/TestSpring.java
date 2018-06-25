package com.jt.redis;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {
	
	@Test
	public void test01(){
		ApplicationContext context = 
				new ClassPathXmlApplicationContext("/spring/factory.xml");
		Calendar calendar1 = 
				(Calendar) context.getBean("calendar1");
		Calendar calendar2 = 
				(Calendar) context.getBean("calendar2");
		Calendar calendar3 = 
				(Calendar) context.getBean("calendar3");
		System.out.println("获取时间1:"+calendar1);
		System.out.println("获取时间2:"+calendar2);
		System.out.println("获取时间3:"+calendar3);
	}
}
