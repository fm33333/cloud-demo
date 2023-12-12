package com.demo.feign.controller;


import com.demo.feign.clients.UserClient;
import com.demo.feign.config.DynamicFeignClientFactory;
import com.demo.feign.data.User;
import com.demo.feign.data.UserDTO;
import com.demo.feign.data.official.TextMessage;
import com.demo.feign.utils.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private DynamicFeignClientFactory<UserClient> feignClientFactory;

    @Autowired
    private UserClient userClient;

    @PostMapping("/test")
    public String test(@RequestParam String serverName, @RequestParam String uri, @RequestParam String id) {
        log.info("test|serverName: {}, uri: {}, id: {}", serverName, uri, id);
        try {
            UserClient userClient = CacheUtil.getUserClient(feignClientFactory
                    , serverName, uri);
            log.info("test|userClient: {}", userClient.toString());

            return userClient.test(id);
        } catch (Exception e) {
            log.error("test|exception: {}", e.getMessage(), e);
            CacheUtil.cleanCache(serverName + uri);
            return "系统异常";
        }
    }

    @PostMapping("/testParam")
    public String testParam(@RequestParam String serverName, @RequestParam String uri, @RequestParam String id) {
        log.info("test|serverName: {}, uri: {}, id: {}", serverName, uri, id);
        try {
            UserClient userClient = CacheUtil.getUserClient(feignClientFactory
                    , serverName, uri);
            log.info("test|userClient: {}", userClient.toString());

            return userClient.testParam(id);
        } catch (Exception e) {
            log.error("test|exception: {}", e.getMessage(), e);
            CacheUtil.cleanCache(serverName + uri);
            return "系统异常";
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        log.info("login|userDTO: {}", userDTO);
        try {
            // 构建serverName对应的客户端
            UserClient userClient = CacheUtil.getUserClient(feignClientFactory
                    , userDTO.getServerName(), userDTO.getUri());
            log.info("login|userClient: {}", userClient.toString());

            return userClient.login(new User()
                    .setUserName(userDTO.getUserName())
                    .setPassword(userDTO.getPassword()));
        } catch (Exception e) {
            log.error("login|exception: {}", e.getMessage(), e);
            CacheUtil.cleanCache(userDTO.getServerName() + userDTO.getUri());
            return "系统异常";
        }
    }
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        log.info("register|userDTO: {}", userDTO);
        try {
            UserClient userClient = CacheUtil.getUserClient(feignClientFactory
                    , userDTO.getServerName(), userDTO.getUri());
            log.info("register|userClient: {}", userClient.toString());
            return userClient.register(new User()
                    .setUserName(userDTO.getUserName())
                    .setPassword(userDTO.getPassword()));
        } catch (Exception e) {
            log.error("register|exception: {}", e.getMessage(), e);
            CacheUtil.cleanCache(userDTO.getServerName() + userDTO.getUri());
            return "系统异常";
        }
    }

    /*@RequestMapping("/login")
    public String login(@RequestBody User user) {
        log.info("user: {}", user);
        try {
            return userClient.login(user);
        } catch (Exception e) {
            log.error("login|exception: {}", e.getMessage(), e);
            return "系统异常";
        }
    }*/

    /*@RequestMapping("/register")
    public String register(@RequestBody User user) {
        log.info("user: {}", user);
        try {
            return userClient.register(user);
        } catch (Exception e) {
            log.error("register|exception: {}", e.getMessage(), e);
            return "系统异常";
        }
        return "临时返回";
    }*/

}
