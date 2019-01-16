package com.linewell.oa.gateway.service;

import com.dingtalk.api.response.OapiUserGetResponse;
import com.linewell.oa.gateway.domail.AuthUserInfo;
import com.linewell.oa.gateway.property.OaProperties;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

/**
 * @author Blysin
 * @since 2019-01-16
 */
@Service
@Log4j2
public class UserService {
    @Autowired
    private OaProperties oaProperties;
    @Autowired
    private RestTemplate restTemplate;

    public AuthUserInfo getAuthUserInfo(String userId,String mobile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>(3);

        params.add("fn", "auth_getAuthUserInfo");
        params.add("userId", userId);
        params.add("mobile", mobile);

        HttpEntity<MultiValueMap<String, String>> res = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<AuthUserInfo> result = restTemplate.postForEntity(oaProperties.getAppMicroServiceUrl(), res, AuthUserInfo.class);
            return result.getBody();
        } catch (Exception e) {
            log.warn("用户信息请求错误",e);
        }
        return AuthUserInfo.bindError();
    }
}
