package cn.qingweico.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 后端路由控制
 *
 * @author zqw
 * @date 2020/11/11
 */
@Controller
public class RouteController {
    private static final String SEPARATOR = "/";

    @GetMapping("/{base}/{url}")
    public String redirect(@PathVariable("base") String base,
                           @PathVariable("url") String url) {
        return base + SEPARATOR + url;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/frontend/index";
    }
}
