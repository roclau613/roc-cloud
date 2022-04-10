package com.roc.cloud.log.utils;

import com.alibaba.fastjson.JSONObject;
import com.roc.cloud.log.model.Audit;
import com.roc.cloud.log.model.ExceptionAudit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 日志消息 <br>
 *
 * @author yw <br>
 * @version 1.0 <br>
 * @date 2021-01-05
 * <br>
 * @since 1.0 <br>
 */
@Component
public class LogSyncUtils {

    private static String mqUrl;

    private static String logMqTopic;

    private static String logMqTag;

    private static String appId;

    private static String appKey;

    @Value("${roc.cloud.mq.url}")
    public void setMqUrl(String mqUrl) {
        LogSyncUtils.mqUrl = mqUrl;
    }

    @Value("${roc.cloud.mq.log.topic}")
    public void setLogMqTopic(String logMqTopic) {
        LogSyncUtils.logMqTopic = logMqTopic;
    }

    @Value("${roc.cloud.mq.tag}")
    public void setLogMqTag(String logMqTag) {
        LogSyncUtils.logMqTag = logMqTag;
    }

    @Value("${roc.zt.appid}")
    public void setAppId(String appId) {
        LogSyncUtils.appId = appId;
    }

    @Value("${roc.zt.appKey}")
    public void setAppKey(String appKey) {
        LogSyncUtils.appKey = appKey;
    }

    /**
     * 发送日志消息到消息中心
     *
     * @param audit :
     * @return void
     * @author yw
     * @date 2021/1/5
     */
    public static void sendMq(Audit audit) {
        String messageKey = audit.getApplicationName() + "_" + audit.getCompanyId() + "_" + audit.getUserId() + "_" + audit.getClassName() + "_" + audit.getMethodName();
        String messageContent = JSONObject.toJSONString(audit);
//        MqUtils.sendMq(mqUrl, logMqTopic, logMqTag, messageKey, messageContent,appId,appKey);
    }

    public static void sendExceptionMq(ExceptionAudit exceptionAudit) {
        String messageKey = "Exception_" + exceptionAudit.getExApplicationName() + "_" + exceptionAudit.getCompanyId() + "_" + exceptionAudit.getUserId() + "_" + exceptionAudit.getExClassName() + "_" + exceptionAudit.getExMethodName();
        String messageContent = JSONObject.toJSONString(exceptionAudit);
//        MqUtils.sendMq(mqUrl, logMqTopic, logMqTag, messageKey, messageContent,appId,appKey);
    }
}
