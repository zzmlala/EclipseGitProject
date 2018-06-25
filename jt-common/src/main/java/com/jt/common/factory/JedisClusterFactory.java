package com.jt.common.factory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JEdis  ~~~id:JEdis
 * Jedis  ~~~id:jedis
 * @author LYJ
 *
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>{
	/**
	 * spring中的工厂模式介绍
	 * 1.静态工厂
	 * 2.实例化工厂
	 * 3.spring工厂
	 */
	
	//@Autowired
	private JedisPoolConfig poolConfig;
	private Resource propertySource;
	private String redisNodePrefix; //redis.cluster
	
	
	//通过工厂对象获取jedisCluster对象
	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public Resource getPropertySource() {
		return propertySource;
	}

	public void setPropertySource(Resource propertySource) {
		this.propertySource = propertySource;
	}

	public String getRedisNodePrefix() {
		return redisNodePrefix;
	}

	public void setRedisNodePrefix(String redisNodePrefix) {
		this.redisNodePrefix = redisNodePrefix;
	}

	@Override
	public JedisCluster getObject() throws Exception {
		
		Set<HostAndPort> nodes = getNodes();
		JedisCluster cluster = new JedisCluster(nodes, poolConfig);
		
		return cluster;
	}
	
	//动态获取set集合
	public Set<HostAndPort> getNodes(){
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		try {
			//定义properties对象
			Properties properties = new Properties();
			
			//加载源文件
			properties.load(propertySource.getInputStream());
			
			//循环遍历配置文件中的key
			for (Object key : properties.keySet()) {
				
				String redisKey = (String) key;
				if(!redisKey.startsWith(redisNodePrefix)){
					//遍历的key不是redis的节点的key
					//System.out.println("获取无用数据:"+redisKey);
					continue;
				}
				
				//IP:端口  192.168.126.151:7008
				String redisNode = properties.getProperty(redisKey);
				
				String[] args = redisNode.split(":");
				
				HostAndPort hostAndPort = 
						new HostAndPort(args[0],Integer.parseInt(args[1]));
				set.add(hostAndPort);
				//System.out.println("获取有用数据:"+redisKey);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return set;
	}
	
	@Override
	public Class<?> getObjectType() {
		
		return JedisCluster.class;
	}

	@Override
	public boolean isSingleton() {
		/**
		 * 表示单例
		 * 单例模式中
		 * 1.尽量不要写成员变量
		 * 2.尽量不要使用单例模式中的属性（基本类型）
		 */
		return true;
	}
}
