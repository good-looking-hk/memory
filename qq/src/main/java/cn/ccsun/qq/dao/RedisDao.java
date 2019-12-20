package cn.ccsun.qq.dao;

import cn.ccsun.qq.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author HK
 * @date 2019-01-23 08:59
 */
@Repository
public class RedisDao {

    private static Logger log = LoggerFactory.getLogger(RedisDao.class);

    private final JedisPool jedisPool;

    private static final String PREFIX = "~123$543#", SUFFIX = "*098@890";

    /**
     * 缓存获取时间，默认1小时
     */
    private static final int TIME_OUT_SEC = 60 * 60;

    public RedisDao(@Value("${spring.redis.host}") String ip, @Value("${spring.redis.port}") int port) {
        jedisPool = new JedisPool(ip, port);
        log.info("已成功连接到redis");
    }

    public String put(String k, Object v) {
        return put(k, v, false);
    }

    public String put(Integer k, Object v) {
        return put(String.valueOf(k), v, false);
    }

    public <T> T get(String k, Class<T> cls) {
        return get(k, false, cls);
    }

    public <T> T get(Integer k, Class<T> cls) {
        return get(String.valueOf(k), false, cls);
    }

    public String put(String k, Object v, boolean needSalt) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = !needSalt ? k : PREFIX + k + SUFFIX;
            byte[] bytes = SerializeUtil.serialize(v);
            log.debug("将对象放入Redis缓存（{}）", v);
            return jedis.setex(key.getBytes(), TIME_OUT_SEC, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    public <T> T get(String k, boolean needSalt, Class<T> cls) {
        Jedis jedis = jedisPool.getResource();
        try {
            String key = !needSalt ? k : PREFIX + k + SUFFIX;
            byte[] bytes = jedis.get(key.getBytes());
            if (bytes != null) {
                T t = SerializeUtil.deserialize(bytes, cls);
                log.debug("从Redis缓存获取数据({})", t);
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    public void remove(String k) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(k);
    }
}
