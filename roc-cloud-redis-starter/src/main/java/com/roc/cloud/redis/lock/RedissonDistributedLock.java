package com.roc.cloud.redis.lock;

import java.util.concurrent.TimeUnit;

import com.roc.cloud.core.constant.CommonCoreConstant;
import com.roc.cloud.core.exception.PlatformApiException;
import com.roc.cloud.core.lock.DistributedLock;
import com.roc.cloud.core.lock.ZLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * @description: redisson分布式锁实现，基本锁功能的抽象实现
 * 本接口能满足绝大部分的需求，高级的锁功能，请自行扩展或直接使用原生api
 * https://gitbook.cn/gitchat/activity/5f02746f34b17609e14c7d5a
 * @author: Roc
 * @date 2020/5/5
 * @version: 1.0
 * <p>
 */

@ConditionalOnClass(RedissonClient.class)
public class RedissonDistributedLock implements DistributedLock {
    @Autowired
    private RedissonClient redisson;

    /**
     * 获取锁
     *
     * @param key    :
     * @param isFair :
     * @return
     * @author Roc
     * @date 2020/11/19
     **/

    private ZLock getLock(String key, boolean isFair) {
        RLock lock;
        if (isFair) {
            lock = redisson.getFairLock(CommonCoreConstant.LOCK_KEY_PREFIX + key);
        } else {
            lock = redisson.getLock(CommonCoreConstant.LOCK_KEY_PREFIX + key);
        }
        return new ZLock(lock, this);
    }

    /**
     * 获取锁
     *
     * @param key       :
     * @param leaseTime :
     * @param unit      :
     * @param isFair    :
     * @return
     * @author Roc
     * @date 2020/11/19
     **/

    @Override
    public ZLock lock(String key, long leaseTime, TimeUnit unit, boolean isFair) {
        ZLock zLock = getLock(key, isFair);
        RLock lock = (RLock) zLock.getLock();
        lock.lock(leaseTime, unit);
        return zLock;
    }

    /**
     * 尝试获取锁
     *
     * @param key       :
     * @param waitTime  :
     * @param leaseTime :
     * @param unit      :
     * @param isFair    :
     * @return
     * @author Roc
     * @date 2020/11/19
     **/

    @Override
    public ZLock tryLock(String key, long waitTime, long leaseTime, TimeUnit unit, boolean isFair) throws InterruptedException {
        ZLock zLock = getLock(key, isFair);
        RLock lock = (RLock) zLock.getLock();
        if (lock.tryLock(waitTime, leaseTime, unit)) {
            return zLock;
        }
        return null;
    }

    /**
     * 释放锁
     *
     * @param lock :
     * @return void
     * @author Roc
     * @date 2020/11/19
     **/

    @Override
    public void unlock(Object lock) {
        if (lock != null) {
            if (lock instanceof RLock) {
                RLock rLock = (RLock) lock;
                if (rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            } else {
                throw new PlatformApiException("requires RLock type");
            }
        }
    }
}
