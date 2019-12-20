package cn.ccsun.qq.entity.layim;

import java.io.Serializable;
import java.util.List;

/**
 * @author HK
 * @date 2019-01-10 16:49
 */
public class DataMsg implements Serializable {

    private MineMsg mine;

    private List<FriendMsg> friend;

    private List<GroupMsg> group;

    public MineMsg getMine() {
        return mine;
    }

    public void setMine(MineMsg mine) {
        this.mine = mine;
    }

    public List<FriendMsg> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendMsg> friend) {
        this.friend = friend;
    }

    public List<GroupMsg> getGroup() {
        return group;
    }

    public void setGroup(List<GroupMsg> group) {
        this.group = group;
    }
}
