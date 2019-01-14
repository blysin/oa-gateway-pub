package com.linewell.oa.gateway;

import com.linewell.oa.gateway.property.HttpPoolProperties;
import com.linewell.oa.gateway.property.JwtProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OaGatewayApplicationTests {
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private HttpPoolProperties httpPoolProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${oa.app-operation-url}")
    private String appOperationUrl;

    @Value("${oa.app-interface-url}")
    private String appInterfaceUrl;

    @Test
    public void contextLoads() throws UnsupportedEncodingException {
        //Map<String, String> json= new HashMap<>(3);
        //json.put("userunid", "71435EC45072BA4CFC77A42FA2A76F1B");
        //json.put("fn", "getLeaveInfo");
        //json.put("year", "2018");

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        //map.add("shopid","1");
        map.add("userunid", "71435EC45072BA4CFC77A42FA2A76F1B");
        map.add("fn", "getLeaveInfo");
        map.add("year", "2018");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> result = restTemplate.postForEntity(appOperationUrl, request, String.class);
        System.out.println(result.getStatusCodeValue());
        System.out.println(result.getHeaders());
        System.out.println(new String(result.getBody().getBytes(),"UTF-8"));

    }

    @Test
    public void strTest(){
        System.out.println(appOperationUrl);
        System.out.println(appInterfaceUrl);
    }

}

