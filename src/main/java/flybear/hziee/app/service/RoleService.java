package flybear.hziee.app.service;

import flybear.hziee.app.entity.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色表服务接口
 *
 * @author flybear
 * @since 2019/12/22 00:23
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 获取角色拥有的菜单列表
     *
     * @param id 主键
     * @return 菜单列表
     */
    List<Long> findMenus(Long id);

    /**
     * 获取角色权限
     *
     * @param id         角色ID
     * @param menuIdsStr 菜单ID字符串列表
     */
    void updateRoleMenus(Long id, String menuIdsStr);
}
