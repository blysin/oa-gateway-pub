package com.linewell.oa.gateway.controller;

import com.linewell.oa.gateway.domail.PostResult;
import com.linewell.oa.gateway.service.DingDingService;
import com.linewell.oa.gateway.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限认证，作废不用
 * @author Blysin
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private DingDingService dingDingService;

    @RequestMapping("/validateAuthCode")
    public PostResult validateAuthCode(String authCode){
        return PostResult.success(dingDingService.getUserAuthInfo(authCode));
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
