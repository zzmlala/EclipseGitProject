package com.jt.manage.factory;

import java.util.Calendar;

public class NewInstanceFactory {
	
	//实例化工厂 相当于对象.方法()
	public Calendar getCalendar(){
		
		return Calendar.getInstance();
	}
}
