package com.demo.feign.config;

import com.alibaba.fastjson.JSON;
import com.demo.feign.data.User;
import com.demo.feign.data.UserDTO;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.form.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 动态设置服务名配置
 * 解决接口存在@PathVariable参数时报错的问题
 */
@Slf4j
@Configuration
public class DynamicRoutingConfig {

    // 接口路径中待替换的标志
    private static final String $_SERVER_NAME = "$serverName";
    private static final String $_URI = "$uri";


    private static final String HTTP = "http:";
    private static final String HTTPS = "https:";

    // 请求参数key
    private static final String SERVER_NAME = "serverName";
    private static final String URI = "uri";

    // 符号常量
    private static final String QUESTION = "?";
    private static final String SLASH = "/";
    private static final String DOUBLE_SLASH = "//";

    /**
     * 调用feign接口才执行此方法
     * @return
     */
    @Bean
    public RequestInterceptor cloudContextInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                log.info("apply|template: {}", template.toString());
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)
                        RequestContextHolder.currentRequestAttributes();
                HttpServletRequest request = servletRequestAttributes.getRequest();
                String url = template.url();
                log.info("apply|url: {}", url);

                // url为“/”，表示使用工厂构建Client，参数类型为@RequestBody，执行后续的target和uri方法会报错，因此直接return
                if (url.equals(SLASH)) {
                    return;
                }

                // 特殊标记，以“//”开头
                if (url.startsWith(DOUBLE_SLASH)) {
                    url = HTTP + url;
                    /*
                     * 有带问号的url时， 这里需要截取url参数，
                     * 不然调用了template.target方法，已经给了参数了， 如 get http://test?param=1
                     * 再调用template.uri("")又给了一次，会变成传一个参数，接收成两个的问题  变成 http://test?param=1&param=1
                     * 不调用template.uri("")，uri会变成 http://test//test?param=1,所以我们需要把uri置空， 变成http://?param=1，最终变成我们正常的url http://test?param=1
                     */
                    if (url.contains(QUESTION)) {
                        url = subUrl(url, QUESTION);
                    }
                }

                // 特殊标记，包含$serverName，动态替换
                if (url.contains($_SERVER_NAME)) {
                    /*if ("application/json".equals(request.getContentType())) {
                        log.info("apply|contentType|application/json");
                        byte[] requestBody;
                        try {
                            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String requestBodyStr = new String(requestBody, StandardCharsets.UTF_8);
                        log.info("apply|requestBodyStr: {}", requestBodyStr);
                        UserDTO userDTO = (UserDTO) JSON.parse(requestBodyStr);
                        url = url.replace($_SERVER_NAME, userDTO.getServerName());
                    } else {
                        log.info("apply|contentType|else");
                        String[] serverNames = request.getParameterMap().get(SERVER_NAME);
                        url = url.replace($_SERVER_NAME, serverNames[0]);
                    }*/
                    String[] serverNames = request.getParameterMap().get(SERVER_NAME);
                    url = url.replace($_SERVER_NAME, serverNames[0]);
                }

                log.info("apply|url: {}", url);
                template.target(url); // 将拼好的路径作为url
                template.uri(""); // 这里需要把uri置空，不置空会变成两个 http://test//test?param=1
            }
        };
    }

    /**
     * 截断target之后的字符
     * @param url
     * @param target
     * @return
     */
    public static String subUrl(String url, String target) {
        return url.substring(0, url.indexOf(target));
    }
}
