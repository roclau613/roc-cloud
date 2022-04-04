package com.roc.cloud.security.auth.util;

import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;

/**
 * JWT-token管理 <br>
 *
 * @date: 2020/10/29 <br>
 * @author: Roc <br>
 * @since: 1.0 <br>
 * @version: 1.0 <br>
 */
public class UrlMatch {

    private UrlMatch() {
    }

    /**
     * url匹配
     *
     * @param patternPath : 模糊匹配表达式
     * @param requestPath : 待匹配的url
     * @return boolean
     * @author Roc
     * @date 2020/10/29
     **/
    public static boolean match(String patternPath, String requestPath) {
        if (StringUtils.isEmpty(patternPath) || StringUtils.isEmpty(requestPath)) {
            return false;
        }
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        return matcher.match(patternPath, requestPath);
    }

    /**
     * 匹配url
     *
     * @param patternPathList :
     * @param requestPath :
     * @return boolean
     * @author Roc
     * @date 2020/10/29
     **/
    public static boolean matchs(List<String> patternPathList, String requestPath) {
        if (CollectionUtils.isNotEmpty(patternPathList)) {
            for (String s : patternPathList) {
                if (match(s, requestPath)){
                    return true;
                }
            }
        }
        return false;
    }

}