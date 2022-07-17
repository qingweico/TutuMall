package cn.qingweico.utils;

import cn.qingweico.config.JedisPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.SafeEncoder;

import java.util.Set;

/**
 * @author zqw
 * @date 2020/11/22
 */
public class JedisUtil {
    /**
     * 操作key的方法
     */
    public Keys KEYS;
    /**
     * 操作String类型的方法
     */
    public Strings STRINGS;
    /**
     * redis连接池对象
     */
    public JedisPool jedisPool;

    /**
     * 获取redis连接池
     *
     * @return JedisPool
     */
    public JedisPool getJedisPool() {
        return jedisPool;
    }

    /**
     * 设置redis连接池
     *
     * @param redisPoolWriter JedisPoolWriter
     */
    public void setJedisPool(JedisPoolConfig redisPoolWriter) {
        this.jedisPool = redisPoolWriter.getJedisPool();
    }

    /**
     * 从jedis连接池中获取jedis对象
     *
     * @return Jedis
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public class Keys {
        public Keys() {

        }

        /**
         * 清空所有的key
         *
         * @return String
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String state = jedis.flushAll();
            jedis.close();
            return state;
        }

        /**
         * 删除key对用的value 可以是多个key
         *
         * @param keys 一个或者多个key
         */
        public void del(String... keys) {
            Jedis jedis = getJedis();
            jedis.del(keys);
            jedis.close();
        }

        /**
         * 判断key是否存在
         *
         * @param key key
         * @return boolean
         */
        public boolean exists(String key) {
            Jedis jedis = getJedis();
            boolean isExists = jedis.exists(key);
            jedis.close();
            return isExists;
        }

        /**
         * 按模式查找所匹配的key
         *
         * @param pattern pattern *表示多个 ?表示一个
         * @return Set
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }


    public class Strings {

        public Strings() {
        }

        /**
         * 根据key获取value
         *
         * @param key key
         * @return String
         */
        public String get(String key) {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            jedis.close();
            return value;
        }

        /**
         * 添加value 若value存在则覆盖
         *
         * @param key   key
         * @param value value
         * @return String
         */
        public String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加value 若value存在则覆盖
         *
         * @param key   key
         * @param value value
         * @return String
         */
        public String set(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }
    }
}
