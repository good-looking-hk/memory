package cn.ccsu.music.controller;

import cn.ccsu.music.entity.ResultEntity;
import cn.ccsu.music.entity.User;
import cn.ccsu.music.service.MusicService;
import cn.ccsu.music.service.UserService;
import cn.ccsu.music.util.ShiroUtil;
import cn.ccsu.music.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private MusicService musicService;

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("list", musicService.allMusic());
        return "index";
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResultEntity login(String phone, String password, HttpServletRequest request) {
        logger.debug("phone:{},password:{}", phone, password);
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(password)) {
            return ResultEntity.error("用户名或密码不能为空");
        }
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(phone, password.toCharArray());
        try {
            currentUser.login(token);
        } catch (UnknownAccountException | IncorrectCredentialsException e) {
            return ResultEntity.error("账号或密码错误");
        }
        User user = ShiroUtil.currentUser();
        request.getSession().setAttribute("user", user);
        return ResultEntity.ok().put("username", user.getUsername());
    }


    @PostMapping(value = "/register")
    @ResponseBody
    public ResultEntity register(String phone, String password, String username) {
        logger.debug("phone:{},password:{},username:{}", phone, password, username);
        if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(password) || StringUtil.isEmpty(username)) {
            return ResultEntity.error("注册输入不能存在空值");
        }
        User user = userService.findByPhone(phone);
        if (user != null) {
            return ResultEntity.error("该手机号已被注册");
        }
        boolean success = userService.save(phone, password, username);
        if (!success) {
            return ResultEntity.error("保存用户失败");
        }
        return ResultEntity.ok();
    }

    @PostMapping(value ="/logout")
    @ResponseBody
    public ResultEntity logout(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();
        return ResultEntity.ok();
    }

    @GetMapping("/manage")
    public String manage(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "manage";
        }
        return "redirect:/";
    }

    @GetMapping("/liuyan")
    public String liuyan(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "liuyan";
        }
        return "redirect:/";
    }
}
