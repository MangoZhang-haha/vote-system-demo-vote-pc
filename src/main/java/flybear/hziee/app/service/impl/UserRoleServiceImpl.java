package flybear.hziee.app.service.impl;

import flybear.hziee.app.entity.UserRole;
import flybear.hziee.app.mapper.UserRoleMapper;
import flybear.hziee.app.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联表服务实现类
 *
 * @author flybear
 * @since 2020/1/7 16:49
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
