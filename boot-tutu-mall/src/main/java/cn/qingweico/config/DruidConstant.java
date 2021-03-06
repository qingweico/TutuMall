package cn.qingweico.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 周庆伟
 * @date 2020/12/3
 */
@Component
@ConfigurationProperties(prefix = "spring.datasource")
@NoArgsConstructor
@Data
public class DruidConstant {
    private String masterUrl;
    private String slaveUrl;
    private String driverClassName;
    private String username;
    private String password;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int timeBetweenEvictionRunsMillis;
    private int minEvictableIdleTimeMillis;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean poolPreparedStatements;
    private String filters;
    private int maxPoolPreparedStatementPerConnectionSize;
    private boolean useGlobalDataSourceStat;
    private String connectionProperties;

}
