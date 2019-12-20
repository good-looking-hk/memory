package cn.ccsu.music.service;

import cn.ccsu.music.entity.ShouCang;
import cn.ccsu.music.entity.Music;
import cn.ccsu.music.mapper.ShouCangMapper;
import cn.ccsu.music.mapper.MusicMapper;
import cn.ccsu.music.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author HK
 * @date 2019-04-12 19:08
 */
@Service
public class MusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private ShouCangMapper likeMapper;

    public Music getById(Integer id) {
        return musicMapper.getById(id);
    }

    public List<Music> queryByName(String name) {
        return musicMapper.queryByName("%" + name + "%");
    }

    public List<Music> allMusic() {
        return musicMapper.getAll();
    }

    public void save(String author, String name, Integer year) {
        Music music = new Music();
        music.setAuthor(author);
        music.setName(name);
        music.setYear(year);
        music.setUploadTime(new Date());
        musicMapper.insert(music);
        // 数据库自增生成的主键id
        int id = music.getId();
        FileUtil.renameById(id);
    }

    public void edit(Integer id, String author, String name, Integer year) {
        Music music = new Music();
        music.setId(id);
        music.setAuthor(author);
        music.setName(name);
        music.setYear(year);
        musicMapper.update(music);
    }

    public void del(Integer id) {
        int row = musicMapper.delete(id);
        if (row == 1) {
            FileUtil.delFiles(id);
        }
    }

    public List<ShouCang> getByUserId(Integer userId) {
        List<ShouCang> list = likeMapper.getByUserID(userId);
        for (ShouCang s : list) {
            Music m = getById(s.getMusicId());
            s.setSongInfo(m.getAuthor() + "-" + m.getName() + "-" + m.getYear());
        }
        return list;
    }

    public void addLike(ShouCang like) {
        likeMapper.insert(like);
    }

    public void delLike(Integer id) {
        likeMapper.delete(id);
    }
}
