package cn.ccsun.qq.controller;

import cn.ccsun.qq.entity.enums.QqStatus;
import cn.ccsun.qq.util.RedisUtil;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HK
 * @date 2019-01-10 19:12
 */
@Component
@ServerEndpoint("/socket/{userId}")
public class WebSocket {

    private static Logger logger = LoggerFactory.getLogger(WebSocket.class);

    private static final ConcurrentHashMap<Integer, Session> US_MAP = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<Session, Integer> SU_MAP = new ConcurrentHashMap<>();

    /**
     * 发送给特定用户消息
     */
    public static boolean sendMsgToUser(Integer userId, String msg) {
        if (RedisUtil.getStatus(userId) != null && US_MAP.containsKey(userId)) {
            Session s = US_MAP.get(userId);
            if (s != null && s.isOpen()) {
                s.getAsyncRemote().sendText(msg);
                return true;
            }
        }
        return false;
    }

    /**
     * 发送全局消息
     */
    public static void sendGlobalMsg(String msg) {
        for (Session s : US_MAP.values()) {
            if (s != null && s.isOpen()) {
                s.getAsyncRemote().sendText(msg);
            }
        }
    }

    /**
     * 建立连接
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) throws IOException {
        // 如该账号已登录，则原账号会被挤下线
        US_MAP.put(userId, session);
        SU_MAP.put(session, userId);
        List<String> list = RedisUtil.getMsg(userId);
        // 可以优化
        if (list != null) {
            for (String str : list) {
                for (Session s : US_MAP.values()) {
                    synchronized (s) {
                        // 同步发送，getAsyncRemote实时异步发送
                        s.getBasicRemote().sendText(str);
                    }
                }
            }
        }
        logger.info("用户 " + userId + " 连接成功，当前在线人数为 " + US_MAP.size());
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose(Session session) {
        Integer userId = SU_MAP.get(session);
        if (userId != null && userId > 0) {
            // 如果当前状态为隐身，那么需要特别处理刷新页面的情况
            if (QqStatus.HIDE.getMsg().equals(RedisUtil.getStatus(userId))) {
                RedisUtil.setStatus(userId, QqStatus.REFRESH.getMsg());
            } else {
                RedisUtil.setStatus(userId, QqStatus.OFFLINE.getMsg());
            }
            String jsonString = "{'id':" + userId + ",'type':'offline'}";
            sendGlobalMsg(jsonString);
            US_MAP.remove(userId);
            SU_MAP.remove(session);
            logger.info("用户" + userId + "断开连接，当前在线人数为" + US_MAP.size());
        }
    }

    /**
     * 收到消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("服务器收到消息" + message);
        JSONObject jsonObject = new JSONObject(message);
        JSONObject to = jsonObject.getJSONObject("to");
        JSONObject from = jsonObject.getJSONObject("mine");
        String type = to.getStr("type");
        String toUserStatus = RedisUtil.getStatus(to.getInt("id"));
        switch (type) {
            case "friend":
                JSONObject temp = new JSONObject();
                temp.put("username", from.getStr("username"));
                temp.put("avatar", from.getStr("avatar"));
                temp.put("id", from.getInt("id"));
                temp.put("fromid", from.getInt("id"));
                temp.put("type", "friend");
                temp.put("content", from.getStr("content"));
                temp.put("timestamp", System.currentTimeMillis());
                if (QqStatus.ONLINE.getMsg().equals(toUserStatus) && US_MAP.containsKey(to.getInt("id"))) {
                    Session s = US_MAP.get(to.getInt("id"));
                    synchronized (s) {
                        if (s != null && s.isOpen()) {
                            logger.info("向用户" + to.getInt("id") + "发送消息");
                            s.getAsyncRemote().sendText(temp.toString());
                        }
                    }
                } else {
                    logger.info("消息存入缓存");
                    RedisUtil.setMsg(to.getInt("id"), temp.toString());
                }
            default:
                break;
        }

//        if(type.equals("onlineStatus")){
//            for(Session s:session.getOpenSessions()){		//循环发给所有在线的人
//                JSONObject toMessage=new JSONObject();
//                toMessage.put("id", jsonObject.getJSONObject("mine").getStr("id"));
//                toMessage.put("content", jsonObject.getJSONObject("mine").getStr("content"));
//                toMessage.put("type",type);
//                s.getAsyncRemote().sendText(toMessage.toString());
//            }
//        }else{
//            int toId=jsonObject.getJSONObject("to").getInt("id");
//            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
//            Date date = new Date();
//            String time=df.format(date);
//            jsonObject.put("time", time);
//            JSONObject toMessage=new JSONObject();
//            toMessage.put("avatar", jsonObject.getJSONObject("mine").getStr("avatar"));
//            toMessage.put("type",type);
//            toMessage.put("content", jsonObject.getJSONObject("mine").getStr("content"));
//            toMessage.put("timestamp",date.getTime());
//            toMessage.put("time",time);
//            toMessage.put("mine",false);
//            toMessage.put("username",jsonObject.getJSONObject("mine").getStr("username"));
//            if(type.equals("friend")||type.equals("fankui")){
//                toMessage.put("id", jsonObject.getJSONObject("mine").getInt("id"));
//            }else{
//                toMessage.put("id", jsonObject.getJSONObject("to").getInt("id"));
//            }

//            switch (type) {
//                case "friend":           							 //单聊,记录到mongo
//                    if(mapUS.containsKey(toId+"")){               //如果在线，及时推送
//                        mapUS.get(toId+"").getAsyncRemote().sendText(toMessage.toString());               //发送消息给对方
//                        logger.info("单聊-来自客户端的消息:" + toMessage.toString());
//                    }else{                                        //如果不在线 就记录到数据库，下次对方上线时推送给对方。
//                        RedisUtils.lpush(toId + "_msg", toMessage.toString());
//                        logger.info("单聊-对方不在线，消息已存入redis:" + toMessage.toString());
//                    }
//                    break;
//                case "group":
//                    JSONArray memberList=JSONArray.fromObject(qunWS.getSimpleMemberByGroupId(toId));  //获取群成员userId列表
//                    if(memberList.size()>0){
//                        for(int i=0;i<memberList.size();i++){                            //发送到在线用户(除了发送者)
//                            if(mapUS.containsKey(memberList.get(i)) && !memberList.get(i).equals(jsonObject.getJSONObject("mine").getInt("id")+"")){
//                                session=mapUS.get(memberList.get(i));
//                                session.getAsyncRemote().sendText(toMessage.toString());
//                                logger.info("群聊-来自客户端的消息:" + toMessage.toString());
//                            }else if(memberList.get(i).equals(jsonObject.getJSONObject("mine").getInt("id")+"")){
//                                //如果是发送者自己，不做任何操作。
//                            }else{    	//如果是离线用户,数据存到redis待用户上线后推送。
//                                RedisUtils.lpush(memberList.get(i) + "_msg", toMessage.toString());
//                            }
//                        }
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
    }

    /**
     * 发生错误
     */
    @OnError
    public void onError(Session session, Throwable error) {
        Integer userId = SU_MAP.get(session);
        if (userId != null && userId > 0) {
            RedisUtil.setStatus(userId, "offline");
            String jsonString = "{'content':'offline','id':" + userId + ",'type':'offline'}";
            for (Session s : session.getOpenSessions()) {
                if (s.isOpen()) {
                    s.getAsyncRemote().sendText(jsonString);
                }
            }
            US_MAP.remove(userId);
            SU_MAP.remove(session);
            logger.info("用户" + userId + "断开连接！ 当前在线人数为" + US_MAP.size());
        }
        logger.error("连接发生错误!");
        error.printStackTrace();
    }
}
