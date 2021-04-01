package flybear.hziee.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.entity.Menu;
import flybear.hziee.app.entity.RoleMenu;
import flybear.hziee.app.mapper.MenuMapper;
import flybear.hziee.app.service.MenuService;
import flybear.hziee.app.service.RoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;

/**
 * 菜单表服务实现类
 *
 * @author flybear
 * @since 2019/12/22 00:23
 */
@Service("menuService")
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private RoleMenuService roleMenuService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeById(Serializable id) {
        this.roleMenuService.remove(Wrappers.<RoleMenu>lambdaUpdate().eq(RoleMenu::getMenuId, id));
        return super.removeById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        this.roleMenuService.remove(Wrappers.<RoleMenu>lambdaUpdate().in(RoleMenu::getMenuId, idList));
        return super.removeByIds(idList);
    }
}
