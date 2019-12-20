package cn.ccsun.qq.controller;

import cn.ccsun.qq.dao.BoxMsgRepository;
import cn.ccsun.qq.entity.Friend;
import cn.ccsun.qq.entity.enums.MsgType;
import cn.ccsun.qq.entity.enums.QqStatus;
import cn.ccsun.qq.entity.ResultEntity;
import cn.ccsun.qq.entity.User;
import cn.ccsun.qq.entity.enums.Readed;
import cn.ccsun.qq.entity.layim.*;
import cn.ccsun.qq.service.FriendService;
import cn.ccsun.qq.service.UserService;
import cn.ccsun.qq.util.RedisUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 处理有关用户请求的RestController，使用json交互
 * @author HK
 * @date 2019-01-10 16:45
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private BoxMsgRepository boxMsgRepository;

    /**
     * 获取个人信息，包括好友及分组
     */
    @RequestMapping("/info")
    public Object userList(HttpServletRequest request) {
       Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return "未登录";
        }
        User user = (User) obj;
        // 用户已登录，但是查一次数据库，防止数据过期
        user = userService.find(user.getUserId());

        // 个人信息
        MineMsg mine = new MineMsg(user);
        String status = RedisUtil.getStatus(user.getUserId());
        if (status == null) {
            status = "online";
            RedisUtil.setStatus(user.getUserId(), status);
            String jsonString = "{'id':" + user.getUserId() + ",'type':'" + status + "'}";
            WebSocket.sendGlobalMsg(jsonString);
        }
        mine.setStatus(status);

        // 好友信息
        List<FriendMsg> friends = new ArrayList<>();
        Map<String, List<User>> map = friendService.findFriendGroup(user.getUserId());
        // 默认分组
        if (map.size() == 0) {
            FriendMsg friendMsg = new FriendMsg();
            friendMsg.setGroupname("我的好友");
            friendMsg.setId(1);
            friends.add(friendMsg);
        }
        for (Map.Entry<String, List<User>> e : map.entrySet()) {
            FriendMsg friendMsg = new FriendMsg();
            int index = e.getKey().lastIndexOf("-");
            friendMsg.setGroupname(e.getKey().substring(0, index));
            friendMsg.setId(Integer.valueOf(e.getKey().substring(index + 1)));
            List<MineMsg> temp = new ArrayList<>();
            for (User u : e.getValue()) {
                MineMsg m = new MineMsg(u);
                String status1 = RedisUtil.getStatus(u.getUserId());
                if (!QqStatus.ONLINE.getMsg().equals(status1)) {
                    status1 = "offline";
                }
                m.setStatus(status1);
                temp.add(m);
            }
            friendMsg.setList(temp);
            friends.add(friendMsg);
        }

        GroupMsg groupMsg = new GroupMsg();

        DataMsg dataMsg = new DataMsg();
        dataMsg.setMine(mine);
        dataMsg.setFriend(friends);
        dataMsg.setGroup(null);

        InitMsg initMsg = new InitMsg();
        initMsg.setCode(0);
        initMsg.setData(dataMsg);
        return initMsg;
    }

    /**
     * 查看用户信息
     */
    @RequestMapping("/getUserInfo")
    public ResultEntity getUserInfo(Integer id, String type) {
        if (id == null) {
            return ResultEntity.error("id不能为空");
        }
        User user = userService.find(id);
        ResultEntity r = ResultEntity.ok();
        r.put("type", "friend");
        r.put("data", user);
        return r;
    }

    /**
     * 修改个人信息
     */
    @RequestMapping("/saveInfo")
    public ResultEntity saveInfo(HttpServletRequest request, String userId, String username, String email, String sign) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null || userId == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        if (!user.getUserId().equals(Integer.valueOf(userId))) {
            return ResultEntity.error("非法操作(id:" + userId + ")");
        }
        userService.updateInfo(Integer.valueOf(userId), username, email, sign);
        log.info(userId + "修改了个人信息");
        return ResultEntity.ok();
    }

    /**
     * 删除好友
     */
    @RequestMapping("/delFriend")
    public ResultEntity delFriend(HttpServletRequest request, Integer friendId) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        userService.delFriend(user.getUserId(), friendId);
        userService.delFriend(friendId, user.getUserId());
        log.info("删除好友成功");
        return ResultEntity.ok();
    }

    /**
     * 修改签名
     */
    @RequestMapping("/sign")
    public ResultEntity sign(HttpServletRequest request, String sign) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        System.out.println(user.getUserId() + sign);
        userService.updateSign(user.getUserId(), sign);
        log.info(user.getUserId() + "修改了签名:" + sign);
        return ResultEntity.ok();
    }

    /**
     * 修改在线状态
     */
    @RequestMapping("/online")
    public ResultEntity online(HttpServletRequest request, String status) {
        if (status == null || (!"online".equals(status) && !"hide".equals(status))) {
            return ResultEntity.error("错误参数" + status);
        }
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        String old = RedisUtil.getStatus(user.getUserId());
        if (!status.equals(old)) {
            RedisUtil.setStatus(user.getUserId(), status);
            String jsonString = "{'id':" + user.getUserId() + ",'type':'" + status + "'}";
            WebSocket.sendGlobalMsg(jsonString);
            log.info(user.getUserId() + "修改了在线状态:" + status);
        }
        return ResultEntity.ok();
    }

    @RequestMapping("/userStatus")
    public ResultEntity userStatus(Integer id) {
        String status = RedisUtil.getStatus(id);
        ResultEntity r = ResultEntity.ok();
        r.put("data", status);
        return r;
    }

    /**
     * 查找qq或qq群
     */
    @RequestMapping("/find")
    public Object find(HttpServletRequest request, String qq, Boolean isFriend) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        if (isFriend == null) {
            return ResultEntity.error("参数错误");
        }
        List<User> list = null;
        // 查找好友
        if (isFriend) {
            if (NumberUtil.isInteger(qq)) {
                // qq是数字，那么先查找qq号，后查找用户名
                list = userService.findFriends(Integer.parseInt(qq), qq);
            } else {
                // 不是数字，只查找用户名
                list = userService.findFriends(qq);
            }
        } else {

        }
        return list;
    }

    @RequestMapping("/msgBox")
    public HashMap msgBox(HttpServletRequest request, int page) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        List<BoxMsg> list = userService.boxMsgs(user.getUserId());
        Collections.reverse(list);
        // 一页显示4条
        int start = 0, end = 0, p = 4, pages = list.size() / p;
        if (page > 0 && page < list.size() / p + 1) {
            start = p * (page - 1) + 1;
            end = p * page;
        }
        if (start < end) {
            list = list.subList(start - 1, end);
        }
        // 解决mysql与layui的字段规范冲突
        JSONArray array = new JSONArray();
        for (BoxMsg b : list) {
            JSONObject o = new JSONObject(b);
            o.put("from", b.getFromId());
            o.put("read", b.getReaded());
            User u = userService.find(b.getFromId());
            o.put("avatar", u.getAvatar());
            o.put("sign", u.getSign());
            array.put(o);
        }
        HashMap<String, Object> r = new HashMap<>(8);
        r.put("code", 0);
        r.put("data", array);
        r.put("pages", pages);
        r.put("type", "box");
        log.info("start:" + start + ",end:" + end + ",page:" + page + ",pages:" + pages);
        return r;
    }

    /**
     * 申请添加好友或添加群
     * @param id 被添加好友或群id
     */
    @RequestMapping("/add")
    public ResultEntity addRequest(HttpServletRequest request, Integer id, String remark, boolean isGroup) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        // 添加好友
        if (!isGroup) {
            String status = RedisUtil.getStatus(id);
            BoxMsg b = new BoxMsg();
            b.setUid(id);
            b.setFromId(user.getUserId());
            b.setUsername(user.getUsername());
            b.setRemark(remark);
            b.setReaded(Readed.TO_READ.getCode());
            b.setType(MsgType.ADD_FRIEND.getCode());
            b.setTime(new Date());
            b.setContent("申请添加你为好友");
            userService.saveBoxMsg(b);
            String msg = "{'system':'true', 'type':'box', 'fromid':'" + user.getUserId() + "', 'content':'" + remark + "'}";
            if (status != null) {
                WebSocket.sendMsgToUser(id, msg);
                log.info("添加好友请求已通知好友");
            } else {
                log.info("添加好友请求存入数据库");
            }
        }
        return ResultEntity.ok();
    }

    /**
     * @param msgId 消息id
     * @param id 本人id
     * @param friendId  好友id
     */
    @RequestMapping("/dealFriendReq")
    public ResultEntity dealFriendRequest(HttpServletRequest request, Integer msgId, Integer id, Integer friendId, boolean agree, String groupName, Integer groupId) {
        Object obj = request.getSession().getAttribute("user");
        if (obj == null) {
            return ResultEntity.error("未登录");
        }
        User user = (User) obj;
        if (!user.getUserId().equals(id) || msgId == null) {
            return ResultEntity.error("非法操作");
        }
        if (!agree) {
            boxMsgRepository.refuseAddFriend(msgId);
            log.info("用户" + id + "已拒绝用户" + friendId + "的好友申请");
            return ResultEntity.ok();
        }
        boxMsgRepository.agreeAddFriend(msgId);
        Friend friend = new Friend();
        friend.setUserId(id);
        friend.setFriendId(friendId);
        friend.setGroupName("我的好友");
        friend.setGroupId(1);
        friend.setCreateTime(new Date());
        friendService.save(friend);

        friend = new Friend();
        friend.setUserId(friendId);
        friend.setFriendId(id);
        friend.setGroupName("我的好友");
        friend.setGroupId(1);
        friend.setCreateTime(new Date());
        friendService.save(friend);
        log.info("用户" + id + "已同意用户" + friendId + "的好友申请");
        return ResultEntity.ok();
    }
}
