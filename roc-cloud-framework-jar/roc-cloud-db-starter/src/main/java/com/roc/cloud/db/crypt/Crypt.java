package com.roc.cloud.db.crypt;

/**
 * @description: 脱敏算法接口定义 <br>
 * @date: 2020/9/22 15:56 <br>
 * @author: Roc <br>
 * @version: 1.0 <br>
 */
public interface Crypt {

    /**
     * 加密
     *
     * @param plain 原始明文
     * @return 密文
     */
    String encrypt(String plain);

    /**
     * 解密
     *
     * @param cipher 密文
     * @return 原始明文
     */
    String decrypt(String cipher);

}

