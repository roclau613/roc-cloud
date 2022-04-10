package com.roc.cloud.db.crypt;

import com.roc.cloud.db.common.CryptTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 脱敏方式上下文 <br>
 * @date: 2020/9/22 15:56 <br>
 * @author: Roc <br>
 * @version: 1.0 <br>
 */
public class CryptContext {

    private static Map<CryptTypeEnum, Crypt> Crypts = new HashMap<>(CryptTypeEnum.values().length);

    /**
     * 获取加密方式
     *
     * @param cryptTypeEnum 加密方式枚举
     * @return 机密方式实现类
     */
    public static Crypt getCrypt(CryptTypeEnum cryptTypeEnum) {
        Crypt crypt = Crypts.get(cryptTypeEnum);

        return crypt;
    }

    /**
     * 设置加密方式
     *
     * @param cryptTypeEnum 加密类型
     * @param crypt         加载方式
     */
    public static void setCrypt(CryptTypeEnum cryptTypeEnum, Crypt crypt) {
        Crypts.put(cryptTypeEnum, crypt);
    }

}