package com.jt.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class TestRedis {
	
	/**
	 * 1.获取远程redisIP地址   192.168.126.151
	 * 2.获取redis的端口号	6379
	 */
	@Test
	public void test01(){
		
		//创建Jedis对象
		Jedis jedis = new Jedis("192.168.126.151", 6379);
		
		jedis.set("1801班","1801");
		
		System.out.println("获取redis的数据:"+jedis.get("1801班"));
	}
	
	//测试分片操作
	@Test
	public void test02(){
		
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		
		//定义shardinfo对象
		JedisShardInfo info1 = new JedisShardInfo("192.168.126.151",6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.126.151",6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.126.151",6381);
		
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		
		
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("name", "tomcat猫");
		System.out.println("获取redis的数据:"+shardedJedis.get("name"));
	}
	
	
	//通过连接池操作jedis
	@Test
	public void test03(){
		//2.创建连接池配置文件,定义池的大小
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1000);	//定义最大连接数
		poolConfig.setMaxIdle(50);      //最大的空间连接
		poolConfig.setTestOnBorrow(true);
		//使用前先测试是否正确,如果有问题则重启获取新的连接
		
		//3定义分片的对象
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		
		//定义shardinfo对象
		JedisShardInfo info1 = new JedisShardInfo("192.168.126.151",6379);
		JedisShardInfo info2 = new JedisShardInfo("192.168.126.151",6380);
		JedisShardInfo info3 = new JedisShardInfo("192.168.126.151",6381);
		
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		
		//1.创建连接池对象
		ShardedJedisPool jedisPool = 
				new ShardedJedisPool(poolConfig, shards);
		
		//4.获取redis链接
		ShardedJedis shardedJedis = jedisPool.getResource();
		
		shardedJedis.set("1801", "redis分片高级应用");
		
		System.out.println("获取缓存数据:"+shardedJedis.get("1801"));;
	}
	
	@Test
	public void test04(){
		System.out.println(new HostAndPort("192.168.126.151",26379));
		
		//连接哨兵的IP+端口
		Set<String> sentinels = new HashSet<String>();
		sentinels.add("192.168.126.151:26379");
		sentinels.add("192.168.126.151:26380");
		sentinels.add("192.168.126.151:26381");
		
		JedisSentinelPool pool = 
					new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("name", "tomcat猫");
		System.out.println("成功获取redis数据:"+jedis.get("name"));
	}
	

	
	
}
