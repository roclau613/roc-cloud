package com.roc.cloud.security.auth.exception;

/**
 * JWT签名不合法 <br>
 *
 * @date: 2020/10/16 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class JwtInvalidException extends RuntimeException {

    public JwtInvalidException(String message) {
        super(message);
    }
}
