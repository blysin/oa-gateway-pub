package com.linewell.oa.gateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Blysin
 * @since 2019-01-15
 */
@Data
@Component
@ConfigurationProperties(prefix = "dd")
public class DingDingProperties {
    private String agentid;
    private String appKey;
    private String appSecret;
    private String urlGetUserInfo;
    private String urlGetTokken;
    private String swapAuthcode;
    private String urlUserGet;
}
