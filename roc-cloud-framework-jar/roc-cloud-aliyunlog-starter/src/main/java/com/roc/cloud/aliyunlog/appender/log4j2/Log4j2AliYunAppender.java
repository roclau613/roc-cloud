package com.roc.cloud.aliyunlog.appender.log4j2;

import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.Producer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfigs;
import com.aliyun.openservices.aliyun.log.producer.errors.LogSizeTooLargeException;
import com.aliyun.openservices.aliyun.log.producer.errors.TimeoutException;
import com.aliyun.openservices.log.common.LogItem;
import com.roc.cloud.aliyunlog.config.LogAppenderConfig;
import com.roc.cloud.aliyunlog.appender.AliYunAppenderCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Throwables;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @description: Aliyun appender for log4j2
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */
public class Log4j2AliYunAppender extends AbstractAppender {

    public static final String APPENDER_NAME = "ALI_YUN_APPENDER";

    private Producer producer;

    private final LogAppenderConfig config;

    protected DateTimeFormatter formatter;

    /**
     * 适配log4j appender
     *
     * @param config :  日志配置类
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public Log4j2AliYunAppender(LogAppenderConfig config) {

        super(APPENDER_NAME, null, null);
        this.config = config;
    }

    /**
     * 适配log4j appender
     *
     * @param config :
     * @param layout :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public Log4j2AliYunAppender(LogAppenderConfig config, PatternLayout layout) {

        super(APPENDER_NAME, null, layout);
        this.config = config;
    }

    /**
     * 执行业务启动类
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void start() {
        super.start();

        formatter = DateTimeFormat.forPattern(config.getTimeFormat()).withZone(DateTimeZone.forID(config.getTimeZone()));
        ProducerConfig producerConfig = config.getProducerConfig();
        if (producerConfig == null) {
            ProjectConfigs projectConfigs = new ProjectConfigs();
            projectConfigs.put(config.getProjectConfig().buildProjectConfig());
            producerConfig = new ProducerConfig(projectConfigs);
        }
        producer = new LogProducer(producerConfig);
    }

    /**
     * 停止方法
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void stop() {
        super.stop();
        if (producer != null) {
            try {
                producer.close();
            } catch (Exception e) {
                LOGGER.error("Failed to close aliyun log producer: ", e);
            }
        }
    }

    /**
     * 本地服务日志输出到阿里云日志
     *
     * @param event :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void append(LogEvent event) {

        List<LogItem> logItems = new ArrayList<LogItem>();
        LogItem item = new LogItem();
        logItems.add(item);
        item.SetTime((int) (event.getTimeMillis() / 1000));
        DateTime dateTime = new DateTime(event.getTimeMillis());
        item.PushBack("time", dateTime.toString(formatter));
        item.PushBack("level", event.getLevel().toString());
        item.PushBack("thread", event.getThreadName());

        StackTraceElement source = event.getSource();
        if (source == null && (!event.isIncludeLocation())) {
            event.setIncludeLocation(true);
            source = event.getSource();
            event.setIncludeLocation(false);
        }

        item.PushBack("location", source == null ? "Unknown(Unknown Source)" : source.toString());

        String message = event.getMessage().getFormattedMessage();
        item.PushBack("message", message);

        String throwable = getThrowableStr(event.getThrown());
        if (throwable != null) {
            item.PushBack("throwable", throwable);
        }

        if (getLayout() != null) {
            item.PushBack("log", new String(getLayout().toByteArray(event)));
        }

        Map<String, String> properties = event.getContextMap();
        if (properties.size() > 0) {
            Object[] keys = properties.keySet().toArray();
            Arrays.sort(keys);
            for (int i = 0; i < keys.length; i++) {
                item.PushBack(keys[i].toString(), properties.get(keys[i].toString()));
            }
        }

        try {
            producer.send(
                    config.getProjectConfig().getName(),
                    config.getProjectConfig().getLogstore(),
                    config.getTopic(),
                    null,
                    logItems,
                    new AliYunAppenderCallback(config, logItems));
        } catch (InterruptedException e) {
            LOGGER.warn("The current thread has been interrupted during send logs.");
        } catch (Exception e) {
            if (e instanceof LogSizeTooLargeException) {
                LOGGER.error("The size of log is larger than the maximum allowable size, e={}", e);
            } else if (e instanceof TimeoutException) {
                LOGGER.error("The time taken for allocating memory for the logs has surpassed., e={}", e);
            } else {
                LOGGER.error("Failed to send log, logItems={}, e=", logItems, e);
            }
        }
    }

    /**
     * 获取异常描述
     *
     * @param throwable :
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    private String getThrowableStr(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : Throwables.toStringList(throwable)) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(System.getProperty("line.separator"));
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 获取配置主体类
     *
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public LogAppenderConfig getConfig() {
        return config;
    }


}
