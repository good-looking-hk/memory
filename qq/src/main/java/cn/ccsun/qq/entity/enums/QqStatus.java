package cn.ccsun.qq.entity.enums;

/**
 * @author HK
 * @date 2019-02-14 13:48
 */
public enum QqStatus {
    /**
     * 在线，隐身，离线，临时刷新页面
     */
    ONLINE(1, "online"), HIDE(2, "hide"), OFFLINE(3, "offline"), REFRESH(4, "refresh");

    int code;

    String msg;

    QqStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String valueOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (QqStatus m : QqStatus.values()) {
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
