package flybear.hziee.app.util;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.enums.ConstraintEnum;
import flybear.hziee.app.enums.ResultEnum;
import org.springframework.validation.BindingResult;

/**
 * 响应封装常用类
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
public class ResultUtil {

    public static <T> Result<T> success(T t) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), t);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg());
    }

    public static <T> Result<T> error(Exception e, ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), e.getMessage());
    }

    public static <T> Result<T> error(ResultEnum resultEnum) {
        return new Result<>(resultEnum.getCode(), resultEnum.getMsg());
    }

    public static <T> Result<T> error(ConstraintEnum constraintEnum) {
        return new Result<>(constraintEnum.getCode(), constraintEnum.getMsg());
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(ResultEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg);
    }

    public static <T> Result<T> error(BindingResult bindingResult) {
        if (bindingResult.getFieldError() == null) {
            return error("");
        }
        return error(bindingResult.getFieldError().getDefaultMessage());
    }
}
