package com.roc.cloud.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.cloud.log.annotation.AuditLog;
import com.roc.cloud.log.model.Audit;
import com.roc.cloud.log.model.ExceptionAudit;
import com.roc.cloud.log.properties.AuditLogProperties;
import com.roc.cloud.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 审计日志切面
 *
 * @author: Roc
 * @date 2020/2/3
 * <p>
 */
@Slf4j
@Aspect
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class})
public class AuditLogAspect {
    @Value("${spring.application.name}")
    private String applicationName;
    private static final String WELL = "#";

    private AuditLogProperties auditLogProperties;

    private IAuditService auditService;


    public static final String ROC_LOGIN_HEADER = "roc-login";
    /**
     * loginUserDto 的字段名
     **/
    private static final String USER_ID = "id";

    /**
     * loginUserDto 的字段名
     **/
    private static final String USER_NAME = "userName";

    /**
     * loginUserDto 的字段名
     **/
    private static final String COMPANY_ID = "companyId";

    public AuditLogAspect(AuditLogProperties auditLogProperties, IAuditService auditService) {
        this.auditLogProperties = auditLogProperties;
        this.auditService = auditService;
    }

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Before("@within(auditLog) || @annotation(auditLog)")
    public void beforeMethod(JoinPoint joinPoint, AuditLog auditLog) {
        //判断功能是否开启
        if (auditLogProperties.getEnabled()) {
            if (auditService == null) {
                log.warn("AuditLogAspect - auditService is null");
                return;
            }
            if (auditLog == null) {
                // 获取类上的注解
                auditLog = joinPoint.getTarget().getClass().getDeclaredAnnotation(AuditLog.class);
            }
            Audit audit = getAudit(auditLog, joinPoint);
            auditService.save(audit);
        }
    }

    @AfterThrowing(value = "@within(auditLog) || @annotation(auditLog)", throwing = "e")
    public void afterMethod(JoinPoint joinPoint, AuditLog auditLog, Throwable e) {
        //判断是否开启用户异常日志
        if (!auditLogProperties.getExceptionEnabled()) {
            return;
        }

        //获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        //判断 是否有用户信息
        JSONObject headObjects = findLoginUserByToken(request);
        if (Objects.isNull(headObjects)) {
            return;
        }
        //获取织入点的方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取所在的方法
        Method method = methodSignature.getMethod();
        //类名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        methodName = className + "." + methodName;
        String operation = auditLog.operation();
        if (operation.contains(WELL)) {
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            operation = getValBySpel(operation, methodSignature, args);
        }
        //请求参数
        Map<String, String> convertMap = convertMap(request.getParameterMap());
        ExceptionAudit exceptionAudit = new ExceptionAudit();
        exceptionAudit.setOperation(operation);
        exceptionAudit.setParams(JSON.toJSONString(convertMap));

        String userId = headObjects.getString(USER_ID);
        String userName = headObjects.getString(USER_NAME);
        String companyId = headObjects.getString(COMPANY_ID);
        exceptionAudit.setUserId(userId);
        exceptionAudit.setUserName(userName);
        exceptionAudit.setCompanyId(companyId);
        exceptionAudit.setExClassName(className);
        exceptionAudit.setExMethodName(methodName);
        exceptionAudit.setExApplicationName(applicationName);
        exceptionAudit.setExceptionMsg(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
//        exceptionAudit.setIp(IpUtil.getIpAddr(request));
        exceptionAudit.setIndexName("audit_log_exception");
        exceptionAudit.setTimestamp(LocalDateTime.now());
        auditService.save(exceptionAudit);
    }

    /**
     * 转换request请求
     */
    public Map<String, String> convertMap(Map<String, String[]> stringMap) {
        Map<String, String> map = new HashMap<>(16);
        for (String key : stringMap.keySet()) {
            map.put(key, stringMap.get(key)[0]);
        }
        return map;
    }

    /**
     * 转换一场信息为字符串
     *
     * @param exceptionName :
     * @param exceptionMsg  :
     * @param elements      :
     * @return java.lang.String
     * @author yw
     * @date 2021/2/2
     */
    public String stackTraceToString(String exceptionName, String exceptionMsg, StackTraceElement[] elements) {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : elements) {
            stringBuilder.append(element + "\n");
        }
        String msg = exceptionName + ":" + exceptionMsg + "\n\t" + stringBuilder.toString();
        return msg;
    }

    /**
     * 解析spEL表达式
     */
    private String getValBySpel(String spel, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spel);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }

    /**
     * 构建审计对象
     */
    private Audit getAudit(AuditLog auditLog, JoinPoint joinPoint) {
        Audit audit = new Audit();
        audit.setTimestamp(LocalDateTime.now());
        audit.setApplicationName(applicationName);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        audit.setClassName(methodSignature.getDeclaringTypeName());
        audit.setMethodName(methodSignature.getName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
//        String ipAddr = IpUtil.getIpAddr(request);
//        audit.setIp(ipAddr);
        JSONObject headObjects = findLoginUserByToken(request);
        if (Objects.isNull(headObjects)) {
            return null;
        }
        String userId = headObjects.getString(USER_ID);
        String userName = headObjects.getString(USER_NAME);
        String companyId = headObjects.getString(COMPANY_ID);
        audit.setUserId(userId);
        audit.setUserName(userName);
        audit.setCompanyId(companyId);

        String operation = auditLog.operation();
        if (operation.contains(WELL)) {
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            operation = getValBySpel(operation, methodSignature, args);
        }
        audit.setOperation(operation);
        audit.setIndexName("audit_log");

        return audit;
    }


    private JSONObject findLoginUserByToken(HttpServletRequest request) {
        String headerValue = Objects.requireNonNull(request, "request not be null").getHeader(ROC_LOGIN_HEADER);
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
