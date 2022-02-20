package cn.qingweico.config;

import cn.qingweico.utils.EncryptPropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * @author 周庆伟
 * @date 2020/12/3
 */
public class EncryptPropertyPlaceholderConfig {
    @Bean
    public EncryptPropertyPlaceholderConfigurer encryptPropertyPlaceholderConfigure() {
        EncryptPropertyPlaceholderConfigurer encrypt = new EncryptPropertyPlaceholderConfigurer();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("classpath:application.yml");
        assert inputStream != null;
        Resource resource = new InputStreamResource(inputStream);
        encrypt.setLocation(resource);
        encrypt.setFileEncoding("UTF-8");
        return encrypt;

    }
}
