package com.roc.cloud.db.crypt.loader;

import com.roc.cloud.db.common.CryptTypeEnum;
import com.roc.cloud.db.crypt.CryptContext;
import com.roc.cloud.db.crypt.imp.CryptImpl;

/**
 * @description: 脱敏实现类加载器 <br>
 * @date: 2020/9/22 15:56 <br>
 * @author: Roc <br>
 * @version: 1.0 <br>
 */
public class CryptLoader {

    /**
     * 加载所有加密方式实现类
     */
    public void loadCrypt() {
        CryptContext.setCrypt(CryptTypeEnum.AES, new CryptImpl());
    }
}