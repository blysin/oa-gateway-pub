package com.linewell.oa.gateway.controller;

import cn.hutool.http.HttpUtil;
import com.linewell.oa.gateway.domail.PostResult;
import com.linewell.oa.gateway.property.OaProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Blysin
 * @since 2019-03-21
 */
@RestController
@RequestMapping("/gateway/files/")
@Slf4j
public class FileIoController {
    @Autowired
    private OaProperties oaProperties;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ClientHttpRequestFactory httpRequestFactory;

    private File baseFile;


    /**
     * 下载阅办单
     *
     * @param response
     * @param docUnid
     * @param flowUnid
     * @param userunid
     */
    @RequestMapping("/flowImage")
    public void flowImage(HttpServletResponse response, @RequestParam String docUnid, @RequestParam String flowUnid, @RequestParam String userunid) {
        if (docUnid.length() == 32 || userunid.length() == 32 || flowUnid.length() == 32) {
            String url = oaProperties.getAppFlowImageUrl() + "?docUnid=" + docUnid + "&flowUnid=" + flowUnid + "&userunid=" + userunid;
            download(response, url);
        } else {
            log.warn("阅办单下载参数错误");
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * 下载附件
     *
     * @param response
     * @param fileUnid
     */
    @RequestMapping("/download")
    public void fileDownload(HttpServletResponse response, @RequestParam String fileUnid) {
        if (true || fileUnid.length() == 32) {
            String url = oaProperties.getAppFileDownloadUrl() + "?fileUnid=" + fileUnid;
            download(response, url);
        } else {
            log.warn("文件下载fileUnid格式不对：" + fileUnid);
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    /**
     * 文件上传
     *
     * @param file
     * @param belongTo
     * @param file_type
     * @param file_creator
     * @return
     */
    @RequestMapping("/upload")
    public Object fileUpload(@RequestParam("file") MultipartFile file, String belongTo, String file_type, String file_creator) {
        if (file.isEmpty()) {
            return PostResult.error("上传失败，请选择文件");
        }

        String fileName = file.getOriginalFilename();

        String result = null;
        try {
            File dest = new File(getBaseFile() , fileName);

            log.info(dest.getName());

            file.transferTo(dest);
            log.info("文件上传，临时文件路径：" + dest.getPath());

            FileSystemResource resource = new FileSystemResource(dest);
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            param.add("file", resource);
            param.add("fileName", dest.getName());
            param.add("belongTo", belongTo);
            param.add("file_type", file_type);
            param.add("file_creator", file_creator);

            result = restTemplate.postForObject(oaProperties.getAppFileUploadUrl(), param, String.class);
            dest.delete();
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return result == null ? PostResult.error("上传失败，请选择文件") : result;
    }

    /**
     * 获取临时文件路径
     *
     * @return
     * @throws FileNotFoundException
     */
    private File getBaseFile() {
        if (baseFile == null) {
            ApplicationHome home = new ApplicationHome(getClass());
            File jarFile = home.getSource();
            baseFile = jarFile.getParentFile();
            log.info("临时文件根目录：" + baseFile.getPath());
            //String osName = System.getProperty("os.name");
            //log.info("当前系统类型：" + osName);
            //if (StringUtils.containsIgnoreCase(osName, "windows")) {
            //    baseFile = new File("c:/temp");
            //} else {
            //    baseFile = new File("/home/gateway/cache/temp");
            //}
            //if (!baseFile.exists()) {
            //    baseFile.mkdirs();
            //}
        }
        return baseFile;
    }

    /**
     * 发送下载请求
     *
     * @param response
     * @param url
     */
    private void download(HttpServletResponse response, String url) {
        log.info("文件下载请求路径：" + url);
        try {
            ClientHttpRequest httpRequest = httpRequestFactory.createRequest(URI.create(url), HttpMethod.GET);
            ClientHttpResponse httpResponse = httpRequest.execute();
            setHeader(response, httpResponse.getHeaders());
            copyFile(httpResponse.getBody(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件写入response输出流中
     *
     * @param body
     * @param response
     */
    private void copyFile(InputStream body, HttpServletResponse response) {
        try {
            int length = IOUtils.copy(body, response.getOutputStream());
            response.setContentLength(length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(body);
        }
    }

    /**
     * 设置响应头
     *
     * @param response
     * @param headers
     */
    private void setHeader(HttpServletResponse response, HttpHeaders headers) {
        Set<String> headerKeys = headers.keySet();
        for (String key : headerKeys) {
            List<String> values = headers.get(key);
            if (CollectionUtils.isNotEmpty(values)) {
                for (String v : values) {
                    response.setHeader(key, v);
                }
            }
        }
    }
}
