package cn.ccsu.music.util;

import cn.ccsu.music.entity.User;
import org.apache.shiro.SecurityUtils;

/**
 * @author HK
 * @date 2019-04-16 14:34
 */
public class ShiroUtil {

    public static User currentUser() {
        return (User)SecurityUtils.getSubject().getPrincipal();
    }
}
