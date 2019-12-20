package cn.ccsun.qq.entity.enums;

/**
 * @author HK
 * @date 2019-02-19 13:41
 */
public enum Readed {

    /**
     *
     */
    TO_READ(0, "未读"), READED(2, "已读"), OUT_OF_TIME(3, "过期消息");

    int code;

    String msg;

    Readed(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String valueOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (Readed m : Readed.values()) {
            if (m.getCode() == code) {
                return m.getMsg();
            }
        }
        return null;
    }

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
}
