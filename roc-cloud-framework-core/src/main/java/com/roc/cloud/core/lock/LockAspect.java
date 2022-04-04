package com.roc.cloud.core.lock;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * 分布式锁切面
 *
 * @author: Roc
 * @date 2020/6/6
 * <p>
 */
@Slf4j
@Aspect
@Order(Integer.MIN_VALUE + 2)
public class LockAspect {
    @Autowired(required = false)
    private DistributedLock locker;

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private static String spelKey = "#";

    @Around("@within(lock) || @annotation(lock)")
    public Object aroundLock(ProceedingJoinPoint point, Lock lock) throws Throwable {
        ZLock lockObj = null;
        if (lock == null) {
            // 获取类上的注解
            lock = point.getTarget().getClass().getDeclaredAnnotation(Lock.class);
        }
        String lockKey = lock.key();
        if (locker == null) {
            throw new PlatformApiException("DistributedLock is null");
        }
        if (StrUtil.isEmpty(lockKey)) {
            throw new PlatformApiException("lockKey is null");
        }

        String methodName = null;
        if (lockKey.contains(spelKey)) {
            MethodSignature methodSignature = (MethodSignature) point.getSignature();
            //获取方法参数值
            Object[] args = point.getArgs();
            lockKey = getValBySpEl(lockKey, methodSignature, args);
            log.info("[lock] => key:{}, ", lockKey);
            methodName = methodSignature.getMethod().getName();
        }
        try {
            //加锁
            if (lock.waitTime() > 0) {
                lockObj = locker.tryLock(lockKey, lock.waitTime(), lock.leaseTime(), lock.unit(), lock.isFair());
            } else {
                lockObj = locker.lock(lockKey, lock.leaseTime(), lock.unit(), lock.isFair());
            }

            if (lockObj != null) {
                long startTime = System.currentTimeMillis();
                Object proceed = point.proceed();
                log.debug("[lock] => {} run times => {} ms", methodName, System.currentTimeMillis() - startTime);
                return proceed;
            } else {
                throw new PlatformApiException(lock.errorCode(), lock.msg());
            }
        } finally {
            locker.unlock(lockObj);
        }
    }

    /**
     * 解析spEL表达式
     */
    private String getValBySpEl(String spEl, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEl);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }

}
