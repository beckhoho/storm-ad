package com.watchme.ad.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 *
 */
public class JedisServer {
	private static Logger logger = LoggerFactory.getLogger(JedisServer.class);
	private static JedisPool pool;

	private static String host = Global.getConfig("redis.host");
	private static int port = Integer.parseInt(Global.getConfig("redis.port"));
	private static int timeout = Integer.parseInt(Global.getConfig("redis.timeout"));

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(100);
		config.setMaxIdle(20);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		pool = new JedisPool(config, host, port, timeout);// 线程数量限制，IP地址，端口，超时时间

	}

	/**
	 * 注意： 作为一个key value存在，很多开发者自然的使用set/get方式来使用Redis，实际上这并不是最优化的使用方法。
	 * 尤其在未启用VM情况下，Redis全部数据需要放入内存，节约内存尤其重要。
	 * 假如一个key-value单元需要最小占用512字节，即使只存一个字节也占了512字节。
	 * 这时候就有一个设计模式，可以把key复用，几个key-value放入一个key中，value再作为一个set存入，
	 * 这样同样512字节就会存放10-100倍的容量。 用于存储多个key-value的值，比如可以存储好多的person Object
	 * 例子：>redis-cli 存储：redis 127.0.0.1:6379> hset personhash personId
	 * personObject 获得：redis 127.0.0.1:6379> hget personhash personId
	 * (就可以获得当前personId对应的person对象)
	 * 
	 * @param key
	 *            hashset key
	 * @param field
	 *            相当于personId
	 * @param value
	 *            person Object
	 */
	public static void hsetItem(int DBIndex, String key, String field, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.hset(key.getBytes(), field.getBytes(), value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	public static byte[] hgetItem(int DBIndex, String key, String field) {
		Jedis jedis = null;
		byte[] value = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			value = jedis.hget(key.getBytes(), field.getBytes());
			// jedis.hgetAll(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * @param key
	 * @param value
	 * @param seconds
	 *            有效时间 秒为单位 0为永久有效
	 */
	public static void setItem(int DBIndex, String key, byte[] value, int seconds) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			if (seconds == 0) {
				jedis.set(key.getBytes(), value);
			} else {
				jedis.setex(key.getBytes(), seconds, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	public static void rpush(int DBIndex, String key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.rpush(key.getBytes(), value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 删除
	 * 
	 * @param keys
	 */
	public static void del(int DBIndex, String... keys) {
		Jedis jedis = null;
		if (keys != null) {
			try {
				jedis = pool.getResource();
				jedis.connect();
				jedis.select(DBIndex);
				jedis.del(keys);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (jedis != null)
					pool.returnResource(jedis);
			}
		}
	}

	/**
	 * 头部添加元素
	 * 
	 * @param key
	 * @param value
	 */
	public static void lpushToList(int DBIndex, String key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.lpush(key.getBytes(), value);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	/**
	 * 返回List
	 * 
	 * @param key
	 * @param value
	 */
	public static List<byte[]> lrangeFromList(int DBIndex, String key, int start, int end) {
		Jedis jedis = null;
		List<byte[]> list = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			list = jedis.lrange(key.getBytes(), start, end);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return list;
	}

	/**
	 * 
	 * @param key
	 *            key
	 * @param member
	 *            存储的value
	 * @param score
	 *            排序字段 一般为objecId
	 */
	public static void addItemToSortSet(int DBIndex, String key, byte[] member, double score) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			jedis.zadd(key.getBytes(), score, member);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	public static void addListToSortSet(int DBIndex, String key, List<byte[]> list, List<Double> scores) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			if (list != null && !list.isEmpty() && scores != null && !scores.isEmpty()
					&& list.size() == scores.size()) {
				for (int i = 0; i < list.size(); i++) {
					jedis.zadd(key.getBytes(), scores.get(i), list.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
	}

	public static List<byte[]> getFromSortSet(int DBIndex, String key, int start, int end, String orderStatus) {
		Jedis jedis = null;
		List<byte[]> list = new ArrayList<byte[]>();
		Set<byte[]> set = new TreeSet<byte[]>();
		try {
			jedis = pool.getResource();
			jedis.connect();
			jedis.select(DBIndex);
			if (orderStatus.equals("DESC")) {
				set = jedis.zrevrange(key.getBytes(), start, end);
			} else {
				set = jedis.zrange(key.getBytes(), start, end);
			}
			if (set != null && !set.isEmpty()) {
				for (byte[] b : set) {
					list.add(b);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}
		return list;
	}

	public static byte[] getItem(int DBIndex, String key) {
		Jedis jedis = null;
		byte[] s = null;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			s = jedis.get(key.getBytes());
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);

		}

	}

	public static boolean exists(int DBIndex, String key) {
		Jedis jedis = null;
		boolean s = false;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			s = jedis.exists(key.getBytes());
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);

		}

	}

	public static byte[] rpop(int DBIndex, byte[] key) {
		Jedis jedis = null;
		byte[] s = null;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			s = jedis.rpop(key);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);

		}

	}

	public static void delItem(int DBIndex, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			jedis.del(key.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

	public static long getIncrement(int DBIndex, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			return jedis.incr(key);

		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

	public static void getkeys(int DBIndex, String pattern) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.select(DBIndex);
			Set<String> keys = jedis.keys(pattern);
			for (String b : keys) {
				System.out.println("keys==> " + b);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (jedis != null)
				pool.returnResource(jedis);
		}

	}

}
