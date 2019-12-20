package cn.ccsu.music.controller;

import cn.ccsu.music.entity.LiuYan;
import cn.ccsu.music.entity.ResultEntity;
import cn.ccsu.music.entity.User;
import cn.ccsu.music.mapper.LiuYanMapper;
import cn.ccsu.music.util.TimeUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller()
@RequestMapping("/liuyan")
public class LiuYanController {

    @Autowired
    private LiuYanMapper liuYanMapper;

    @RequestMapping(value = "/all")
    public String all(Model model) {
        List<LiuYan> list = liuYanMapper.getAll();
        model.addAttribute("list", list);
        return "liuyan";
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResultEntity add(String content) {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        LiuYan liuyan = new LiuYan();
        liuyan.setContent(content);
        liuyan.setCreateTime(TimeUtil.getCurrentDate());
        liuyan.setName(user.getUsername());
        liuYanMapper.insert(liuyan);
        return ResultEntity.ok();
    }
}
