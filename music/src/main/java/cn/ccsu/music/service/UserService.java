package cn.ccsu.music.service;

import cn.ccsu.music.entity.User;
import cn.ccsu.music.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author HK
 * @date 2019-02-20 13:46
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> all() {
        return userMapper.getAll();
    }

    public User login(String phone, String password) {
        User user = userMapper.findByPhone(phone);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }

    public boolean save(String phone, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setRoleName("普通用户");
        userMapper.insert(user);
        return true;
    }
}
