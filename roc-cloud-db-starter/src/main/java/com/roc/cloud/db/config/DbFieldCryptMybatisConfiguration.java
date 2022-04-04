package com.roc.cloud.db.config;


import com.roc.cloud.db.crypt.CryptInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: DbFieldCryptMybatisConfiguration
 * @Description: 数据库字段脱敏处理配置类
 * @author: Roc
 * @date: 2020/9/22 20:55
 **/
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "roc.cloud.db.crypt",name = "enabled", havingValue = "true", matchIfMissing = true)
public class DbFieldCryptMybatisConfiguration {


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "roc.cloud.db.crypt", name = "enabled", havingValue = "true", matchIfMissing = true)
    public CryptInterceptor cryptInterceptor() {
        CryptInterceptor cryptInterceptor = new CryptInterceptor();
        return cryptInterceptor;
    }
}
