package flybear.hziee.app.service.impl;

import flybear.hziee.app.entity.RoleMenu;
import flybear.hziee.app.mapper.RoleMenuMapper;
import flybear.hziee.app.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色菜单关联表服务实现类
 *
 * @author flybear
 * @since 2020/1/7 21:09
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
