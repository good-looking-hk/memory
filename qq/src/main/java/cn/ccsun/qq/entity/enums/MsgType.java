package cn.ccsun.qq.entity.enums;

/**
 * @author HK
 * @date 2019-02-19 13:37
 */
public enum MsgType {
    /**
     *
     */
    ADD_FRIEND(1, "申请添加好友"), AGREE_ADD(2, "同意添加好友"), REFUSE_ADD(3, "拒绝添加好友"), SYSTEM(4, "系统消息");

    int code;

    String msg;

    MsgType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String valueOf(Integer code) {
        if (code == null) {
            return null;
        }
        for (MsgType m : MsgType.values()) {
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
