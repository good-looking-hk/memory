package cn.ccsu.music.mapper;

import cn.ccsu.music.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author HK
 * @date 2019-02-20 13:53
 */
public interface UserMapper {

    @Select("SELECT * FROM user")
    List<User> getAll();

    @Select("SELECT * FROM user WHERE phone = #{phone}")
    User findByPhone(String phone);

    @Insert("INSERT INTO user(username,password,phone,role_name,create_time) VALUES(#{username}, #{password}, #{phone}, #{roleName}, #{createTime})")
    int insert(User user);

    @Update("UPDATE users SET username = #{username} WHERE id =#{id}")
    int update(User user);

    @Delete("DELETE FROM users WHERE id =#{id}")
    int delete(Integer id);
}
