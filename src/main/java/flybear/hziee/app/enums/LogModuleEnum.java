package flybear.hziee.app.enums;

import lombok.Getter;

/**
 * 日志记录模块枚举类
 *
 * @author qiancj
 * @since 2020/1/8 16:22
 */
@Getter
public enum LogModuleEnum {
    DEFALUT(0, null),
    LOGIN(1, "登录"),
    LOGOUT(2, "登出"),
    USER_MANAGEMENT(3, "用户管理"),
    ROLE_MANAGEMENT(4, "角色管理"),
    MENU_MANAGEMENT(5, "菜单管理"),
    DICT_MANAGEMENT(6, "字典管理");

    private final int code;
    private final String name;

    LogModuleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
