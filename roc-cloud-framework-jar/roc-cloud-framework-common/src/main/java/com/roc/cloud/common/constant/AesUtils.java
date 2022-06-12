package com.roc.cloud.common.constant;

import com.alibaba.fastjson.JSONObject;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import io.netty.handler.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AESUtils <br>
 *
 * @date: 2022/3/30 <br>
 * @author: yanghao <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class AesUtils {

    private static final Logger log = LoggerFactory.getLogger(AesUtils.class);
    private static Cipher cipher;
    private static IvParameterSpec iv;

    static {
        try {
            byte[] vi = Hex.decodeHex("12345678123456781234567812345678".toCharArray());
            iv = new IvParameterSpec(vi);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException var1) {
            log.error("NoSuchAlgorithmException");
        } catch (NoSuchPaddingException var2) {
            log.error("NoSuchPaddingException");
        } catch (DecoderException var3) {
            log.error("DecoderException");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AesUtils() {
    }

    public static String encryptAES(String key, String plaintext) {
        String ciphertext = "";

        try {
            byte[] keyByte = parseHexStr2Byte(key);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(1, skeySpec, iv);
            byte[] pbyte = plaintext.getBytes("utf-8");
            byte[] result = cipher.doFinal(pbyte);
            ciphertext = parseByte2HexStr(result);
        } catch (Exception var7) {
            log.error("Aes encrypt error", var7);
        }

        return ciphertext;
    }

    public static String decryptAES(String key, String ciphertext) {
        String plaintext = "";

        try {
            byte[] keyByte = parseHexStr2Byte(key);
            byte[] cbyte = parseHexStr2Byte(ciphertext);
            SecretKeySpec skeySpec = new SecretKeySpec(keyByte, "AES");
            cipher.init(2, skeySpec, iv);
            byte[] pbyte = cipher.doFinal(cbyte);
            plaintext = new String(pbyte, "utf-8");
        } catch (Exception var7) {
            log.error("AES decrypt error!", var7);
        }

        return plaintext;
    }

    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }

            return result;
        }
    }

    private static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }

            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String s = encryptAES("552A9A6820A352B739ED1D670BF0E432", "-----------------------------------我是文件名");
            System.out.println(s);
        }


        // String key = createAESKey(AESType.AES_128);
        // System.out.println(key);
        // String ciphertext = encryptAES(key, "Basisuser123");
        // System.out.println(ciphertext);
        // System.out.println(decryptAES(key, ciphertext));
    }

}
