package com.demo.feign.clients;


import com.demo.feign.config.DynamicRoutingConfig;
import com.demo.feign.data.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * feign客户端
 */
@FeignClient(name = "userClient", configuration = DynamicRoutingConfig.class)
public interface UserClient {

    @PostMapping("")
    String login(User user);

    @PostMapping("")
    String register(User user);

    // 含PathVariable或RequestParam必须这么写
    @PostMapping("//$serverName/user/test/{id}")
    String test(@PathVariable(value = "id") String id);

    @PostMapping("//$serverName/user/testParam")
    String testParam(@RequestParam String id);
}
