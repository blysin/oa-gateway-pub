package com.linewell.oa.gateway.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Blysin
 * @since 2019-01-14
 */
@Data
@Component
@ConfigurationProperties(prefix = "http-pool")
public class HttpPoolProperties {
    private Integer maxTotal;
    private Integer defaultMaxPerRoute;
    private Integer connectTimeout;
    private Integer connectionRequestTimeout;
    private Integer socketTimeout;
    private Integer validateAfterInactivity;
}
