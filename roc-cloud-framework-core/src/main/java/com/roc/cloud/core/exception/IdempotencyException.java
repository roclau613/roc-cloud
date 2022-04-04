package com.roc.cloud.core.exception;

/**
 * 幂等性异常
 *
 * @author: Roc
 */
public class IdempotencyException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    /**
     * 设置message值
    * @param message :  
     *        
     * @return 
     * @author Roc
     * @date 2020/9/30
     **/
    public IdempotencyException(String message) {
        super(message);
    }
}
