package cn.ccsun.qq.entity.layim;

import cn.ccsun.qq.entity.User;

import java.io.Serializable;

/**
 * @author HK
 * @date 2019-01-10 16:51
 */
public class MineMsg implements Serializable {

    private Integer id;

    private String username;

    /**
     * 只能为online或hide
     */
    private String status;

    private String sign;

    private String avatar;

    public MineMsg() {
    }

    public MineMsg(User user) {
        this.username = user.getUsername();
        this.id = user.getUserId();
        this.sign = user.getSign();
        this.avatar = user.getAvatar();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
