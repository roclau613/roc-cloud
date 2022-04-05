package com.roc.cloud.aliyunlog.config;

import java.lang.reflect.InvocationTargetException;

import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @description: Config loader
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */

public interface LogAppenderConfigLoader {

    /**
     * 从spring environment获取配置
     *
     * @param environment :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    LogAppenderConfig load(ConfigurableEnvironment environment) throws InvocationTargetException, IllegalAccessException;

}
