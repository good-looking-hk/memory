package cn.ccsu.music.config;

import cn.ccsu.music.entity.User;
import cn.ccsu.music.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author HK
 * @date 2019-02-20 14:02
 */
public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 权限认证
     */
    @Override
    @SuppressWarnings("unchecked")
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        // 依次根据用户名，邮箱，手机号进行用户查找
        User user = userService.findByPhone(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException();
        }
        // 密码校对
        String password = user.getPassword();
        if (!password.equals(user.getPassword())) {
            throw new IncorrectCredentialsException();
        }
        // 参数分别为登录后的subject,原始密码，原始密码盐，realm名字,shiro本身需要一次密码验证
        return new SimpleAuthenticationInfo(user, password, null, getName());
    }

}
