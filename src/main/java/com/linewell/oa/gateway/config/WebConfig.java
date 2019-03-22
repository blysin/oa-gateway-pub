package com.linewell.oa.gateway.config;

import com.linewell.oa.gateway.interceptor.TokenInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Blysin
 * @since 2019-01-11
 */
@Component
public class WebConfig implements  WebMvcConfigurer {
    /**
     * 配置跨域请求，在对应的请求方法上添加@CrossOrigin注解即可
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");//添加一个拦截器，并配置拦截uri
    }
}
