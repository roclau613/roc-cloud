package com.roc.cloud.aliyunlog.enhancer;

import com.roc.cloud.aliyunlog.AliyunLogConstant;
import com.roc.cloud.aliyunlog.utils.LogEnvUtils;
import com.roc.cloud.aliyunlog.appender.log4j2.Log4j2AliYunAppender;
import com.roc.cloud.aliyunlog.config.LogAppenderConfig;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.util.ClassUtils;


/**
 * @description: Default log enhancer for log4j2
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */
public class DefaultLog4j2Enhancer extends AbstractLogEnhancer {

    private final ClassLoader classLoader;

    private LoggerContext ctx;

    /**
     * DefaultLog4j2Enhancer
     *
     * @param classLoader :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public DefaultLog4j2Enhancer(ClassLoader classLoader) {

        this.classLoader = classLoader;
        ctx = getLoggerContext();
    }

    /**
     * alreadyBound
     *
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public boolean alreadyBound() {

        Class clazz = LogEnvUtils.loadAliyunLog4j2AppenderExist(classLoader);
        if (clazz == null) {
            return false;
        } else {

            Map<String, Appender> appenderMap = ctx.getConfiguration().getAppenders();
            for (Map.Entry<String, Appender> entry : appenderMap.entrySet()) {
                String name = entry.getKey();
                Appender appender = entry.getValue();
                if (appender.getClass().isAssignableFrom(clazz)) {
                    return true;
                }
            }
            return false;
        }


    }

    /**
     * cleanUp
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void cleanUp() {

        markAsUnEnhanced();
    }

    /**
     * doEnhance
     *
     * @param logAppenderConfig :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    protected void doEnhance(LogAppenderConfig logAppenderConfig) {

        final Configuration config = ctx.getConfiguration();

        // config pattern
        Appender appender = null;
        String pattern = logAppenderConfig.getPattern();
        if (!StringUtils.isBlank(pattern)) {
            PatternLayout layout = PatternLayout
                    .newBuilder()
                    .withPattern(pattern)
                    .withCharset(Charset.forName(logAppenderConfig.getCharset()))
                    .build();
            appender = new Log4j2AliYunAppender(logAppenderConfig, layout);
        } else {
            appender = new Log4j2AliYunAppender(logAppenderConfig);
        }

        appender.start();
        config.addAppender(appender);
        List<String> loggerFilter = logAppenderConfig.getLoggerFilter();
        // log4j2 root logger name is ""
        if (loggerFilter.remove(AliyunLogConstant.DEFAULT_ROOT_LOGGER_NAME)) {
            loggerFilter.add("");
        }

        final Map<String, LoggerConfig> loggerMap = config.getLoggers();
        Appender finalAppender = appender;
        loggerMap.forEach((loggerName, loggerConfig) -> {
            if (!loggerFilter.contains(loggerName)) {
                loggerConfig.addAppender(finalAppender, null, null);
            }
        });

        ctx.updateLoggers();
    }

    /**
     * afterEnhance
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    protected void afterEnhance() {
        markAsEnhanced();
    }

    /**
     * hasBeanEnhanced
     *
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    protected boolean hasBeanEnhanced() {

        if (ctx.getConfiguration().getProperties()
                .get(DefaultLog4j2Enhancer.class.getCanonicalName()) != null) {

            return true;
        }

        return false;
    }

    /**
     * markAsEnhanced
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    private void markAsEnhanced() {

        ctx.getConfiguration().getProperties()
                .put(DefaultLog4j2Enhancer.class.getCanonicalName(), "");
    }

    /**
     * markAsUnEnhanced
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    private void markAsUnEnhanced() {

        ctx.getConfiguration().getProperties()
                .remove(DefaultLog4j2Enhancer.class.getCanonicalName());
    }

    /**
     * 获取上下文
     *
     * @return org.apache.logging.log4j.core.LoggerContext
     * @author Roc
     * @date 2020/9/30
     **/
    private LoggerContext getLoggerContext() {

        try {
            ClassUtils.forName(LogManager.class.getName(), classLoader);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find LogManager, but Log4j2 is on the classpath");
        }

        return (LoggerContext) LogManager.getContext(false);
    }

}
