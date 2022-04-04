package com.roc.cloud.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 国际化转换工具类 MessageUtils
 * @date: 2020/10/13
 * @author: xdli
 * @since: 1.0
 * @version: 1.0
 */
@Slf4j
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }


    /**
     * 根据key获取国际化转换值
     * @param msgKey :国际化key
     *
     * @return java.lang.String
     * @author xdli
     * @date 2020/10/13
     **/
    public static String get(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            log.error("国际化key转换失败",e);
            return msgKey;
        }
    }

}
