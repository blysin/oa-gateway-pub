package com.linewell.oa.gateway.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Blysin
 * @since 2019-01-16
 */
@Data
@Component
@ConfigurationProperties(prefix = "oa")
public class OaProperties {
    private String appOperationUrl;

    private String appInterfaceUrl;

    private String appMicroServiceUrl;
}
