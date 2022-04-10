package com.roc.cloud.aliyunlog.enhancer;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * @description: 日志增强
 * @date: 2020/9/08 9:26
 * @author: Roc
 * @version: 1.0
 */
public class PatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    private PatternLayout patternLayout;

    /**
     * 启动类
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void start() {
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }

    /**
     * setPatternLayout
     *
     * @param layout :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public void setPatternLayout(PatternLayout layout) {

        this.patternLayout = layout;
    }
}
