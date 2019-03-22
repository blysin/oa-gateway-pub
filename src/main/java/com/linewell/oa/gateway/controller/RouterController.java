package com.linewell.oa.gateway.controller;

import com.linewell.oa.gateway.property.OaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping(value = "/gateway", method = RequestMethod.POST)
@Slf4j
public class RouterController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OaProperties oaProperties;

    private final String METHOD = "method";

    @RequestMapping("/appOperation")
    public Object appOperation(HttpServletRequest request, HttpServletResponse response) {
        return distribute(request, oaProperties.getAppOperationUrl());
    }

    @RequestMapping("/appInterface")
    public Object appInterface(HttpServletRequest request, HttpServletResponse response) {
        return distribute(request, oaProperties.getAppInterfaceUrl());
    }

    @RequestMapping("/appFiles/{method}")
    public Object appFiles(HttpServletRequest request, HttpServletResponse response, @PathVariable("method") String method) {
        log.info(method);
        return distribute(request, oaProperties.getAppFilesUrl().replace(METHOD, method));
    }

    /**
     * 请求分发
     *
     * @param request
     * @param url
     * @return
     */
    public Object distribute(HttpServletRequest request, String url) {
        return restTemplate.postForObject(url, getParams(request), Object.class);
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
