package flybear.hziee.app.enums;

import lombok.Getter;

/**
 * 响应状态码
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Getter
public enum ResultEnum {
    // 通用
    SUCCESS(0, "成功"),
    UN_AUTH(401, "未登录"),
    NO_ACCESS(403, "无权限"),
    ERROR(1, "错误"),

    // 用户认证自定义异常
    TOKEN_ERROR(40001, "token认证失败"),
    NOT_EXIST_USERNAME(40002, "用户名不存在"),
    ERROR_PASSWORD(40003, "密码错误"),
    NOT_EXIST_PHONE(40004, "手机号不存在"),
    ERROR_VCODE(40005, "验证码错误"),
    ACCOUNT_LOCKED(40006, "帐号已被锁定"),

    // Service层自定义异常
    SERVICE_SAVE_ERROR(60001, "保存失败"),
    SERVICE_UPDATE_ERROR(60002, "更新失败"),
    SERVICE_DELETE_ERROR(60003, "删除失败"),

    // 数据库抛出异常
    DATA_NOT_FOUNT_ERROR(50001, "无法查找到实体对象"),
    DATA_INTEGRITY_VIOLATION_ERROR(50002, "违反完整性约束");

    private final int code;
    private final String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
