package com.roc.cloud.aliyunlog.utils;

/**
 * @description: 工具类
 * @date: 2020/9/08 9:26
 * @author: Roc
 * @version: 1.0
 */
public final class LogEnvUtils {

    private LogEnvUtils() {
    }

    /**
     * isLogbackUsable
     *
     * @param classloader :
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    public static boolean isLogbackUsable(ClassLoader classloader) {

        try {
            return classloader.loadClass("ch.qos.logback.classic.LoggerContext") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * isLog4j2Usable
     *
     * @param classloader :
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    public static boolean isLog4j2Usable(ClassLoader classloader) {

        try {
            return (classloader.loadClass("org.apache.logging.slf4j.Log4jLoggerFactory") != null);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * loadAliyunLog4j2AppenderExist
     *
     * @param classloader :
     * @return java.lang.Class
     * @author Roc
     * @date 2020/9/30
     **/
    public static Class loadAliyunLog4j2AppenderExist(ClassLoader classloader) {

        try {
            return classloader.loadClass("com.aliyun.openservices.log.log4j2.LoghubAppender");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * loadAliyunLogbackAppenderExist
     *
     * @param classloader :
     * @return java.lang.Class
     * @author Roc
     * @date 2020/9/30
     **/
    public static Class loadAliyunLogbackAppenderExist(ClassLoader classloader) {

        try {
            return classloader.loadClass("com.aliyun.openservices.log.logback.LoghubAppender");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
