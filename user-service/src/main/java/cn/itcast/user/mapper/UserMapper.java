package cn.itcast.user.mapper;

import cn.itcast.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author TSG-admin
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-09-13 16:52:38
* @Entity cn.itcast.user.entity.User
*/
@Mapper
public interface UserMapper {

    User getByUsernameAndPassword(@Param("userName") String userName,
                                  @Param("password") String password);

    void save(User user);

}




