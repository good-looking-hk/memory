package cn.ccsun.qq.util;

import cn.ccsun.qq.entity.enums.QqStatus;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author HK
 * @date 2019-02-14 13:24
 */
public class RedisUtil {

    private static final String SUFFIX_STATUS = "_status", SUFFIX_MSG = "_msg";

    private static final int ONE_HOUR = 60 * 60;

    private static final int ONE_WEEK = 7 * 24 * ONE_HOUR;

    private static final String REPLAY_OK = "OK";

    /**
     * 内部类单例，按需加载
     */
    private static class RedisInstance {
        private static Jedis jedis = new Jedis("localhost");
    }

    private static Jedis getRedis() {
        return RedisInstance.jedis;
    }

    /**
     * 更新状态，暂定为在线，离线，隐身
     */
    public static boolean setStatus(Integer userId, String status) {
        Jedis jedis = getRedis();
        if (status.equals(QqStatus.ONLINE.getMsg()) || status.equals(QqStatus.HIDE.getMsg()) ) {
            String r = jedis.setex(userId + SUFFIX_STATUS, ONE_HOUR, status);
            if (r.equals(REPLAY_OK)) {
                return true;
            }
            return false;
        } else if (status.equals(QqStatus.REFRESH.getMsg())) {
            // 半离线状态，状态保存6秒，用于区分刷新页面
            jedis.setex(userId + SUFFIX_STATUS, 6, QqStatus.HIDE.getMsg());
        } else  {
            jedis.del(userId + SUFFIX_STATUS);
        }
        return true;
    }

    /**
     * 如果不存在则返回null
     */
    public static String getStatus(Integer userId) {
        Jedis jedis = getRedis();
        return jedis.get(userId + SUFFIX_STATUS);
    }

    /**
     * 存储离线消息
     */
    public static void setMsg(Integer userId, String msg) {
        Jedis jedis = getRedis();
        jedis.rpush(userId + SUFFIX_MSG, msg);
        jedis.expire(userId + SUFFIX_MSG, ONE_WEEK);
    }

    /**
     * 获取所有离线消息
     */
    public static List<String> getMsg(Integer userId) {
        Jedis jedis = getRedis();
        List<String> list = jedis.lrange(userId + SUFFIX_MSG, 0, -1);
        jedis.del(userId + SUFFIX_MSG);
        return list;
    }

    private RedisUtil() {
    }

    public static void main(String[] args) {
        RedisUtil.setStatus(1, "online");
        System.out.println(RedisUtil.getStatus(2));
    }
}
