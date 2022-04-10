package com.roc.cloudsecurity.auth.api;

import lombok.Builder;
import lombok.Data;

/**
 * 荷载 <br>
 *
 * @date: 2020/10/14 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
@Data
@Builder
public class PayloadDto {

    /**
     * 主题
     **/
    private String sub;

    /**
     * 签发时间
     **/
    private Long iat;

    /**
     * 过期时间
     **/
    private Long exp;

    /**
     * JWT的ID
     **/
    private String jti;

    /**
     * 用户名称
     **/
    private LoginUserDto loginUser;
}
