package com.roc.cloud.security.auth.exception;

/**
 * JWT过期 <br>
 *
 * @date: 2020/10/16 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException(String message) {
        super(message);
    }
}
