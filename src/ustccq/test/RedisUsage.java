package ustccq.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUsage {

	final static String key = "andrew.meowlomo";
	final static String hkey = "cq.meowlomo";
	final static String hkey2 = "cq.meowlomo_";
	final static String clusterKey = "andrew.meowlomo.cluster";
	final static String host = "10.0.30.20";
	final static String localHost = "10.0.100.180";
	final static String host177 = "10.0.100.178";
	final static int port177 = 7000;
	final static String pwd177 = "testing123";
	final static String field = "VMC";
	final static String field2 = "VMC_";
	public static void main(String[] args) {
		
//		LettuceSingleUsage();
//		LettuceLocalClusterUsage();
//		LettuceClusterUsage();
//		Jedis177Version5Usage();
		JedisLocalSingleUsage();
//		JedisLocalUsage();
	}

	private static String jedisHget(Jedis edis) {
		return edis.hget(hkey2, field);
	}
	
	private static void JedisLocalSingleUsage() {
		Jedis edis = new Jedis(host, 6380);
//		String result = edis.hget(hkey, field);
		Object tmp = jedisHget(edis);
		String sTmp = tmp.toString();
		System.out.println(sTmp);
	}
	
	private static void Jedis177Version5Usage() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(host177, port177));
		
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
	    jedisPoolConfig.setMaxTotal(100);
	    jedisPoolConfig.setMaxIdle(20);
	    jedisPoolConfig.setMaxWaitMillis(-1);
	    jedisPoolConfig.setTestOnBorrow(true);
		
		JedisCluster cluster = new JedisCluster(nodes, 1000, 1000, 3, pwd177, jedisPoolConfig);
//		JedisCluster cluster = new JedisCluster(nodes);
		
		System.out.println(cluster.exists(key));
		System.out.println(cluster.set(key, new Date().toString()));
		System.out.println(cluster.exists(key));
		System.out.println(cluster.hset(hkey, field, "傻儿军长"));
		System.out.println(cluster.hget(hkey, field));
		
		String hResult = cluster.hget(hkey, field2);
		System.out.println(cluster.hget(hkey, field2));
		System.out.println(cluster.hget(hkey2, field2));
		System.out.println(cluster.get(key));
//		System.out.println(cluster.del(key));
		
		cluster.close();
		cluster = null;
	}
	
	private static void JedisLocalUsage() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort(localHost, 7000));
		nodes.add(new HostAndPort(localHost, 7001));
		nodes.add(new HostAndPort(localHost, 7002));
		nodes.add(new HostAndPort(localHost, 7003));
		nodes.add(new HostAndPort(localHost, 7004));
		nodes.add(new HostAndPort(localHost, 7005));
//		nodes.add(new HostAndPort(host, 7006));
//		nodes.add(new HostAndPort(host, 7007));
		
		JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
	    jedisPoolConfig.setMaxTotal(100);
	    jedisPoolConfig.setMaxIdle(20);
	    jedisPoolConfig.setMaxWaitMillis(-1);
	    jedisPoolConfig.setTestOnBorrow(true);
		
		JedisCluster cluster = new JedisCluster(nodes, 1000, 3, jedisPoolConfig);
//		JedisCluster cluster = new JedisCluster(nodes);
		
		System.out.println(cluster.exists(key));
		System.out.println(cluster.set(key, new Date().toString()));
		System.out.println(cluster.exists(key));
//		System.out.println(cluster.del(key));
		
		cluster.close();
		cluster = null;
	}
	
	private static void LettuceLocalClusterUsage() {
		RedisURI redisUriMaster1 = RedisURI.create(localHost, 7000);
		RedisURI redisUriMaster2 = RedisURI.create(localHost, 7001);
		RedisURI redisUriMaster3 = RedisURI.create(localHost, 7002);
		RedisURI redisUriSlave1 = RedisURI.create(localHost, 7003);
		RedisURI redisUriSlave2 = RedisURI.create(localHost, 7004);
		RedisURI redisUriSlave3 = RedisURI.create(localHost, 7005);

		RedisClusterClient clusterClient = RedisClusterClient.create(redisUriMaster1);
//		RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(redisUriMaster1, redisUriSlave1
//				, redisUriMaster2, redisUriSlave2, redisUriMaster3, redisUriSlave3));
		
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
		RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
		System.out.println(syncCommands.set(key, String.valueOf(System.currentTimeMillis())));
		
		connection.close();
		clusterClient.shutdown();
	}
	
	private static void LettuceClusterUsage() {
		RedisURI redisUriMaster = RedisURI.create(host, 7000);
		RedisURI redisUriSlave1 = RedisURI.create(host, 7001);
		RedisURI redisUriSlave2 = RedisURI.create(host, 7002);
		RedisURI redisUriSlave3 = RedisURI.create(host, 7003);
		RedisURI redisUriSlave4 = RedisURI.create(host, 7004);
		RedisURI redisUriSlave5 = RedisURI.create(host, 7005);

		RedisClusterClient clusterClient = RedisClusterClient.create(redisUriMaster);
//		RedisClusterClient clusterClient = RedisClusterClient.create(Arrays.asList(redisUriMaster, redisUriSlave1
//				, redisUriSlave2, redisUriSlave3, redisUriSlave4, redisUriSlave5));
		
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();
		RedisAdvancedClusterCommands<String, String> syncCommands = connection.sync();
		System.out.println(syncCommands.set(key, String.valueOf(System.currentTimeMillis())));
		
		connection.close();
		clusterClient.shutdown();
	}
	
	private static RedisURI LettuceSingleUsage() {
		RedisURI redisUri = RedisURI.Builder.redis(host, 6380).build();//withPassword("authentication")
		RedisClient client = RedisClient.create(redisUri);
		StatefulRedisConnection<String, String> connection = client.connect();
		RedisCommands<String, String> syncCommands = connection.sync();
		System.out.println(syncCommands.set(clusterKey, String.valueOf(System.currentTimeMillis())));
		connection.close();
		client.shutdown();
		return redisUri;
	}
}
