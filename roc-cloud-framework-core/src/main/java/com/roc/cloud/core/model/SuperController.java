package com.roc.cloud.core.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.cloud.common.constant.SecurityConstants;
import com.roc.cloud.common.utils.HttpServletUtils;

import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 实体父类
 *
 * @author: Roc
 */
@Slf4j
@Data
public class SuperController {

    /**
     * loginUserDto 的字段名
     **/
    private static final String LOGIN_ID = "id";

    /**
     * loginUserDto 的字段名
     **/
    private static final String USER_ID = "userId";

    /**
     * loginUserDto 的字段名
     **/
    private static final String USER_NAME = "userName";

    /**
     * loginUserDto 的字段名
     **/
    private static final String COMPANY_ID = "companyId";

    /**
     * userDto 子类标识
     **/
    private static final String TYPE = "type";

    /**
    * loginUserDto 的字段名
    **/
    private static final String WX_OPEN_ID ="wxOpenId";

    /**
    * loginUserDto 的字段名
    **/
    private static final String THIRD_APP_ID ="thirdAppId";

    /**
    * 小程序sessionKey
    **/
    private static final String WX_SESSION_KEY ="wxSessionKey";

    /**
     * loginUserDto 校区列表字段名
     **/
    private static final String CAMPUS_AUTHORITIES = "campusAuthorities";

    /**
     * loginUserDto 角色列表字段名
     **/
    private static final String AUTHORITIES = "authorities";

    /**
     * 学员id
     **/
    private static final String STUDENT_ID = "studentId";

    /**
     * 当前登录用的用户角色类型
     */
    public String getLoginUserType() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getString(TYPE) : null;
    }

    /**
     * 当前登录用的id
     */
    public Long getLoginId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getLong(LOGIN_ID) : null;
    }

    /**
     * 当前登录用的userId
     */
    public Long getUserId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getLong(USER_ID) : null;
    }

    /**
     * 当前登录用的名字
     */
    public String getLoginUserName() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getString(USER_NAME) : null;
    }

    /**
     * 当前登录的微信openid
     */
    public String getWxOpenId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getString(WX_OPEN_ID) : null;
    }

    /**
     * 当前登录的微信appId
     */
    public String getThirdAppId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getString(THIRD_APP_ID) : null;
    }

    /**
     * 当前登录的微信appId
     */
    public String getWxSessionKey() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getString(WX_SESSION_KEY) : null;
    }

    /**
     * 当前登录用的机构的id，如果此用户没有加入或者创建机构。则此值为null
     */
    public Long getLoginCompanyId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getLong(COMPANY_ID) : null;
    }

    /**
     * 获取token中的学员id
     */
    public Long getStudentId() {
        return null != findLoginUserByToken() ? findLoginUserByToken().getLong(STUDENT_ID) : null;
    }

    /**
     * 获取登陆用户的校区列表
     */
    public List<Long> hasCampusAuthorities() {
        JSONObject loginUserByToken = findLoginUserByToken();
        if (Objects.isNull(loginUserByToken) || Objects.isNull(loginUserByToken.get(CAMPUS_AUTHORITIES))) {
            return null;
        }
        return loginUserByToken.getJSONArray(CAMPUS_AUTHORITIES).toJavaList(Long.class);
    }

    /**
     * 获取登陆用户的角色列表
     */
    public List<Long> hasAuthorities() {
        JSONObject loginUserByToken = findLoginUserByToken();
        if (Objects.isNull(loginUserByToken) || Objects.isNull(loginUserByToken.get(CAMPUS_AUTHORITIES))) {
            return null;
        }
        return loginUserByToken.getJSONArray(AUTHORITIES).toJavaList(Long.class);
    }

    /**
    * 获取登录的用户对象
    **/
    public JSONObject getLoginUser(){
        return findLoginUserByToken();
    }

    private JSONObject findLoginUserByToken() {
        String headerValue = Objects.requireNonNull(HttpServletUtils.getRequest(), "request not be null")
            .getHeader(SecurityConstants.ROC_LOGIN_HEADER);
        if (StringUtils.isNotBlank(headerValue)) {
            JSONObject loginUser = JSON.parseObject(headerValue);
            if (Objects.nonNull(loginUser)) {
                return loginUser;
            }
        }
        log.warn("not found loginUserInfo");
        return null;
    }

}
