package cn.ccsun.qq.entity.layim;

import java.io.Serializable;
import java.util.List;

/**
 * @author HK
 * @date 2019-01-10 16:55
 */
public class FriendMsg implements Serializable {

    private Integer id;

    private String groupname;

    private List<MineMsg> list;

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

    public List<MineMsg> getList() {
        return list;
    }

    public void setList(List<MineMsg> list) {
        this.list = list;
    }
}
