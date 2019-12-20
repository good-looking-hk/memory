package cn.ccsu.music;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * @author HK
 * @date 2019-04-13 14:57
 */
@Component
public class TestSpring implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        System.out.println(path);
    }
}
