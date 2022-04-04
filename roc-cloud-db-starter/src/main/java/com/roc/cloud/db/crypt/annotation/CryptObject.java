package com.roc.cloud.db.crypt.annotation;

import com.roc.cloud.db.common.CryptTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @description: 加密对象注解 <br>
 * @date: 2020/9/22 15:56 <br>
 * @author: Roc <br>
 * @version: 1.0 <br>
 */

@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CryptObject {
    CryptTypeEnum value() default CryptTypeEnum.AES;
}

