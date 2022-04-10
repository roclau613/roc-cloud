package com.roc.cloud.core.utils;

import java.nio.charset.Charset;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 鉴权签名工具类 <br>
 *
 * @date: 2021/1/5 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class AuthServerSignHelper {

    private static final Log log = LogFactory.getLog(AuthServerSignHelper.class);
    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static String CharsetConstant = "UTF-8";

    /**
     * 通过appid加密
     *
     * @param appId  : 应用ID
     * @param appKey : 应用密钥key
     * @return java.lang.String
     * @author Roc
     * @date 2021/1/5
     **/
    public static String signByAppId(String appId, String appKey) {
        return sign(appId + appKey, appKey);
    }

    /**
     * sha256_HMAC加密
     *
     * @param body   签名消息体
     * @param secret 秘钥
     * @return 加密后字符串
     */
    public static String sign(String body, String secret) {
        String hash = "";
        try {
            Mac sha256 = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(Charset.forName(CharsetConstant)), HMAC_SHA_256);
            sha256.init(secretKey);
            byte[] bytes = sha256.doFinal(body.getBytes(CharsetConstant));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            log.error("HmacSHA256加密异常", e);
        }
        return hash;
    }

    /**
     * 生成密钥
     *
     * @return java.lang.String
     * @author Roc
     * @date 2021/1/6
     **/
    public static String generateAppKey(String appId) {
        return signByAppId("1", System.currentTimeMillis() + "").substring(32);
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}
