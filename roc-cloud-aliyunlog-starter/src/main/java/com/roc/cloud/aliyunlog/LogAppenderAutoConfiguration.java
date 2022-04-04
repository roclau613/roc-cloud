package com.roc.cloud.aliyunlog;import com.roc.cloud.aliyunlog.config.CommonLogAppenderConfigLoader;import com.roc.cloud.aliyunlog.config.LogAppenderConfig;import com.roc.cloud.aliyunlog.config.LogAppenderConfigLoader;import com.roc.cloud.aliyunlog.enhancer.LogEnhancer;import org.slf4j.Logger;import org.slf4j.LoggerFactory;import org.springframework.context.EnvironmentAware;import org.springframework.context.annotation.Configuration;import org.springframework.core.env.ConfigurableEnvironment;import org.springframework.core.env.Environment;/** * @description:  增强配置入口 * @date: 2020/9/08 9:26 * @author: Roc * @version: 1.0 */@Configurationpublic class LogAppenderAutoConfiguration implements EnvironmentAware {    private static final Logger logger = LoggerFactory.getLogger(LogAppenderAutoConfiguration.class);    /**     * 阿里云日志启动入口     * @param environment :     *     * @return void     * @author Roc     * @date 2020/9/30     **/    @Override    public void setEnvironment(Environment environment) {        logger.info("aliyun log appender has been integrated into system");        ConfigurableEnvironment env = (ConfigurableEnvironment)environment;        // load config        LogAppenderConfigLoader loader = new CommonLogAppenderConfigLoader();        final LogAppenderConfig config;        try {            config = loader.load(env);        } catch (Exception e) {            throw new IllegalStateException("Failed to load log appender config", e);        }        if(!config.isEnable()) {            return;        }        // init logEnhancer        ClassLoader classLoader = env.getClass().getClassLoader();        LogEnhancerBinder.bindClassLoader(classLoader);        LogEnhancer logEnhancer = LogEnhancerBinder.getInstance();        // do nothing if aliyun appender has been bound        if(!logEnhancer.alreadyBound()) {            logEnhancer.enhance(config);            logger.info("aliyun log appender has been successfully initialized");        }    }}