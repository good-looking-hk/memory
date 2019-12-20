package cn.ccsun.qq.entity.layim;

import java.io.Serializable;

/**
 * @author HK
 * @date 2019-01-10 16:47
 */
public class InitMsg implements Serializable {

    private int code;

    private String msg;

    private DataMsg data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataMsg getData() {
        return data;
    }

    public void setData(DataMsg data) {
        this.data = data;
    }
}
