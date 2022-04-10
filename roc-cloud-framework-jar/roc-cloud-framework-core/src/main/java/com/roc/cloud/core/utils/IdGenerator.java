package com.roc.cloud.core.utils;

/**
 * 高效分布式ID生成算法(sequence),基于Snowflake算法优化实现64位自增ID算法。
 * 其中解决时间回拨问题的优化方案如下：
 * 1. 如果发现当前时间少于上次生成id的时间(时间回拨)，着计算回拨的时间差
 * 2. 如果时间差(offset)小于等于5ms，着等待 offset * 2 的时间再生成
 * 3. 如果offset大于5，则直接抛出异常
 *
 * @author: Roc
 * @date 2019/3/5
 */
public class IdGenerator {
    private static Sequence WORKER = new Sequence();

    /**
     * getId
     *
     * @return long
     * @author Roc
     * @date 2020/9/30
     **/
    public static long getId() {
        return WORKER.nextId();
    }

    /**
     * getIdStr
     *
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    public static String getIdStr() {
        return String.valueOf(WORKER.nextId());
    }
}
