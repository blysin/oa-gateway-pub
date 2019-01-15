package com.linewell.oa.gateway.controller;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * 路由转发
 *
 * @author Blysin
 * @since 2019-01-14
 */
@RestController
public class RouterController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${oa.app-operation-url}")
    private String appOperationUrl;

    @Value("${oa.app-interface-url}")
    private String appInterfaceUrl;

    @RequestMapping("/appOperation")
    public String appOperation(HttpServletRequest request, HttpServletResponse response) {
        return distribute(request, appOperationUrl);
    }

    @RequestMapping("/appInterface")
    public String appInterface(HttpServletRequest request, HttpServletResponse response) {
        return distribute(request, appInterfaceUrl);
    }

    /**
     * 请求分发
     *
     * @param request
     * @param url
     * @return
     */
    public String distribute(HttpServletRequest request, String url) {
        ResponseEntity<String> result = restTemplate.postForEntity(url, getParams(request), String.class);
        return result.getBody();
    }

    /**
     * 获取参数
     *
     * @param request
     * @return
     */
    private HttpEntity getParams(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String[]> map = request.getParameterMap();
        MultiValueMap<String, Object> params = null;
        if (MapUtils.isNotEmpty(map)) {
            params = new LinkedMultiValueMap<>();
            Set<String> keys = map.keySet();
            for (String key : keys) {
                params.add(key, request.getParameter(key));
            }
        }

        HttpEntity<MultiValueMap<String, Object>> res = new HttpEntity<>(params, headers);
        return res;
    }
}
