package cn.ccsun.qq.entity.layim;

import java.io.Serializable;

/**
 * @author HK
 * @date 2019-01-10 17:02
 */
public class GroupMsg implements Serializable {

    private Integer id;

    private String groupname;

    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
