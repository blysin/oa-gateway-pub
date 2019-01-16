package com.linewell.oa.gateway.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.linewell.oa.gateway.domail.Token;
import com.linewell.oa.gateway.property.DingDingProperties;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Blysin
 * @since 2019-01-15
 */
@Service
@Log4j2
public class TokenService {
    private volatile Token token = new Token();
    @Autowired
    private DingDingProperties dingDingProperties;

    public String getToken(){
        String tokenStr = token.getToken();
        if (StringUtils.isNotEmpty(tokenStr) || !isEmpire()) {
            return tokenStr;
        }
        return requestToken();
    }

    private String requestToken() {
        String tokenStr = null;
        if (dingDingProperties.getDev()) {
            tokenStr = getTokenFromDingDing();
        } else {
            tokenStr = getTokenFromLocalService();
        }
        if (StringUtils.isNotEmpty(tokenStr)) {
            token.setToken(tokenStr);
            token.setCreateTimeMillis(System.currentTimeMillis());
        }
        return tokenStr;
    }

    /**
     * 请求政务外网
     * @return
     */
    private String getTokenFromLocalService() {
        log.info("请求本地钉钉服务");
        try {
            String resp = HttpUtil.get(dingDingProperties.getUrlGetTokken());//LOCAL_URL_GET_TOKKEN
            log.info("响应数据：" + resp);
            if (StringUtils.isEmpty(resp)) return "";
            JSONObject respBody = JSON.parseObject(resp);
            if (isSuccess(respBody)) {
                return respBody.getJSONObject("retData").getString("token");
            } else {
                log.warn("token请求失败！");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isSuccess(JSONObject respBody) {
        return (respBody != null && respBody.getInteger("retCode") == 0 && "success".equals(respBody.getString("retMessage")));
    }

    /**
     * 请求钉钉官方服务器
     * @return
     */
    private String getTokenFromDingDing() {
        try {
            log.info("发起token请求");
            DefaultDingTalkClient client = new DefaultDingTalkClient(dingDingProperties.getUrlGetTokken());//URL_GET_TOKKEN
            OapiGettokenRequest request = new OapiGettokenRequest();

            client.setConnectTimeout(3000);//超时3秒
            request.setAppkey(dingDingProperties.getAppKey());
            request.setAppsecret(dingDingProperties.getAppSecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            String accessToken = response.getAccessToken();
            log.info("token信息：" + JSON.toJSONString(response));
            return accessToken;
        } catch (Exception e) {
            log.error("getAccessToken failed", e);
        }
        return "";
    }

    /**
     * 判断token是否创建时间是否超过7200秒
     * @return
     */
    private boolean isEmpire(){
        return System.currentTimeMillis() - token.getCreateTimeMillis() > 7190_000;
    }

}
