package cn.qingweico.config;

import cn.qingweico.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

/**
 * @author 周庆伟
 * @date 2020/11/15
 */

@Configuration
public class RedisConfig {

    @Autowired
    private RedisConstant redisConstant;
    @Autowired
    private redis.clients.jedis.JedisPoolConfig jedisPoolConfig;
    @Autowired
    private JedisPoolConfig jedisWritePool;
    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建redis连接池的设置
     *
     * @return JedisPoolConfig
     */
    @Bean(name = "jedisPoolConfig")
    public redis.clients.jedis.JedisPoolConfig createJedisPoolConfig() {
        redis.clients.jedis.JedisPoolConfig jedisPoolConfig = new redis.clients.jedis.JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(redisConstant.getPool().get("maxActive")));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(redisConstant.getPool().get("maxIdle")));
        jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(redisConstant.getPool().get("maxWait")));
        return jedisPoolConfig;
    }

    /**
     * 创建Redis连接池
     *
     * @return JedisPoolWriter
     */
    @Bean(name = "jedisWriterPool")
    public JedisPoolConfig createJedisPoolWriter() {
        return new JedisPoolConfig(jedisPoolConfig,
                                   redisConstant.getHost(),
                                   redisConstant.getPort(),
                                   redisConstant.getTimeout(),
                                   redisConstant.getPassword());
    }

    /**
     * 创建Redis工具类, 封装好Redis的连接以进行相关的操作
     *
     * @return JedisUtil
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisWritePool);
        return jedisUtil;
    }

    /**
     * Redis的key操作
     *
     * @return JedisUtil.Keys
     */
    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        return jedisUtil.new Keys();
    }

    /**
     * Redis的Strings操作
     *
     * @return JedisUtil.Strings
     */
    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        return jedisUtil.new Strings();
    }
}
