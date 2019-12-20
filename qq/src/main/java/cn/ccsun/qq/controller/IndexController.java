package cn.ccsun.qq.controller;

import cn.ccsun.qq.entity.ResultEntity;
import cn.ccsun.qq.entity.User;
import cn.ccsun.qq.service.UserService;
import cn.ccsun.qq.util.Md5Util;
import cn.ccsun.qq.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author HK
 * @date 2019-01-09 23:34
 */
@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    /**
     * 注册界面
     */
    @GetMapping("/register")
    public String register(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "forward:/";
        }
        return "register";
    }

    /**
     * 处理注册请求
     */
    @PostMapping("/register")
    @ResponseBody
    public ResultEntity register1(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        if (StringUtil.isEmpty(username)) {
            return ResultEntity.error("用户名不能为空");
        }
        if (StringUtil.isEmpty(password)) {
            return ResultEntity.error("密码不能为空");
        }
        if (StringUtil.isEmpty(email)) {
            return ResultEntity.error("邮箱不能为空");
        }
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResultEntity.error("该用户名已被占用");
        }
        user = userService.getUserByEmail(email);
        if (user != null) {
            return ResultEntity.error("该邮箱已被占用");
        }
        user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setCreateTime(new Date());
        user.setAvatar("/img/default.png");
        user.setSign("暂无签名");
        user.setPassword(Md5Util.md5(password));
        userService.save(user);
        return ResultEntity.ok();
    }

    /**
     * 登录界面
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "forward:/";
        }
        return "login";
    }

    /**
     * 处理登录请求
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultEntity login1(HttpServletRequest request) {
        String username = request.getParameter("loginName");
        String password = request.getParameter("password");
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            return ResultEntity.error("账号或密码不能为空");
        }
        User user = userService.getUserByUsername(username);
        if (user == null) {
            user = userService.getUserByEmail(username);
            if (user == null) {
                return ResultEntity.error("账号不存在");
            }
        }
        // md5加密，不可逆加密
        if (!Md5Util.md5(password).equals(user.getPassword())) {
            return ResultEntity.error("密码错误");
        }
        request.getSession().setAttribute("user", user);
        return ResultEntity.ok();
    }

    /**
     * 注销请求，返回到登录界面
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().removeAttribute("user");
        }
        return "login";
    }

    /**
     * 主界面
     */
    @GetMapping({"/", "/index"})
    public String index(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "login";
        }
        return "index";
    }
}
