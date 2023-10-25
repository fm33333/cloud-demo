## 目录结构

### eureka-server1

eureka注册中心1

### eureka-server2

eureka注册中心2

### user-service

登录注册服务

### user-consume

登录注册服务调用者

## 搭建步骤

1. 修改 C:\Windows\System32\drivers\etc\hosts

   添加如下内容：

   127.0.0.1 registry1

   127.0.0.1 registry2

2. 启动 eureka-server1（7001端口） 和 eureka-server2（7002端口）

3. 复制修改 user-service 的端口配置，分别在两个端口启动 user-service 服务

4. 启动 user-consume 服务，访问地址如下：

    http://localhost:8080/register

   请求参数：

   ```txt
   {
       "username": "qaz",
       "password": "asd"
   }
   ```

    http://localhost:8080/login 

   请求参数：

   ```txt
   {
       "username": "qaz",
       "password": "asd"
   }
   ```