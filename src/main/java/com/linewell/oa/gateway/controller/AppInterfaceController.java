package com.linewell.oa.gateway.controller;

import com.linewell.oa.gateway.domail.PostResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Blysin
 * @since 2019-01-11
 */
@RestController
@Log4j2
public class AppInterfaceController {

    @RequestMapping("/ping")
    public PostResult ping(){
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("info");
        return PostResult.success("pong");
    }

    public PostResult verifyAuthCode(String authCode){

        return PostResult.success("pong");
    }

}
