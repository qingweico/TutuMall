package cn.qingweico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zqw
 * @date 2020/11/8
 */
@Controller
public class IndexController {
    @GetMapping({"/", "/index"})
    public String index(){
        return "index";
    }
}
