package cn.qingweico.config;

import cn.qingweico.dao.spilt.DynamicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 动态数据源配置
 * @author 周庆伟
 * @date 2020/12/3
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean
    public DynamicDataSource dynamicDataSource(DataSource masterDataSource, DataSource slaveDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> map = new HashMap<>(5);
        map.put("master", masterDataSource);
        map.put("slave", slaveDataSource);
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;

    }
}
