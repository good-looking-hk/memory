package cn.ccsun.qq.dao;

import cn.ccsun.qq.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author HK
 * @date 2019-01-24 20:59
 */
public interface FriendRepository extends JpaRepository<Friend,Integer> {

    /**
     * 分组好友
     */
    List<Friend> findFriendsByUserIdOrderByGroupName(Integer userId);

    @Transactional
    int deleteFriendByUserIdAndFriendId(Integer userId, Integer friendId);
}
