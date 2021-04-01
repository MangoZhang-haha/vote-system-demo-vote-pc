package flybear.hziee.app.enums;

import lombok.Getter;

/**
 * 约束枚举类，用于捕获约束异常时返回提示信息
 * 枚举项名称为数据库中的约束名
 *
 * @author flybear
 * @since 2020/1/1 19:25
 */
@Getter
public enum ConstraintEnum {

    // User
    UK_X_USER_USERNAME("已存在该用户名"),
    UK_X_USER_PHONE("已存在该手机号"),

    // Role
    UK_X_ROLE_NAME("已存在该角色名"),
    UK_X_ROLE_ROLE("已存在该角色标识"),

    // Dict
    UK_X_DICT_CODE_VALUE("字典标识和值必须唯一"),
    UK_X_DICT_CODE_LABEL("字典标识和标签必须唯一");

    private final String msg;

    ConstraintEnum(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return ResultEnum.DATA_INTEGRITY_VIOLATION_ERROR.getCode();
    }
}
