package flybear.hziee.app.handle;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.enums.ConstraintEnum;
import flybear.hziee.app.exception.ConstraintException;
import flybear.hziee.app.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 约束异常捕获
 *
 * @author flybear
 * @since 2020/1/1 19:37
 */
@Slf4j
@RestControllerAdvice
public class ConstraintExceptionHandle {

    /**
     * 约束键名正则匹配表达式
     */
    private static final Pattern CONSTRAINT_KEY_PATTERN = Pattern.compile("key '(.+)'$");

    /**
     * 约束异常捕获
     *
     * @param e Exception
     * @return Result
     */
     static Result handle(Exception e) {
        if (e instanceof ConstraintException) {
            return ResultUtil.error(((ConstraintException) e).getConstraintEnum());
        }
        if (e instanceof SQLIntegrityConstraintViolationException) {
            SQLIntegrityConstraintViolationException violationException = (SQLIntegrityConstraintViolationException) e;
            Matcher matcher = CONSTRAINT_KEY_PATTERN.matcher(violationException.getMessage());
            if (matcher.find()) {
                String constraint = matcher.group(1);
                try {
                    return ResultUtil.error(ConstraintEnum.valueOf(constraint));
                } catch (Exception ignore) {
                    return ResultUtil.error(constraint);
                }
            }
        }
        return ResultUtil.error(e.getMessage());
    }

}
