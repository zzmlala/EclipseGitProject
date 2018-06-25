package com.jt.test;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TestHttpClient {
	
	//get请求
	@Test
	public void test01() throws ClientProtocolException, IOException{
		
		//1.创建HTTPclient对象
		CloseableHttpClient client = 
				HttpClients.createDefault();
		//2.定义请求路径
		String uri = "https://cds.3.cn/hotwords/get?callback=jQuery5602376&cate=9987%2C653%2C655&_=1528880572046";
		//3.定义请求的类型
		HttpGet httpGet = new HttpGet(uri);
		//4.获取请求的结果
		CloseableHttpResponse httpResponse = 
		client.execute(httpGet);
		
		//5.获取响应结果
		String result = 
				EntityUtils.toString(httpResponse.getEntity());
		
		System.out.println(result);
	}
	
	//post请求
	@Test
	public void testPost() throws ClientProtocolException, IOException{
		CloseableHttpClient client = 
				HttpClients.createDefault();
		
		String uri = "https://item.jd.com/6138112.html";
		
		HttpPost httpPost = new HttpPost(uri);
		
		CloseableHttpResponse response = 
		client.execute(httpPost);
		
		//判断请求是否正确返回
		if (response.getStatusLine().getStatusCode() == 200) {
			
			String result = EntityUtils.toString(response.getEntity());
			System.out.println(result);
		}
	}
	
	
	
	
	
	
	
}
