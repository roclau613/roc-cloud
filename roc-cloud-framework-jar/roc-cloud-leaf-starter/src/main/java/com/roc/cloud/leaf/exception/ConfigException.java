package com.roc.cloud.leaf.exception;

/**
 * @className: ConfigException
 * @description: 配置异常
 * @Author: Roc
 * @date: 2020/9/22
 **/
public class ConfigException extends RuntimeException {

    private static final long serialVersionUID = -8649610593203701970L;

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }
}
