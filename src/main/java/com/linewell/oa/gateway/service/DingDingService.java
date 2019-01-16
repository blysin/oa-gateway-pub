package com.linewell.oa.gateway.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.linewell.oa.gateway.domail.AuthUserInfo;
import com.linewell.oa.gateway.property.DingDingProperties;
import com.taobao.api.ApiException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Blysin
 * @since 2019-01-15
 */
@Service
@Log4j2
public class DingDingService {
    @Autowired
    private DingDingProperties dingDingProperties;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    public String getDDUserId(String accessToken, String authCode) {
        //获取用户信息
        DefaultDingTalkClient client = new DefaultDingTalkClient(dingDingProperties.getUrlGetUserInfo());//URL_GET_USER_INFO
        client.setConnectTimeout(3000);
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(authCode);
        request.setHttpMethod("GET");

        OapiUserGetuserinfoResponse response;
        String userId = null;
        try {
            response = client.execute(request, accessToken);
            userId = response.getUserid();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 获取用户姓名
     *
     * @param accessToken
     * @param userId
     * @return
     */
    public String getUserName(String accessToken, String userId) {
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(dingDingProperties.getUrlUserGet());//URL_USER_GET
            client.setConnectTimeout(3000);
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            OapiUserGetResponse response = client.execute(request, accessToken);
            System.out.println(response);
            return response.getName();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public OapiUserGetResponse getUserInfo(String userId) {
        String token = tokenService.getToken();
        OapiUserGetResponse response = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient(dingDingProperties.getUrlUserGet());//URL_USER_GET
            OapiUserGetRequest request = new OapiUserGetRequest();
            request.setUserid(userId);
            request.setHttpMethod("GET");
            response = client.execute(request, token);

        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public AuthUserInfo getUserAuthInfo(String authCode) {
        //获取钉钉用户id和用户手机号，去数据库匹配，如果无法匹配则返回无绑定用户的信息，
        //if (dingDingProperties.getDev()) {
            return getUserAuthInfoFromDingDing(authCode);
        //} else {
        //    return getUserAuthInfoFromLocal(authCode);
        //}
        //return null;
    }

    //private JSONObject getUserInfoFromLocal(String authCode) {
    //    String swapCodeBodyStr = HttpUtil.get(dingDingProperties.getSwapAuthcode()+ authCode);//LOCAL_SWAP_AUTHCODE
    //    if (StringUtils.isEmpty(swapCodeBodyStr)) {
    //        return null;
    //    }
    //    JSONObject respBody = JSON.parseObject(swapCodeBodyStr);
    //    if (AccessTokenUtil.isSuccess(respBody)) {
    //        String code = respBody.getJSONObject("retData").getString("code");
    //        log.info("转换后的code：" + code);
    //        String token = AccessTokenUtil.getToken();
    //        String userInfoStr = HttpUtil.get(LOCAL_GET_USER_INFO + "?token=" + token + "&code=" + code);
    //        log.info("获取用户信息：" + userInfoStr);
    //        if (StringUtils.isNotEmpty(userInfoStr)) {
    //            respBody = JSON.parseObject(userInfoStr);
    //            if (AccessTokenUtil.isSuccess(respBody)) {
    //                return respBody;
    //            }
    //        }
    //    }
    //    return null;
    //}
    //
    //private String getDDUserIdByAuthCodeFromLocal(String authCode) {
    //    JSONObject respBody = getUserInfoFromLocal(authCode);
    //    return respBody == null ? "" : respBody.getJSONObject("retData").getString("code");
    //}
    //
    //private AuthUserInfo getUserAuthInfoFromLocal(String authCode) {
    //    JSONObject respBody = getUserInfoFromLocal(authCode);
    //    if (respBody == null) {
    //        return AuthUserInfo.bindError();
    //    }
    //
    //    String userId = respBody.getJSONObject("retData").getString("code");
    //    UcapUserBusiness userBusiness = new UcapUserBusiness();
    //    UcapUser ucapUser = userBusiness.doFindBeanByCondition("thirdpart_userid = ?", new String[]{userId});
    //
    //    if (ucapUser == null) {
    //        String userMobile = respBody.getJSONObject("retData").getString("mobile");
    //        ucapUser = userBusiness.doFindBeanByCondition("user_mobile = ?", new String[]{userMobile});
    //        //直接绑定用户
    //        if (ucapUser != null && StringUtils.isEmpty(ucapUser.getThirdpart_userid()) && StringUtils.isNotEmpty(userId)) {
    //            ucapUser.setThirdpart_userid(userId);
    //            userBusiness.doUpdate(ucapUser);
    //        }
    //    }
    //
    //    if (ucapUser == null) {
    //        log.info("没有关联用户！");
    //        return AuthUserInfo.bindError();
    //    } else {
    //        log.info("登录成功！");
    //        return validateSuccess(ucapUser);
    //    }
    //}
    //
    //
    private AuthUserInfo getUserAuthInfoFromDingDing(String authCode) {
        String accessToken = tokenService.getToken();
        //获取用户ID
        String userId = StringUtils.isEmpty(accessToken) ? "" : getDDUserId(accessToken, authCode);
        if (StringUtils.isEmpty(userId)) {//获取不到，应该是测试环境
            log.info("无法通过钉钉服务器获取用户信息");
            AuthUserInfo authUserInfo = new AuthUserInfo();
            authUserInfo.setHasBindUser(false);
            return authUserInfo;
        }

        //获取用户信息
        OapiUserGetResponse ddUserInfo = getUserInfo(userId);

        //根据用户信息去oa做校验
        return userService.getAuthUserInfo(ddUserInfo.getUserid(),ddUserInfo.getMobile());
    }
    //
    //public AuthUserInfo validateSuccess(UcapUser ucapUser) {
    //    AuthUserInfo authUserInfo = new AuthUserInfo();
    //    authUserInfo.setUsername(ucapUser.getUser_display_name());
    //    authUserInfo.setUserunid(ucapUser.getUser_unid());
    //    authUserInfo.setDingdingUserId(ucapUser.getThirdpart_userid());
    //    UcapDept dept = new UcapDeptBusiness().doFindBeanByKey(ucapUser.getUser_depts());
    //
    //    if (dept != null) {
    //        authUserInfo.setDeptname(dept.getDept_name());
    //        authUserInfo.setDeptunid(dept.getDept_unid());
    //    }
    //    return authUserInfo;
    //}
    //
    //public String getDDUserIdByAuthCode(String authCode) {
    //    if (DingDingConfig.isDebug) {
    //        return getDDUserIdByAuthCodeFromDingDing(authCode);
    //    } else {
    //        return getDDUserIdByAuthCodeFromLocal(authCode);
    //    }
    //}
    //
    //
    //private String getDDUserIdByAuthCodeFromDingDing(String authCode) {
    //    String accessToken = AccessTokenUtil.getToken();
    //    return StringUtils.isEmpty(accessToken) ? "" : new DingDingService().getDDUserId(accessToken, authCode);
    //}
}
