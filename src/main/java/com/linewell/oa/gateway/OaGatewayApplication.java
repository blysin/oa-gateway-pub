package com.linewell.oa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OaGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OaGatewayApplication.class, args);
    }

}

