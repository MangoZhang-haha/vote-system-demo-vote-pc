package flybear.hziee.app.handle;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 数据验证异常捕获
 *
 * @author flybear
 * @since 2020/1/1 19:43
 */
@Slf4j
public class ValidationExceptionHandle {

    /**
     * 数据验证异常捕获
     *
     * @param e Exception
     * @return Result
     */
    static Result handle(Exception e) {
        if (e instanceof ConstraintViolationException) {
            // 服务实现类中 字段验证 异常捕获
            return ResultUtil.error(getViolationsMessage(((ConstraintViolationException) e).getConstraintViolations()));
        }
        if (e instanceof BindException) {
            // 控制器中参数 @Valid 异常捕获
            return ResultUtil.error(((BindException) e).getBindingResult());
        }
        return ResultUtil.error(e.getMessage());
    }

    /**
     * 获取验证错误内容
     *
     * @param violations ConstraintViolation
     * @return 验证错误内容
     */
    private static String getViolationsMessage(Set<ConstraintViolation<?>> violations) {
        StringBuffer stringBuffer = new StringBuffer();
        violations.forEach(constraintViolation -> {
            stringBuffer.append(constraintViolation.getMessage()).append(",");
        });
        return stringBuffer.substring(0, stringBuffer.length() - 1);
    }

}
