package cn.ccsun.qq.util;

import cn.hutool.crypto.SecureUtil;

/**
 * @author HK
 * @date 2019-01-10 05:17
 */
public class Md5Util {

    /**
     * 使用第三方工具包提供的md5加密
     */
    public static String md5(String password) {
        return SecureUtil.md5(password);
    }

}
