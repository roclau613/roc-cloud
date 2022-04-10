package com.roc.cloud.core.exception;

/**
 * 分布式锁异常
 *
 * @author: Roc
 */
public class LockException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    /**
     * LockException
     *
     * @param message :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public LockException(String message) {
        super(message);
    }
}
