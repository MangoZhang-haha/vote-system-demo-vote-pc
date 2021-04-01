package flybear.hziee.app.entity.dto;

import flybear.hziee.app.entity.Menu;
import flybear.hziee.app.entity.Role;
import flybear.hziee.app.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户信息
 *
 * @author flybear
 * @since 2019-12-24 03:12
 */
@Data
@NoArgsConstructor
public class UserInfo implements Serializable {
    private Long id;
    private String username;
    private String realName;
    private String avatar;
    private String phone;
    private Boolean active;
    private Set<Role> roles;
    private Set<Menu> menus;

    public UserInfo(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.realName = user.getRealName();
        this.avatar = user.getAvatar();
        this.phone = user.getPhone();
        this.active = user.getActive();
        this.roles = user.getRoles();
        this.menus = user.getMenus();
    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setRealName(this.realName);
        user.setAvatar(this.avatar);
        user.setActive(this.active);
        user.setPhone(this.phone);
        user.setRoles(this.roles);
        user.setMenus(this.menus);
        return user;
    }
}
