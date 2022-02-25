package cn.qingweico.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author zqw
 * @date 2020/11/7
 */
@Component
@Data
public class User {
    private String name;
    private String password;
}
