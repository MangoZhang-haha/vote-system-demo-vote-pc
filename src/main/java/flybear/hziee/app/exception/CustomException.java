package flybear.hziee.app.exception;

import flybear.hziee.app.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义异常
 *
 * @author qiancj
 * @since 2020/1/8 17:02
 */
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private ResultEnum resultEnum;
}
