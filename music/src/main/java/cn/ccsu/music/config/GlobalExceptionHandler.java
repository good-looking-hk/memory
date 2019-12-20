package cn.ccsu.music.config;

import cn.ccsu.music.entity.ResultEntity;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * 全局控制器异常拦截器，优先级越高值越低
 * 结果可返回一个对象（@ResponseBody），会转化成相应的字符串，亦可返回一个字符串，返回相应的界面，并携带信息
 * @author HK
 * @date 2018-05-31 01:37
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultEntity exception(Exception e) {
        e.printStackTrace();
        return ResultEntity.error(e.getMessage() + " cause by " + e.getCause());
    }
}