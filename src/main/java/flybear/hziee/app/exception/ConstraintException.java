package flybear.hziee.app.exception;

import flybear.hziee.app.enums.ConstraintEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 约束异常类
 *
 * @author flybear
 * @since 2020/1/1 20:46
 */
@Getter
@AllArgsConstructor
public class ConstraintException extends RuntimeException {
    private ConstraintEnum constraintEnum;
}
