package cn.qingweico.controller.wechat;

import cn.qingweico.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author 周庆伟
 * @date 2020/10/14
 */
@Slf4j
@Controller
@RequestMapping("/wechat")
public class WeChatController {
    @GetMapping
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("WeChat get...");
        // 微信加密签名, signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        // 通过检验signature对请求进行校验, 若校验成功则原样返回echostr, 表示接入成功, 否则接入失败
        try (PrintWriter out = response.getWriter()) {
            if (SignUtil.checkSignature(signature, timestamp, nonce)) {
                log.debug("WeChat get success....");
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
