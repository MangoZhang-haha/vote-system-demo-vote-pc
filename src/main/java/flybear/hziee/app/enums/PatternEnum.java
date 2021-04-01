package flybear.hziee.app.enums;

import flybear.hziee.app.consts.PatternConst;
import lombok.Getter;

/**
 * 正则枚举类
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@Getter
public enum PatternEnum {
    PASSWORD(PatternConst.PASSWORD, "密码格式不正确，应该由6~14位数字或大小写字母组成"),
    PHONE(PatternConst.PHONE, "手机号码不正确"),
    REAL_NAME(PatternConst.REAL_NAME, "请输入正确的中文姓名"),
    VCODE(PatternConst.VCODE, "验证码格式不正确，应该由4位数字组成"),
    ROLE(PatternConst.ROLE, "角色标识只能由英文和数字组成"),
    PERMISSION(PatternConst.PERMISSION, "权限标识只能由英文、数字和冒号组成");

    private final String regEx;
    private final String msg;

    PatternEnum(String regEx, String msg) {
        this.regEx = regEx;
        this.msg = msg;
    }
}
