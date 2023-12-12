package cn.itcast.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class XxlJobDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxlJobDemoApplication.class, args);
    }
}