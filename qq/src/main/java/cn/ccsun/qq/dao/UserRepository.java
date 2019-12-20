package cn.ccsun.qq.dao;

import cn.ccsun.qq.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author HK
 * @date 2019-01-10 01:14
 */
public interface UserRepository extends JpaRepository<User,Integer> {

    /**
     * 这一句等于select * from user where username = ?
     */
    User getUserByUsername(String username);

    User getUserByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update user set sign = ?2 where user_id = ?1", nativeQuery = true)
    int updateSign(Integer userId, String sign);

    @Transactional
    @Modifying
    @Query(value = "update user set username = ?2, email = ?3, sign = ?4 where user_id = ?1", nativeQuery = true)
    int updateInfo(Integer userId, String username, String email, String sign);

    List<User> findUsersByUserIdIsLikeOrUsernameIsLike(Integer userId, String username);

    List<User> findUsersByUsernameIsLike(String username);
}
