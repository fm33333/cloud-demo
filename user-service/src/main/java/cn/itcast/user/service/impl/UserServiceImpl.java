package cn.itcast.user.service.impl;

import cn.itcast.user.entity.User;
import cn.itcast.user.mapper.UserMapper;
import cn.itcast.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-09-10 18:59:34
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getByUsernameAndPassword(String userName, String md5DigestAsHex) {
        return userMapper.getByUsernameAndPassword(userName, md5DigestAsHex);
    }

    @Override
    public void save(User user) {
        userMapper.save(user);
    }
}




