package cn.ccsu.music.controller;

import cn.ccsu.music.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HK
 * @date 2019-04-12 19:55
 */
@Controller
public class UserController {

    /**
     * 根据用户角色跳转至不同的页面
     */
    @GetMapping("/user")
    public String index() {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if ("管理员".equals(user.getRoleName())) {
            return "manage";
        }
        return "my";
    }



}
