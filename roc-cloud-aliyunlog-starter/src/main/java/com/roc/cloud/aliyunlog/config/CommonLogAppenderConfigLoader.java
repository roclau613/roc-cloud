package com.roc.cloud.aliyunlog.config;

import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
import com.aliyun.openservices.aliyun.log.producer.ProjectConfigs;
import com.roc.cloud.aliyunlog.AliyunLogConstant;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;

/**
 * @description: Load log appender config from environment
 * @date: 2020/8/13 9:26
 * @author: Roc
 * @version: 1.0
 */
public class CommonLogAppenderConfigLoader implements LogAppenderConfigLoader {
    /**
     * load
     *
     * @param environment :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    @Override
    public LogAppenderConfig load(ConfigurableEnvironment environment) throws InvocationTargetException, IllegalAccessException {

        LogAppenderConfig config = readConfigFromContext(environment);

        return config;
    }

    /**
     * readConfigFromContext
     *
     * @param environment :
     * @return
     * @author Roc
     * @date 2020/9/30
     **/
    private LogAppenderConfig readConfigFromContext(ConfigurableEnvironment environment) throws InvocationTargetException, IllegalAccessException {

        Set<String> configKeys = new HashSet<>();
        Iterator<PropertySource<?>> propertySourceIterator = environment.getPropertySources().iterator();
        while (propertySourceIterator.hasNext()) {
            PropertySource propertySource = propertySourceIterator.next();
            if (propertySource instanceof EnumerablePropertySource) {
                configKeys.addAll(Arrays.asList(((EnumerablePropertySource) propertySource)
                        .getPropertyNames()));
            }
        }

        Map<String, String> context = new HashMap<>(configKeys.size());
        Map<String, String> projectContext = new HashMap<>(configKeys.size());
        Map<String, String> producerContext = new HashMap<>(configKeys.size());
        for (String key : configKeys) {
            if (filterAllLogConfig(key)) {
                if (key.startsWith(AliyunLogConstant.LOG_PRODUCER_CONFIG_PREFIX)) {
                    producerContext.put(
                            parseConfigKey(AliyunLogConstant.LOG_PRODUCER_CONFIG_PREFIX, key),
                            environment.getProperty(key));
                } else if (key.startsWith(AliyunLogConstant.LOG_PROJECT_CONFIG_PREFIX)) {
                    projectContext.put(
                            parseConfigKey(AliyunLogConstant.LOG_PROJECT_CONFIG_PREFIX, key),
                            environment.getProperty(key));
                } else {
                    context.put(
                            parseConfigKey(AliyunLogConstant.LOG_CONFIG_PREFIX, key),
                            environment.getProperty(key));
                }
            }
        }

        LogAppenderConfig config = new LogAppenderConfig();
        BeanUtils.populate(config, context);
        String filterStr = context.get(AliyunLogConstant.LOG_CONFIG_LOGGER_FILTER);
        if (!StringUtils.isBlank(filterStr)) {
            List<String> loggerFilter = Arrays.asList(filterStr.split(","));
            config.getLoggerFilter().addAll(loggerFilter);
        }
        if (StringUtils.isBlank(config.getTopic())) {
            throw new IllegalStateException("Topic must not be null");
        }

        CommonProjectConfig commonProjectConfig = SpringUtil.getBean(CommonProjectConfig.class);
        BeanUtils.populate(commonProjectConfig, projectContext);

        ProjectConfigs projectConfigs = new ProjectConfigs();
        projectConfigs.put(commonProjectConfig.buildProjectConfig());
        ProducerConfig producerConfig = new ProducerConfig(projectConfigs);
        BeanUtils.populate(producerConfig, producerContext);

        config.setProducerConfig(producerConfig);
        config.setProjectConfig(commonProjectConfig);

        return config;
    }

    /**
     * parseConfigKey
     *
     * @param prefix :
     * @param key    :
     * @return java.lang.String
     * @author Roc
     * @date 2020/9/30
     **/
    private String parseConfigKey(String prefix, String key) {
        return key.replace(prefix + ".", "");
    }

    /**
     * filterAllLogConfig
     *
     * @param key :
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    private boolean filterAllLogConfig(String key) {

        return key.startsWith(AliyunLogConstant.LOG_CONFIG_PREFIX);
    }

}
