package cn.ccsun.qq.service;

import cn.ccsun.qq.dao.FriendRepository;
import cn.ccsun.qq.dao.UserRepository;
import cn.ccsun.qq.entity.Friend;
import cn.ccsun.qq.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author HK
 * @date 2019-01-24 21:08
 */
@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(Friend friend) {
        friendRepository.save(friend);
    }

    /**
     * 好友分组
     */
    public Map<String, List<User>> findFriendGroup(Integer userId) {
        Map<String, List<User>> map = new HashMap<>(6);
        List<Friend> friends = friendRepository.findFriendsByUserIdOrderByGroupName(userId);
        String newTag = null;
        List<User> temp = null;
        Iterator<Friend> i = friends.iterator();
        while (i.hasNext()) {
            Friend f = i.next();
            if (!(f.getGroupName() + "-" + f.getGroupId()).equals(newTag)) {
                if (temp != null) {
                    map.put(newTag, temp);
                }
                temp = new ArrayList<>();
                temp.add(userRepository.getOne(f.getFriendId()));
                newTag = f.getGroupName() + "-" + f.getGroupId();
            } else {
                temp.add(userRepository.getOne(f.getFriendId()));
            }
        }
        // 针对最后一组做额外添加操作
        if (newTag != null) {
            map.put(newTag, temp);
        }
        return map;

    }

}
