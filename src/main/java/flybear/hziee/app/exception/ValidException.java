package flybear.hziee.app.exception;

/**
 * 数据校验异常类
 *
 * @author flybear
 * @since 2019/12/20 23:16
 */
public class ValidException extends RuntimeException {
    public ValidException(String msg) {
        super(msg);
    }
}
