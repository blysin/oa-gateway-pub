package com.linewell.oa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 打包成war包时需要继承SpringBootServletInitializer并重写configure方法
 * <p>
 * 打包war：mvn clean package -Dmaven.test.skip=true
 * 打包jar：mvn clean install -Dmaven.test.skip=true
 */
@SpringBootApplication
@EnableCaching
public class OaGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OaGatewayApplication.class, args);
    }

}

