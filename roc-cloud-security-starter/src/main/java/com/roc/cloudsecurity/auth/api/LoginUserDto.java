package com.roc.cloudsecurity.auth.api;

import java.util.List;
import lombok.Data;

/**
 * 登录用户dto
 *
 * @date: 2020/10/24 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 2.0 <br>
 */
@Data
public class LoginUserDto {

    /**
     * 当前登录用的id.
     */
    private Long id;

    /**
     * 用户id
     **/
    private Long userId;

    /**
     * 当前登录用的名字
     */
    private String userName;

    /**
     * 当前登录用的机构的id，如果此用户没有加入或者创建机构。则此值为null
     */
    private Long companyId;

    /**
     * userDTO子类标识
     */
    private String type;

    /**
     * 用户拥有的权限(当前)
     **/
    private List<Long> authorities;

    /**
     * 用户拥有的校区权限(当前)
     **/
    private List<Long> campusAuthorities;

    /**
     * 微信openid
     **/
    private String wxOpenId;

    /**
     * 微信appid
     **/
    private String thirdAppId;

    /**
     * 小程序sessionKey
     **/
    private String wxSessionKey;

    /**
     * 是否超级管理员
     **/
    private Boolean superAdmin;

    /**
     * 产品业务线 ProductCodeEnum.number（1云校，2云课，3小竹通）
     **/
    private Integer productCode;

    /**
     * 学员id
     **/
    private Long studentId;
}
