package flybear.hziee.app.entity;

import lombok.*;

/**
 * 角色菜单关联表
 *
 * @author flybear
 * @since 2020/1/7 21:07
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RoleMenu {
    /**
     * 角色主键
     */
    private Long roleId;
    /**
     * 菜单主键
     */
    private Long menuId;
}
