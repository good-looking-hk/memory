package cn.ccsun.qq.entity;

import java.util.HashMap;

/**
 * spring boot会自动序列化
 * @author HK
 * @date 2019-01-10 00:54
 */
public class ResultEntity extends HashMap<String, Object> {

    public ResultEntity(Integer code, String msg) {
        super(4);
        put("code", code);
        put("msg", msg);
    }

    public static ResultEntity error(String msg) {
        return new ResultEntity(500, msg);
    }

    /**
     * {"code":200,"msg":"ok"}
     */
    public static ResultEntity ok() {
        return new ResultEntity(200, "ok");
    }
}
