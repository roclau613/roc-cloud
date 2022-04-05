/**
 *
 */
package com.roc.cloud.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *方法返回不需要装配标注response格式 <br>
 *
 * @date: 2020/9/28 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAssemblyBody {

    /**
     * 是否需要匹配Result
     *
     * @return boolean
     * @author Roc
     * @date 2020/9/30
     **/
    boolean value() default true;
}
