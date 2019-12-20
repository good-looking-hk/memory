package cn.ccsu.music.config;

import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.apache.shiro.session.mgt.AbstractSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT;
import static org.apache.shiro.session.mgt.AbstractValidatingSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;

/**
 * @author HK
 * @date 2019-02-20 14:02
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroDbRealm getShiroDbRealm() {
        ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
        // 是否对realm处理的结果进行缓存，这样可以避免重复执行realm中的权限验证
        // 由于已经在realm中实现了权限的缓存，所以这一步没必要
        // 简单的缓存实现可以使用MemoryConstrainedCacheManager
        shiroDbRealm.setCachingEnabled(false);
        return shiroDbRealm;
    }

    /**
     * shiro的核心,安全管理器，需要依赖注入的缓存管理器(目前使用的是ehcache，可以改为redis注入)
     */
    @Bean
    public DefaultWebSecurityManager securityManager(WebSessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(this.getShiroDbRealm());
        // 针对session做缓存，这里可以为扩展为分布式session
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // 登陆访问url,默认为/login.jsp(一般情况下建议主动修改)
        shiroFilter.setLoginUrl("/index");
        // 没有权限跳转的url
        shiroFilter.setUnauthorizedUrl("/login");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/register", "anon");
        filterMap.put("/login", "anon");

        // 放行静态资源
        filterMap.put("/aplayer/**", "anon");
        filterMap.put("/libs/**", "anon");
        filterMap.put("/html/**", "anon");
        filterMap.put("/css/**", "anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/img/**", "anon");
        filterMap.put("/music/**.mp3", "anon");
        filterMap.put("/lrc/**", "anon");
        filterMap.put("/**/*/.gif", "anon");
        filterMap.put("/**", "anon");
//        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean
    public DefaultWebSessionManager getDefaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        LinkedList<SessionListener> list = new LinkedList<>();
        // 添加监听
        sessionManager.setSessionListeners(list);
        // session失效时间，单位秒,shiro默认60分钟
        sessionManager.setSessionValidationInterval(DEFAULT_SESSION_VALIDATION_INTERVAL);
        // 多久检测一次失效的session,shiro默认30分钟
        sessionManager.setGlobalSessionTimeout(DEFAULT_GLOBAL_SESSION_TIMEOUT);
        // 网上找的，处理JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        // 删除失效session
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }


}
