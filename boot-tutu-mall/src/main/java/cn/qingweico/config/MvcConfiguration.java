package cn.qingweico.config;

import cn.qingweico.interceptor.LoginHandleInterceptor;
import cn.qingweico.utils.PathUtil;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author 周庆伟
 * @date 2020/09/28
 */
public class MvcConfiguration implements WebMvcConfigurer {
    /**
     * 视图解析器
     *
     * @param registry ViewControllerRegistry
     */
    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {

    }

    /**
     * 拦截器
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandleInterceptor()).addPathPatterns("/shopadmin/**")
                .excludePathPatterns("/shopadmin/addshopauthmap")
                .excludePathPatterns("/shopadmin/adduserproductmap")
                .excludePathPatterns("/shopadmin/exchangeaward")
                .excludePathPatterns("/shopadmin/getshoplist")
                .excludePathPatterns("/shopadmin/getshopinitinfo")
                .excludePathPatterns("/shopadmin/registershop")
                .excludePathPatterns("/shopadmin/shopoperation")
                .excludePathPatterns("/shopadmin/shopmanagement")
                .excludePathPatterns("/shopadmin/getshopmanagementinfo")
                .excludePathPatterns("/shopadmin/addshopauthmap")
                .excludePathPatterns("/shopadmin/adduserproductmap")
                .excludePathPatterns("/shopadmin/exchangeaward");
    }

    /**
     * 图片访问路径
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name");
        if(PathUtil.OS.equals(os)) {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:E:/picture/upload/");
        }else {
            registry.addResourceHandler("/upload/**").addResourceLocations("file:/home/image/upload/");

        }
        //静态资源的访问
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/templates/",
                "classpath:/static/",
                "classpath:/resources/");

    }


    /**
     * 文件上传
     *
     * @return CommonsMultipartResolver
     */
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxUploadSize(5242880);
        multipartResolver.setMaxInMemorySize(5242880);
        return multipartResolver;
    }

    /**
     * 验证码
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean<KaptchaServlet> servletRegistrationBean() {
        ServletRegistrationBean<KaptchaServlet> servletRegistrationBean = new ServletRegistrationBean<>(new KaptchaServlet(), "/Kaptcha");
        servletRegistrationBean.addInitParameter("kaptcha.border", "no");
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.color", "red");
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.size", "43");
        servletRegistrationBean.addInitParameter("kaptcha.image.width", "135");
        servletRegistrationBean.addInitParameter("kaptcha.image.height", "50");
        servletRegistrationBean.addInitParameter("kaptcha.testproducer.char.string", "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.char.length", "4");
        servletRegistrationBean.addInitParameter("kaptcha.noise.color", "black");
        servletRegistrationBean.addInitParameter("kaptcha.textproducer.font.names", "Courier");
        return servletRegistrationBean;

    }
}
