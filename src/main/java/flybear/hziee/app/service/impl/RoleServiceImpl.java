package flybear.hziee.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.entity.Role;
import flybear.hziee.app.entity.RoleMenu;
import flybear.hziee.app.entity.UserRole;
import flybear.hziee.app.mapper.RoleMapper;
import flybear.hziee.app.service.RoleMenuService;
import flybear.hziee.app.service.RoleService;
import flybear.hziee.app.service.UserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色表服务实现类
 *
 * @author flybear
 * @since 2019/12/22 00:23
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserRoleService userRoleService;

    @Override
    public List<Long> findMenus(Long id) {
        return this.roleMenuService.list(
                Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id)
        ).stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRoleMenus(Long id, String menuIdsStr) {
        this.roleMenuService.remove(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, id));
        if (StringUtils.isNotBlank(menuIdsStr)) {
            String[] menuIds = menuIdsStr.split(",");
            Set<RoleMenu> rmSet = new HashSet<>(menuIds.length);
            for (String menuId : menuIds) {
                rmSet.add(new RoleMenu(id, Long.valueOf(menuId)));
            }
            this.roleMenuService.saveBatch(rmSet, rmSet.size());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        this.userRoleService.remove(Wrappers.<UserRole>lambdaUpdate().eq(UserRole::getRoleId, id));
        this.roleMenuService.remove(Wrappers.<RoleMenu>lambdaUpdate().eq(RoleMenu::getRoleId, id));
        return super.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        this.userRoleService.remove(Wrappers.<UserRole>lambdaUpdate().in(UserRole::getRoleId, idList));
        this.roleMenuService.remove(Wrappers.<RoleMenu>lambdaUpdate().in(RoleMenu::getRoleId, idList));
        return super.removeByIds(idList);
    }
}
