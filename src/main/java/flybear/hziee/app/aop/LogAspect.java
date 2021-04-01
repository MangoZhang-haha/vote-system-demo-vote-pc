package flybear.hziee.app.aop;

import flybear.hziee.app.entity.LogInfo;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 日志记录AOP
 *
 * @author qiancj
 * @since 2020/1/8 14:52
 */
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Pointcut("@annotation(flybear.hziee.app.anno.Log)")
    public void logPointCut() {
    }

    /**
     * 记录用户行为日志
     *
     * @param joinPoint JoinPoint
     */
    @Before("logPointCut()")
    public void log(JoinPoint joinPoint) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String ip = LogUtil.getIpAddress();
        try {
            LogInfo logInfo = LogUtil.getLogInfo(joinPoint);
            LogUtil.log(
                    ip, user,
                    (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"),
                    logInfo.getModule(),
                    logInfo.getDescription()
            );
            if (logInfo.isNeedRecord()) {
                // TODO 保存日志到数据库
            }
        } catch (Exception e) {
            log.error("日志记录失败：{}", e.getMessage());
        }
    }

    /**
     * 记录异常信息
     *
     * @param joinPoint JoinPoint
     * @param e         Throwable
     */
    @AfterThrowing(pointcut = "logPointCut()", throwing = "e")
    public void logErr(JoinPoint joinPoint, Throwable e) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String ip = LogUtil.getIpAddress();
        String params = LogUtil.getParams(joinPoint);
        try {
            LogInfo logInfo = LogUtil.getLogInfo(joinPoint);
            LogUtil.logErr(
                    ip, user,
                    (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"),
                    logInfo.getModule(),
                    logInfo.getDescription(),
                    params, e
            );
            if (logInfo.isNeedRecord()) {
                // TODO 保存日志到数据库
            }
        } catch (Exception ex) {
            log.error("日志记录失败：{}", e.getMessage());
        }
    }
}
