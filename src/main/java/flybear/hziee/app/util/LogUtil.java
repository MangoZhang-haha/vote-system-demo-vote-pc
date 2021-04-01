package flybear.hziee.app.util;

import com.alibaba.fastjson.JSON;
import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.LogInfo;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.enums.LogModuleEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 日志记录常用类
 *
 * @author qiancj
 * @since 2020/1/8 14:52
 */
@Slf4j
public class LogUtil {

    /**
     * 打印日志记录
     *
     * @param ip 请求IP
     * @param user 用户信息
     * @param method 请求方法
     * @param module 模块
     * @param description 方法描述
     */
    public static void log(String ip, User user, String method, LogModuleEnum module, String description) {
        log.info("=========================== 进入控制器 ===========================");
        log.info("模    块：{}", module.getName());
        log.info("请 求 IP：{}", ip);
        log.info("请求用户：{}", user.getUsername());
        log.info("请求方法：{}", method);
        log.info("方法描述：{}", description);
        log.info("==================================================================");
    }

    /**
     *
     * 打印错误日志记录
     *
     * @param ip 请求IP
     * @param user 用户信息
     * @param method 请求方法
     * @param module 模块
     * @param description 方法描述
     * @param params 调用参数
     * @param e Throwable
     */
    public static void logErr(String ip, User user, String method, LogModuleEnum module, String description, String params, Throwable e) {
        log.info("=========================== 操作异常 ===========================");
        log.info("模    块：{}", module.getName());
        log.info("请 求 IP：{}", ip);
        log.info("请求用户：{}", user.getUsername());
        log.info("请求方法：{}", method);
        log.info("方法描述：{}", description);
        log.info("调用参数：{}", params);
        log.info("异常代码：{}", e.getClass().getName());
        log.info("异常信息：{}", e.getMessage());
        log.info("=================================================================");
    }

    /**
     * 获取 HttpServletRequest
     * @return HttpServletRequest
     */
    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }

    /**
     * 获取IP地址
     *
     * @return IP 地址
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取注解信息
     *
     * @param joinPoint JoinPoint
     * @return 注解信息
     * @throws Exception Exception
     */
    public static LogInfo getLogInfo(JoinPoint joinPoint) throws Exception {
        Map<String, Object> map = new HashMap<>(2);
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        LogInfo logInfo = new LogInfo();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] cls = method.getParameterTypes();
                if (cls.length == arguments.length) {
                    if (method.isAnnotationPresent(Log.class)) {
                        Log log = method.getAnnotation(Log.class);
                        logInfo.setDescription(log.value());
                        logInfo.setNeedRecord(log.needRecord());
                        logInfo.setModule(log.module());
                        if (targetClass.isAnnotationPresent(Log.class)) {
                            Annotation pLog = targetClass.getAnnotation(Log.class);
                            // 方法注解如果没有模块信息，则查询类注解的模块信息
                            if (pLog != null && logInfo.getModule().getCode() == 0) {
                                logInfo.setModule(((Log) pLog).module());
                            }
                        }
                        break;
                    }
                }
            }
        }
        return logInfo;
    }

    /**
     * 获取参数 JSON 字符串
     *
     * @param joinPoint JoinPoint
     * @return 参数 JSON 字符串
     */
    public static String getParams(JoinPoint joinPoint) {
        StringBuilder params = new StringBuilder();
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                params.append(JSON.toJSON(joinPoint.getArgs()[i])).append(";");
            }
        }
        return params.toString();
    }
}
