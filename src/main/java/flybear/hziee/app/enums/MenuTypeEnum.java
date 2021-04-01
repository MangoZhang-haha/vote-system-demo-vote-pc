package flybear.hziee.app.enums;

import lombok.Getter;

/**
 * 菜单类型枚举类
 *
 * @author qiancj
 * @since 2020/1/8 13:35
 */
@Getter
public enum MenuTypeEnum {
    DIR(0, "目录"),
    PAGE(1, "页面"),
    BTN(2, "按钮");

    private final int type;
    private final String name;

    MenuTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }
}
