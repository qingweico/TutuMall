package cn.qingweico.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 周庆伟
 * @date 2020/12/2
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@NoArgsConstructor
@Data
public class RedisConstant {

    private String host;

    private int port;

    private int timeout;

    private Map<String, String> pool = new HashMap<>();

    private String password;
}
