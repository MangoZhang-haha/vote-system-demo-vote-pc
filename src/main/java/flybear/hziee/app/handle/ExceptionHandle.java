package flybear.hziee.app.handle;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.enums.ResultEnum;
import flybear.hziee.app.exception.*;
import flybear.hziee.app.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常捕获
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    /**
     * 异常捕获入口方法
     *
     * @param e Exception
     * @return Result
     */
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        // 获取最开始的异常
        while (e.getCause() != null) {
            e = (Exception) e.getCause();
        }

        // 数据验证异常捕获
        if (e instanceof ConstraintViolationException
                || e instanceof ValidException
                || e instanceof BindException) {
            log.info("【数据验证异常】", e);
            return ValidationExceptionHandle.handle(e);
        }
        // 约束异常捕获
        if (e instanceof ConstraintException
                || e instanceof DataIntegrityViolationException
                || e instanceof SQLIntegrityConstraintViolationException) {
            log.info("【约束异常】", e);
            return ConstraintExceptionHandle.handle(e);
        }

        if (e instanceof CustomException) {
            log.info("【自定义异常】", e);
            return ResultUtil.error(((CustomException) e).getResultEnum());
        } else if (e instanceof DataNotFoundException) {
            log.info("【无法查找到对应数据】", e);
            return ResultUtil.error(ResultEnum.DATA_NOT_FOUNT_ERROR);
        } else if (e instanceof NoAccessException) {
            log.info("【权限不足】", e);
            return ResultUtil.error(ResultEnum.NO_ACCESS);
        } else {
            log.error("【未知错误】", e);
        }
        return ResultUtil.error(e.getMessage());
    }

    /**
     * Shiro异常捕获
     *
     * @param e Exception
     * @return Result
     */
    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public Result handleUnauthenticatedException(Exception e) {
        if (e instanceof UnauthenticatedException) {
            return ResultUtil.error(ResultEnum.UN_AUTH);
        }
        if (e instanceof UnauthorizedException) {
            return ResultUtil.error(ResultEnum.NO_ACCESS);
        }
        return ResultUtil.error(e.getMessage());
    }

    /**
     * 文件上传错误
     *
     * @param e MultipartException
     * @return Result
     */
    @ExceptionHandler(MultipartException.class)
    public Result handleMultipartException(MultipartException e) {
        log.warn("文件上传失败", e);
        return ResultUtil.error(e.getMessage());
    }

}
