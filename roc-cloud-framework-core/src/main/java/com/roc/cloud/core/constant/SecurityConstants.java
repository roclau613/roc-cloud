package com.roc.cloud.core.constant;

/**
 * <br>
 *
 * @date: 2020/10/27 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class SecurityConstants {

    public static final String X3_AUTH_HEADER = "x3-authentication";

    public static final String X3_LOGIN_HEADER = "x3-login";

    /**
     * 用户信息分隔符
     */
    public static final String USER_SPLIT = ":";

    /**
     * 用户信息头
     */
    public static final String USER_HEADER = "x-user-header";

    /**
     * 用户id信息头
     */
    public static final String USER_ID_HEADER = "x-userid-header";

    /**
     * 角色信息头
     */
    public static final String ROLE_HEADER = "x-role-header";

    /**
     * 租户信息头(应用)
     */
    public static final String TENANT_HEADER = "x-tenant-header";

    //    /**
    //     * 基础角色
    //     */
    //    public static final  String BASE_ROLE = "ROLE_USER";
    //
    //    /**
    //     * 授权码模式
    //     */
    //    public static final String AUTHORIZATION_CODE = "authorization_code";
    //
    //    /**
    //     * 密码模式
    //     */
    //    public static final String PASSWORD = "password";
    //
    //    /**
    //     * 刷新token
    //     */
    //    public static final  String REFRESH_TOKEN = "refresh_token";
    //
    //    /**
    //     * oauth token
    //     */
    //    public static final  String OAUTH_TOKEN_URL = "/oauth/token";
    //
    //    /**
    //     * 默认的处理验证码的url前缀
    //     */
    //    public static final  String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/validata/code";
    //
    //    /**
    //     * 手机号的处理验证码的url前缀
    //     */
    //    public static final String MOBILE_VALIDATE_CODE_URL_PREFIX = "/validata/smsCode";
    //
    //    /**
    //     * 默认生成图形验证码宽度
    //     */
    //    public static final String DEFAULT_IMAGE_WIDTH = "100";
    //
    //    /**
    //     * 默认生成图像验证码高度
    //     */
    //    public static final  String DEFAULT_IMAGE_HEIGHT = "35";
    //
    //    /**
    //     * 默认生成图形验证码长度
    //     */
    //    public static final String DEFAULT_IMAGE_LENGTH = "4";
    //
    //    /**
    //     * 默认生成图形验证码过期时间
    //     */
    //    public static final int DEFAULT_IMAGE_EXPIRE = 60;
    //
    //    /**
    //     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
    //     */
    //    public static final String DEFAULT_COLOR_FONT = "blue";
    //
    //    /**
    //     * 图片边框
    //     */
    //    public static final String DEFAULT_IMAGE_BORDER = "no";
    //
    //    /**
    //     * 默认图片间隔
    //     */
    //    public static final  String DEFAULT_CHAR_SPACE = "5";
    //
    //    /**
    //     * 默认保存code的前缀
    //     */
    //    public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";
    //
    //    /**
    //     * 验证码文字大小
    //     */
    //    public static final String DEFAULT_IMAGE_FONT_SIZE = "30";
    //    /**
    //     * zlt公共前缀
    //     */
    //    public static final String ZLT_PREFIX = "zlt:";
    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String CACHE_CLIENT_KEY = "oauth_client_details";
    //    /**
    //     * OAUTH模式登录处理地址
    //     */
    //    public static final String OAUTH_LOGIN_PRO_URL = "/user/login";
    //    /**
    //     * 获取授权码地址
    //     */
    //    public static final String AUTH_CODE_URL = "/oauth/authorize";
    /**
     * 登录页面
     */
    public static final String LOGIN_PAGE = "/login.html";
    //    /**
    //     * 登录失败页面
    //     */
    //    public static final String LOGIN_FAILURE_PAGE = LOGIN_PAGE + "?error";
    //    /**
    //     * 登出URL
    //     */
    //    public static final  String LOGOUT_URL = "/oauth/remove/token";
    //    /**
    //     * 默认token过期时间(1小时)
    //     */
    //    public static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60;
    //    /**
    //     * redis中授权token对应的key
    //     */
    //    public static final String REDIS_TOKEN_AUTH = "auth:";
    //    /**
    //     * redis中应用对应的token集合的key
    //     */
    //    public static final String REDIS_CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    //    /**
    //     * redis中用户名对应的token集合的key
    //     */
    //    public static final String REDIS_UNAME_TO_ACCESS = "uname_to_access:";
    //    /**
    //     * rsa公钥
    //     */
    //    public static final String RSA_PUBLIC_KEY = "pubkey.txt";
}
