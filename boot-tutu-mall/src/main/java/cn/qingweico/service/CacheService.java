package cn.qingweico.service;

import org.springframework.stereotype.Repository;

/**
 * @author 周庆伟
 * @date 2020/10/15
 */
@Repository
public interface CacheService {
    /**
     * 删除以key开头的所有的key-value
     *
     * @param prefixToKey prefixToKey
     */
    void removeFromCache(String prefixToKey);
}
