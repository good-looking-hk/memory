package cn.ccsu.music.controller;

import cn.ccsu.music.entity.ShouCang;
import cn.ccsu.music.entity.Music;
import cn.ccsu.music.entity.ResultEntity;
import cn.ccsu.music.entity.User;
import cn.ccsu.music.service.MusicService;
import cn.ccsu.music.util.FileUtil;
import cn.ccsu.music.util.ShiroUtil;
import cn.ccsu.music.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/music")
public class MusicController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MusicService musicService;

    @RequestMapping("/all")
    @ResponseBody
    public HashMap<String, Object> all() {
        List<Music> list = musicService.allMusic();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "ok");
        map.put("count", list.size());
        map.put("data", list);
        return map;
    }

    @GetMapping("/search")
    public String search(String name, Model model) {
        List<Music> list = musicService.queryByName(name);
        model.addAttribute("list", list);
        model.addAttribute("key", name);
        return "search";
    }

    /**
     * 上传歌曲的音频、歌词、封面
     *
     * @param name 可选值为song、lrc、img
     * @param file 音频或歌词或封面
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResultEntity upload(String name, MultipartFile file) throws Exception {
        logger.info("name：{}", name);
        String suffix = file.getOriginalFilename().split("\\.")[1];
        String fileName = "temp." + suffix;
        if ("song".equals(name) && fileName.endsWith(".mp3")) {
            FileUtil.saveSong(fileName, file.getInputStream());
        } else if ("lrc".equals(name) && fileName.endsWith(".lrc")) {
            FileUtil.saveLrc(fileName, file.getInputStream());
        } else if ("img".equals(name) && (fileName.endsWith(".jpg") || fileName.endsWith(".png") || fileName.endsWith(".jpeg"))) {
            // 统一为png后缀，方便管理
            FileUtil.saveImg("temp.png", file.getInputStream());
        } else {
            return ResultEntity.error("格式不正确");
        }
        return ResultEntity.ok(name);
    }

    @PostMapping("/save")
    @ResponseBody
    public ResultEntity save(String author, String name, Integer year) {
        logger.info("author:{},name：{},yaer:{}", author, name, year);
        if (StringUtil.isEmpty(author) || StringUtil.isEmpty(name) || year == null) {
            return ResultEntity.error("参数不正确");
        }
        if (!FileUtil.findFiles()) {
            return ResultEntity.error("请先上传文件");
        }
        musicService.save(author, name, year);
        return ResultEntity.ok();
    }

    @PostMapping("/del")
    @ResponseBody
    public ResultEntity del(Integer id) {
        if (id == null) {
            return ResultEntity.error("参数错误");
        }
        musicService.del(id);
        return ResultEntity.ok();
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResultEntity edit(Integer id, String author, String name, Integer year) {
        logger.info("id:{},author:{},name:{},year", id, author, name, year);
        musicService.edit(id, author, name, year);
        return ResultEntity.ok();
    }

    @GetMapping("/my")
    public String myMusic(HttpServletRequest request) {
        if (request.getSession().getAttribute("user") != null) {
            return "my";
        }
        return "redirect:/";
    }

    @RequestMapping("/myLike")
    @ResponseBody
    public HashMap myLike() {
        User user = ShiroUtil.currentUser();
        List<ShouCang> list = musicService.getByUserId(user.getId());
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "ok");
        map.put("count", list.size());
        map.put("data", list);
        return map;
    }

    @PostMapping("/like")
    @ResponseBody
    public ResultEntity like(Integer id) {
        User user = ShiroUtil.currentUser();
        ShouCang like = new ShouCang();
        like.setMusicId(id);
        like.setUserId(user.getId());
        like.setCreateTime(new Date());
        musicService.addLike(like);
        return ResultEntity.ok();
    }

    @PostMapping("/delLike")
    @ResponseBody
    public ResultEntity delLike(Integer id) {
        musicService.delLike(id);
        return ResultEntity.ok();
    }

}
