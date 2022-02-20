package cn.qingweico;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 周庆伟
 * @date 2020/09/23
 */
@SpringBootApplication
@MapperScan("cn.qingweico.dao")
public class BootTutuMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootTutuMallApplication.class, args);
    }

}
