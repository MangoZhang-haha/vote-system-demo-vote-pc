package flybear.hziee.app.entity;

import flybear.hziee.app.enums.ResultEnum;
import lombok.Data;

/**
 * 响应实体类
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Data
public class Result<T> {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public Result() {
    }

    public Result(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public Result(ResultEnum resultEnum, T t) {
        this.code = resultEnum.getCode();
        if (t instanceof String) {
            this.msg = t.toString();
        } else {
            this.msg = resultEnum.getMsg();
            this.data = t;
        }
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
