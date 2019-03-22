package com.linewell.oa.gateway.config;

import com.linewell.oa.gateway.property.HttpPoolProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * @author Blysin
 * @since 2019-01-14
 */
@Configuration
public class RestTemplateConfig {
    @Autowired
    private HttpPoolProperties httpPoolProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));//处理编码
        return restTemplate;
    }

    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpPoolProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpPoolProperties.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(httpPoolProperties.getValidateAfterInactivity());
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(httpPoolProperties.getSocketTimeout()) //服务器返回数据(response)的时间，超过抛出read timeout
                .setConnectTimeout(httpPoolProperties.getConnectTimeout()) //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectionRequestTimeout(httpPoolProperties.getConnectionRequestTimeout())//从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .build();
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    //@Bean
    //public SimpleClientHttpRequestFactory httpRequestFactory1() {
    //    return new SimpleClientHttpRequestFactory().;
    //}
}
