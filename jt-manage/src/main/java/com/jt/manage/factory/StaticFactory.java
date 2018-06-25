package com.jt.manage.factory;

import java.util.Calendar;

public class StaticFactory {
	
	//静态工作中必须有静态方法.否则配置文件不生效
	public static Calendar getCalendar(){
		
		return Calendar.getInstance();
	}
}
