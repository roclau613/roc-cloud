package com.roc.cloud.db.crypt.imp;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import com.roc.cloud.db.crypt.Crypt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @description: 具体加解密实现类 <br>
 * @date: 2020/9/22 15:56 <br>
 * @author: Roc <br>
 * @version: 1.0 <br>
 */
@Slf4j
public class CryptImpl implements Crypt {

    private final String aesKey = "3e$R8j7n";
    private final DES des = SecureUtil.des(aesKey.getBytes());


    @Override
    public String encrypt(String value) {
        String desEncryptValue = value;
        if (StringUtils.isNotEmpty(value)) {
            desEncryptValue = des.encryptBase64(value);
        }
        return desEncryptValue;
    }

    @Override
    public String decrypt(String value) {
        String desDecryptValue = value;
        if (StringUtils.isNotEmpty(value)) {
            try {
                desDecryptValue = des.decryptStr(value);
            } catch (Exception e) {
                desDecryptValue = null;
            }
        }
        return desDecryptValue;
    }
}
