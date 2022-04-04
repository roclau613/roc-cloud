package com.roc.cloud.aliyunlog.enhancer;

import com.roc.cloud.aliyunlog.config.LogAppenderConfig;

/**
 * @description: Abstract log enhancer
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */

public abstract class AbstractLogEnhancer implements LogEnhancer {

    /**
     * 抽象enhance
     * @param config :
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void enhance(LogAppenderConfig config) {

        try {
            if(!hasBeanEnhanced()) {
                doEnhance(config);
            }
        } catch (Exception e) {
            handlerEnhanceError(e);
        } finally {
            afterEnhance();
        }
    }

    protected abstract boolean hasBeanEnhanced();

    protected abstract void doEnhance(LogAppenderConfig config);

    protected abstract void afterEnhance();

    protected void handlerEnhanceError(Exception exception) {

        cleanUp();
        exception.printStackTrace(System.err);
        throw new IllegalStateException(exception);
    }
}
