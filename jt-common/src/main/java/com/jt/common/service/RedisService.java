package com.jt.common.service;

import javax.tools.Diagnostic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {
	
	//哨兵的配置
	@Autowired(required=false)
	private JedisSentinelPool sentinelPool;
	
	public void set(String key,String value){
		Jedis jedis = sentinelPool.getResource();
		jedis.set(key, value);
		sentinelPool.returnResource(jedis);
	}
	
	public String get(String key){
		Jedis jedis = sentinelPool.getResource();
		String value = jedis.get(key);
		sentinelPool.returnResource(jedis);
		return value;
	}
	
	
	
	
	
	/*//只有当需要使用该对象时才注入
	@Autowired(required=false)
	private ShardedJedisPool jedisPool;
	
	public void set(String key,String value){
		
		ShardedJedis jedis = jedisPool.getResource();
		jedis.set(key, value);
		//将源还回池中
		jedisPool.returnResource(jedis);
	}
	
	public String get(String key){
	
		ShardedJedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		jedisPool.returnResource(jedis);
		return value;
	}
	
	//为redis指定超时时间
	public void set(String key,String value,int seconds){
		
		ShardedJedis jedis = jedisPool.getResource();
		
		//为数据添加超时时间 默认时间为秒
		jedis.setex(key, seconds, value);
		jedisPool.returnResource(jedis);
	}*/
}
