package cn.qingweico.dao.spilt;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * mysql主从复制
 *
 * @author 周庆伟
 * @date 2020/09/24
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDbType();
    }
}
