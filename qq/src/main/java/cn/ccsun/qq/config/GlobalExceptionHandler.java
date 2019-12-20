package cn.ccsun.qq.config;


import cn.ccsun.qq.entity.ResultEntity;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局控制器异常拦截器，优先级越高值越低
 * @author HK
 * @date 2018-05-31 01:37
 */
@ControllerAdvice
@Order(-1)
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResultEntity npe(RuntimeException e) {
        e.printStackTrace();
        return ResultEntity.error("非法操作:" + e.getMessage());
    }
}