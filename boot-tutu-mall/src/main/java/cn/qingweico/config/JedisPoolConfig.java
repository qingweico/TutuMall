package cn.qingweico.config;

import redis.clients.jedis.JedisPool;

/**
 * @author zqw
 * @date 2020/11/21
 */
public class JedisPoolConfig {
    /**
     * redis连接池对象
     */
    private JedisPool jedisPool;

    public JedisPoolConfig(final redis.clients.jedis.JedisPoolConfig jedisPoolConfig,
                           final String host,
                           final int port,
                           final int timeout,
                           final String password) {
        try {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
