package cn.ccsun.qq.service;

import cn.ccsun.qq.dao.BoxMsgRepository;
import cn.ccsun.qq.dao.FriendRepository;
import cn.ccsun.qq.dao.UserRepository;
import cn.ccsun.qq.entity.User;
import cn.ccsun.qq.entity.layim.BoxMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HK
 * @date 2019-01-10 01:12
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private BoxMsgRepository boxMsgRepository;

    public User find(Integer userId) {
        return userRepository.getOne(userId);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void updateSign(Integer userId, String sign) {
        userRepository.updateSign(userId, sign);
    }

    public void updateInfo(Integer userId, String username, String email, String sign) {
        userRepository.updateInfo(userId, username, email, sign);
    }

    public void delFriend(Integer userId, Integer friendId) {
        friendRepository.deleteFriendByUserIdAndFriendId(userId, friendId);
    }

    public List<User> findFriends(Integer userId, String username) {
        return userRepository.findUsersByUserIdIsLikeOrUsernameIsLike(userId, "%" + username + "%");
    }

    public List<User> findFriends(String username) {
        return userRepository.findUsersByUsernameIsLike( "%" + username + "%");
    }

    public List<BoxMsg> boxMsgs(Integer userId) {
        return boxMsgRepository.findAllByUid(userId);
    }

    public void saveBoxMsg(BoxMsg boxMsg) {
        boxMsgRepository.save(boxMsg);
    }
}


