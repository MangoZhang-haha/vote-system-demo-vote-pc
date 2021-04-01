package flybear.hziee.app.entity;

import lombok.*;

/**
 * 用户角色关联表
 *
 * @author flybear
 * @since 2020/1/6 22:09
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserRole {
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 角色主键
     */
    private Long roleId;
}
