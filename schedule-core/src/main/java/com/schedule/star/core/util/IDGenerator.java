package com.schedule.star.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.system.SystemUtil;

import java.util.Date;
import java.util.Random;

/**
 * @author xiangnan
 * @date 2018/1/28 15:50
 */
public class IDGenerator {

    private static final Random random = new Random();
    private static String ip = "";

    static {
        String address = SystemUtil.getHostInfo().getAddress();
        if (address.length() > 15) {
            // ipv6 ?
            address = address.substring(0, 15);
        }

        ip = address;
    }

    /**
     * 生成随机字符串（最大长度为31）
     */
    public synchronized static String getId() {
        // yyMMddHHmmss + 4位随机码 + ip(最大长度15位)
        return DateUtil.format(new Date(), "yyMMddHHmmss") + getRandom() + ip;
    }

    /**
     * 生成4位随机码
     */
    private static int getRandom() {
        return random.nextInt(9000) + 1000;
    }
}
