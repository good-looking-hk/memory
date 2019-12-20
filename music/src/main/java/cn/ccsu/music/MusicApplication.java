package cn.ccsu.music;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * layui aplayer jquery spring boot shiro thymeleaf
 * http://www.ityouknow.com/springboot/2016/05/01/spring-boot-thymeleaf.html
 */
@SpringBootApplication
@MapperScan("cn.ccsu.music.mapper")
public class MusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicApplication.class, args);
    }
}
