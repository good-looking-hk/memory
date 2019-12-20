package cn.ccsu.music.mapper;

import cn.ccsu.music.entity.LiuYan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HK
 * @date 2019-04-15 21:43
 */
public interface LiuYanMapper {

    @Select("SELECT * FROM liuyan")
    List<LiuYan> getAll();

    @Insert("INSERT INTO liuyan(name,content,create_time) VALUES(#{name}, #{content}, #{createTime})")
    int insert(LiuYan liuYan);
}
