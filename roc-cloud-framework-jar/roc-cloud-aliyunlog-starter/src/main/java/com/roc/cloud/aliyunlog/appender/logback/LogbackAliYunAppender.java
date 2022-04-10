package com.roc.cloud.aliyunlog.appender.logback;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.aliyun.openservices.aliyun.log.producer.LogProducer;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfigs;
import com.aliyun.openservices.aliyun.log.producer.errors.LogSizeTooLargeException;
import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import com.aliyun.openservices.aliyun.log.producer.errors.TimeoutException;
import com.aliyun.openservices.log.common.LogItem;
import com.roc.cloud.aliyunlog.AliyunLogConstant;
import com.roc.cloud.aliyunlog.config.LogAppenderConfig;
import com.roc.cloud.aliyunlog.appender.AliYunAppenderCallback;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * @description: LogbackAliYunAppender
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */
public class LogbackAliYunAppender<E> extends UnsynchronizedAppenderBase<E> {

    private Encoder<E> encoder;

    protected LogAppenderConfig config;

    private LogProducer producer;

    private DateTimeFormatter formatter;

    private static final String LOG_ERROR = "ERROR";

    /**
     * 适配Logback appender
     *
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public LogbackAliYunAppender() {
    }

    /**
     * 适配Logback appender
     *
     * @param logAppenderConfig :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    public LogbackAliYunAppender(LogAppenderConfig logAppenderConfig) {

        this.config = logAppenderConfig;
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
        try {
            doStart();
        } catch (Exception e) {
            addError("Failed to start LogbackAliYunAppender.", e);
        }
    }

    /**
     * 执行业务启动类
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    private void doStart() {

        formatter = DateTimeFormat.forPattern(config.getTimeFormat()).withZone(DateTimeZone.forID(config.getTimeZone()));
        if (config.getProducerConfig() == null) {
            ProjectConfigs projectConfigs = new ProjectConfigs();
            projectConfigs.put(config.getProjectConfig().buildProjectConfig());
            ProducerConfig producerConfig = new ProducerConfig(projectConfigs);
            config.setProducerConfig(producerConfig);
        }
        producer = new LogProducer(config.getProducerConfig());

        super.start();
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
        try {
            doStop();
        } catch (Exception e) {
            addError("Failed to stop LogbackAliYunAppender.", e);
        }
    }

    /**
     * 停止方法
     *
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    private void doStop() throws InterruptedException, ProducerException {
        if (!isStarted()) {
            return;
        }
        super.stop();
        if (producer != null) {
            producer.close();
        }
    }

    /**
     * 本地服务日志输出到阿里云日志
     *
     * @param eventObject :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public void append(E eventObject) {

        //init Event Object
        if (!(eventObject instanceof LoggingEvent)) {
            return;
        }
        LoggingEvent event = (LoggingEvent) eventObject;

        List<LogItem> logItems = new ArrayList<LogItem>();
        LogItem item = new LogItem();

        logItems.add(item);
        item.SetTime((int) (event.getTimeStamp() / 1000));
        String level = event.getLevel().toString();
        DateTime dateTime = new DateTime(event.getTimeStamp());
        item.PushBack("time", dateTime.toString(formatter));
        item.PushBack("level", level);
        item.PushBack("thread", event.getThreadName());


        item.PushBack("status", event.getLevel().toString());


        StackTraceElement[] caller = event.getCallerData();
        if (caller != null && caller.length > 0) {
            item.PushBack("location", caller[0].toString());
        }

        String companyId = MDC.get(AliyunLogConstant.LOG_COMPANY_ID);
        if (!StringUtils.isEmpty(companyId)) {
            item.PushBack(AliyunLogConstant.LOG_COMPANY_ID, companyId);
        }
        String traceId = MDC.get(AliyunLogConstant.LOG_TRACE_ID);
        if (!StringUtils.isEmpty(traceId)) {
            item.PushBack(AliyunLogConstant.LOG_TRACE_ID, traceId);
        }

        String requestUrl = MDC.get(AliyunLogConstant.LOG_REQUEST_URL);
        if (!StringUtils.isEmpty(requestUrl)) {
            item.PushBack(AliyunLogConstant.LOG_REQUEST_URL, requestUrl);
        }

        IThrowableProxy iThrowableProxy = event.getThrowableProxy();
        if (iThrowableProxy != null) {
            String throwable = getExceptionInfo(iThrowableProxy);
            throwable += fullDump(event.getThrowableProxy().getStackTraceElementProxyArray());
            item.PushBack("throwable", throwable);
        }

        if (this.encoder != null) {
            String errorLogText = new String(this.encoder.encode(eventObject));
            if (LOG_ERROR.equals(level)) {
                if (!StringUtils.isEmpty(errorLogText)) {
                    errorLogText = errorLogText.replaceAll("\"", "");
                }
            }
            item.PushBack("log", errorLogText);
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
            addError("The current thread has been interrupted during send logs.");
        } catch (Exception e) {
            if (e instanceof LogSizeTooLargeException) {
                addError("The size of log is larger than the maximum allowable size, e={}", e);
            } else if (e instanceof TimeoutException) {
                addError("The time taken for allocating memory for the logs has surpassed., e={}", e);
            } else {
                addError("Failed to send log, e=", e);
            }
        }
    }

    /**
     * 获取异常信息
     *
     * @param iThrowableProxy :
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    private String getExceptionInfo(IThrowableProxy iThrowableProxy) {
        String s = iThrowableProxy.getClassName();
        String message = iThrowableProxy.getMessage();
        return (message != null) ? (s + ": " + message) : s;
    }

    /**
     * 异常处理
     *
     * @param stackTraceElementProxyArray :
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    private String fullDump(StackTraceElementProxy[] stackTraceElementProxyArray) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElementProxy step : stackTraceElementProxyArray) {
            builder.append(CoreConstants.LINE_SEPARATOR);
            String string = step.toString();
            builder.append(CoreConstants.TAB).append(string);
            ThrowableProxyUtil.subjoinPackagingData(builder, step);
        }
        return builder.toString();
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

    /**
     * 设置日志配置类
     *
     * @param config :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public void setConfig(LogAppenderConfig config) {
        this.config = config;
    }

    /**
     * 编码
     *
     * @param encoder :
     * @return void
     * @author Roc
     * @date 2020/9/30
     **/
    public void setEncoder(Encoder<E> encoder) {
        this.encoder = encoder;
    }
}
