package com.linewell.oa.gateway;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.linewell.oa.gateway.domail.PostResult;
import com.linewell.oa.gateway.util.JwtUtils;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @since 2019-01-11
 */
public class SimpleTest {

    @Test
    public void tokenTest(){
        System.out.println(JwtUtils.getToken("dsk"));
    }

    @Test
    public void decodeToken(){
        String token = "eyJraWQiOiJ1c2VydW5pZCIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJpc3MiOiJsaW5ld2VsbDEiLCJleHAiOjE1NDcxOTQ1Mjh9.4jR3NQ3m__MO8lcMgho2si7R27FWCL1v0-pcWzho5FQ";
        try {
            DecodedJWT jwt = JWT.decode(token);
            System.out.println(jwt.getExpiresAt().after(new Date()));
            System.out.println(jwt.getKeyId());
            System.out.println(jwt.getIssuer());

        } catch (JWTVerificationException exception){

        }
    }

    //eyJraWQiOiJsd0AxMjMiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NDcxOTM2ODJ9.4uelAAJ0Qff4VWgePLD06dewhzvLDSZEBPHE6F_nDGg
    @Test
    public void token(){
        PostResult result = PostResult.builder().result(false).status(404).data("hello world").build();
        System.out.println(JSON.toJSON(result));
    }

    @Test
    public void test1() {
        //String url = "http://175.43.157.100:8180/ddoa/appinterface.action";
        String url = "http://blysin.vaiwan.com:8081/fzoa/appinterface.action";
        Map<String, Object> map = new HashMap<>();
        map.put("fn", "loginVerify");
        map.put("username", "董淑锦");
        map.put("password", "8c22abf9a3a11d956ce2ade54bca707a");
        String str = HttpUtil.post(url, map);

        JSONObject json = JSON.parseObject(str);
        System.out.println(JSON.toJSONString(json, true));
    }
}
