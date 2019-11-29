/*
这个是10月22日10.11分的测试用例
 */

package com.bingo.qa.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class JedisPoolUtil {

private volatile JedisPool pool;
private Jedis jedis;
private final String host;
private final int port;
private String password;

public JedisPoolUtil(final String host, final int port) {
this.host = host;
this.port = port;
initPool();
}

public JedisPoolUtil(final String host, final int port, final String password) {
this.host = host;
this.port = port;
this.password = password;
initPool();
}

public JedisPool initPool() {
if (pool == null) {
if (password != null) {
JedisPoolConfig config = new JedisPoolConfig();
config.setMaxIdle(100);
config.setMaxWaitMillis(1000);
// 设置空间连接
config.setMaxIdle(10);
pool = new JedisPool(config, host, port, 2000, password);
} else {
pool = new JedisPool(host, port);
}
System.out.println("连接池创建成功");
}
return pool;
}

public Jedis initJedis() {
if(jedis==null) {
jedis = pool.getResource();
}
System.out.println("jedis创建成功");
return jedis;
}

public String set(String key, String value) {
if (jedis == null) {
jedis = initJedis();
}
String result = jedis.set(key, value);
if ("OK".equals(result)) {
jedis.close();
}
return result;
}

public static void main(String[] args) {
for (int i = 0; i < 100; i++) {
JedisPoolUtil jedisPoolUtil = new JedisPoolUtil("127.0.0.1", 6379,"123456");
System.out.println(jedisPoolUtil.set("test" + i, "value" + i));
}
}



}
