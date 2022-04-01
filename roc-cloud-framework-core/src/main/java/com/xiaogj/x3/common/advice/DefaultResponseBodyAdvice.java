package com.roc.cloud.common.advice;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.roc.cloud.common.annotation.NotAssemblyBody;
import com.roc.cloud.common.helper.X3PageHelper;
import com.roc.cloud.common.model.PageResult;
import com.roc.cloud.common.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 业务侧默认组装Result对象通用处理
 *
 * @author: Roc
 */
@ResponseBody
@Slf4j
public class DefaultResponseBodyAdvice implements ResponseBodyAdvice {

    private final String SWAGGER_NAME = "swagger";
    private final String SWAGGER_API_DOCS_NAME = "api-docs";

    /**
     * description: 对方法返回参数进行封装，统一返回格式
     * version: 1.0
     * date: 2020/8/13 9:34
     * author: Roc
     *
     * @param result             业务接口返回参数
     * @param methodParameter    业务接口方法参数对象
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return java.lang.Object
     */
    @Override
    public Object beforeBodyWrite(Object result, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        NotAssemblyBody notAssemblyBody = methodParameter.getMethod().getAnnotation(NotAssemblyBody.class);
        if (notAssemblyBody != null && notAssemblyBody.value()) {
            return result;
        }
        String url = serverHttpRequest.getURI().getPath();
        if (url.indexOf(SWAGGER_NAME) != -1 || url.indexOf(SWAGGER_API_DOCS_NAME) != -1) {
            return result;
        }
        if (result == null) {
            return Result.succeed();
        }
        if (result instanceof Result) {
            return result;
        } else {
            Result response = Result.succeed();
            try {
                Boolean pageType = X3PageHelper.getLocalPageType();
                if (pageType != null && pageType && result instanceof Page) {
                    PageInfo pageInfo = new PageInfo((Page) result);
                    PageResult page = new PageResult();
                    page.setPageNum(pageInfo.getPageNum());
                    page.setTotalCount(Long.valueOf(pageInfo.getTotal()).intValue());
                    page.setTotalPage(pageInfo.getPages());
                    response.setPage(page);
                }
                response.setData(result);
            } finally {
                X3PageHelper.clearLocalPageType();
            }

            if (result instanceof String) {
                return JSON.toJSONString(response);
            }
            return response;
        }
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }
}
