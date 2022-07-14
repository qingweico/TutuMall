package cn.qingweico.dao.spilt;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * mysql主从复制
 *
 * @author zqw
 * @date 2020/09/24
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceHolder.getDbType();
    }
}
