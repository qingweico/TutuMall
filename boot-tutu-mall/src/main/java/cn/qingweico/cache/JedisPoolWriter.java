package cn.qingweico.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zqw
 * @date 2020/11/21
 */
public class JedisPoolWriter {
    /**
     * redis连接池对象
     */
    private JedisPool jedisPool;

    public JedisPoolWriter(final JedisPoolConfig jedisPoolConfig,
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

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }
}
