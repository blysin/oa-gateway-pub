package com.linewell.oa.gateway.controller;

import com.linewell.oa.gateway.domail.PostResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限认证
 * @author Blysin
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @RequestMapping("/validateAuthCode")
    public PostResult validateAuthCode(String authCode){
        return PostResult.success();
    }

    @RequestMapping("/bindUser")
    public PostResult bindUser(String authCode){
        return PostResult.success();
    }

    /**
     * 重新获取token，根据钉钉userId和数据库对比，如果一致则返回token
     *
     * @param userId
     * @return
     */
    @RequestMapping("/getToken")
    public PostResult getToken(String userId){
        return PostResult.success();
    }

}
