package cn.itcast.user.service;

import cn.itcast.user.entity.User;

/**
* @author DELL
* @description 针对表【user】的数据库操作Service
* @createDate 2023-09-10 18:59:34
*/
public interface UserService{

    User getByUsernameAndPassword(String userName, String md5DigestAsHex);

    void save(User user);
}
