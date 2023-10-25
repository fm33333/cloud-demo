package cn.itcast.user.controller;

import cn.itcast.user.entity.User;
import cn.itcast.user.service.UserService;
import cn.itcast.user.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody User user) {
        log.info("user: {}", user);
        // 密码校验
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        User user_temp = userService.getByUsernameAndPassword(user.getUserName(), md5DigestAsHex);
        if (user_temp == null) {
            return "用户名或密码错误";
        }

        // 生成 token
        String token = JwtUtil.createJWT(user_temp.getId().toString());
        return token;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody User user) {
        log.info("user: {}", user);
        String password = user.getPassword();
        // md5 加密
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(md5DigestAsHex);
        // 存入数据库
        try {
            userService.save(user);
        }catch (Exception e) {
            e.printStackTrace();
            return "用户名已存在";
        }
        return "注册成功";
    }

    @PostMapping("/test/{id}")
    public String test(@PathVariable(value = "id") String id) {
        return id;
    }


    @PostMapping("/testParam")
    public String testParam(@RequestParam String id) {
        return id;
    }
}
