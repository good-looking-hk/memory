package cn.ccsu.music.mapper;

import cn.ccsu.music.entity.Music;
import cn.ccsu.music.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author HK
 * @date 2019-04-12 19:09
 */
public interface MusicMapper {

    @Select("SELECT * FROM music WHERE id = #{id}")
    Music getById(Integer id);

    @Select("SELECT * FROM music")
    List<Music> getAll();

    @Select("SELECT * FROM music where name like #{name} or author like #{name}")
    List<Music> queryByName(String name);

    @Insert("INSERT INTO music(name,author,year,upload_time) VALUES(#{name}, #{author}, #{year}, #{uploadTime})")
    @Options(keyColumn="id",keyProperty="id",useGeneratedKeys=true)
    int insert(Music music);

    @Update("UPDATE music SET author = #{author}, name = #{name}, year = #{year} WHERE id =#{id}")
    int update(Music music);

    @Delete("DELETE FROM music WHERE id =#{id}")
    int delete(Integer id);

}
