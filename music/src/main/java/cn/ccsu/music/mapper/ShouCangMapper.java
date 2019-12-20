package cn.ccsu.music.mapper;

import cn.ccsu.music.entity.ShouCang;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author HK
 * @date 2019-04-16 14:25
 */
public interface ShouCangMapper {

    @Select("SELECT * FROM shoucang WHERE user_id = #{userId}")
    List<ShouCang> getByUserID(Integer userId);

    @Insert("INSERT INTO shoucang(user_id,music_id,create_time) VALUES(#{userId}, #{musicId}, #{createTime})")
    int insert(ShouCang like);

    @Delete("DELETE FROM shoucang WHERE id =#{id}")
    int delete(Integer id);
}
