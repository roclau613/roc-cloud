package com.roc.cloud.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 *
 * @author: Roc
 * @date 2018/5/29 14:12
 * <p>
 */
public interface DistributedLock {
    /**
     * 获取锁，如果获取不成功则一直等待直到lock被获取
     *
     * @param key       锁的key
     * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit      {@code leaseTime} 参数的时间单位
     * @param isFair    是否公平锁
     * @return 锁对象
     * @throws Exception 未知异常
     */
    ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

    /**
     * TODO 添加方法描述
     *
     * @param key       :
     * @param leaseTime :
     * @param unit      :
     * @return com.roc.cloud.common.lock.ZLock
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default ZLock lock(String key, long leaseTime, TimeUnit unit) throws Exception {
        return this.lock(key, leaseTime, unit, false);
    }

    /**
     * TODO 添加方法描述
     *
     * @param key    :
     * @param isFair :
     * @return com.roc.cloud.common.lock.ZLock
     * @throws Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     *
     **/
    default ZLock lock(String key, boolean isFair) throws Exception {
        return this.lock(key, -1, null, isFair);
    }

    /**
     * lock
     *
     * @param key :
     * @return com.roc.cloud.common.lock.ZLock
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default ZLock lock(String key) throws Exception {
        return this.lock(key, -1, null, false);
    }

    /**
     * 尝试获取锁，如果锁不可用则等待最多waitTime时间后放弃
     *
     * @param key       锁的key
     * @param waitTime  获取锁的最大尝试时间(单位 {@code unit})
     * @param leaseTime 加锁的时间，超过这个时间后锁便自动解锁；
     *                  如果leaseTime为-1，则保持锁定直到显式解锁
     * @param unit      {@code waitTime} 和 {@code leaseTime} 参数的时间单位
     * @param isFair
     * @return 锁对象，如果获取锁失败则为null
     * @exception Exception 未知异常
     */
    ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws Exception;

    /**
     * tryLock
     *
     * @param key       :
     * @param waitTime  :
     * @param leaseTime :
     * @param unit      :
     * @return com.roc.cloud.common.lock.ZLock
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit) throws Exception {
        return this.tryLock(key, waitTime, leaseTime, unit, false);
    }

    /**
     * tryLock
     *
     * @param key      :
     * @param waitTime :
     * @param unit     :
     * @param isFair   :
     * @return com.roc.cloud.common.lock.ZLock
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default ZLock tryLock(String key, long waitTime, TimeUnit unit, boolean isFair) throws Exception {
        return this.tryLock(key, waitTime, -1, unit, isFair);
    }

    /**
     * tryLock
     *
     * @param key      :
     * @param waitTime :
     * @param unit     :
     * @return com.roc.cloud.common.lock.ZLock
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default ZLock tryLock(String key, long waitTime, TimeUnit unit) throws Exception {
        return this.tryLock(key, waitTime, -1, unit, false);
    }

    /**
     * 释放锁
     *
     * @param lock 锁对象
     * @return void
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    void unlock(Object lock) throws Exception;


    /**
     * 释放锁
     *
     * @param zLock 锁抽象对象
     * @return void
     * @exception Exception 未知异常
     * @author Roc
     * @date 2020/11/19
     **/
    default void unlock(ZLock zLock) throws Exception {
        if (zLock != null) {
            this.unlock(zLock.getLock());
        }
    }
}
