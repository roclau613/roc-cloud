package com.roc.cloud.common.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * 国际化LocaleConfig
 * @date: 2020/10/13
 * @author: xdli
 * @since: 1.0
 * @version: 1.0
 */
@Configuration
public class LocaleConfig {


    /**
     * 默认解析器
     *
     * @return org.springframework.web.servlet.LocaleResolver
     * @author xdli
     * @date 2020/10/13
     **/
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.CHINA);
        return localeResolver;
    }

    /**
     * 默认国际化拦截器 其中lang表示切换语言的参数名
     *
     * @return org.springframework.web.servlet.config.annotation.WebMvcConfigurer
     * @author xdli
     * @date 2020/10/13
     **/
    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName("lang");
                registry.addInterceptor(localeInterceptor);
            }
        };
    }


}
