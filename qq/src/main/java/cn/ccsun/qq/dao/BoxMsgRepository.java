package cn.ccsun.qq.dao;

import cn.ccsun.qq.entity.layim.BoxMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author HK
 * @date 2019-02-18 19:49
 */
public interface BoxMsgRepository extends JpaRepository<BoxMsg, Integer> {

    List<BoxMsg> findAllByUid(Integer uid);

    @Transactional
    @Modifying
    @Query(value = "update box_msg set readed = 1, type = 2 where id = ?1", nativeQuery = true)
    int agreeAddFriend(Integer id);

    @Transactional
    @Modifying
    @Query(value = "update box_msg set readed = 1, type = 3 where id = ?1", nativeQuery = true)
    int refuseAddFriend(Integer id);

}
