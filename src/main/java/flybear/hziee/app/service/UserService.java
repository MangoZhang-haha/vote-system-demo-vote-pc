package flybear.hziee.app.service;

import flybear.hziee.app.entity.User;
import flybear.hziee.app.entity.dto.UserInfo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户表服务接口
 *
 * @author flybear
 * @since 2019/12/20 21:37
 */
public interface UserService extends BaseService<User> {

    /**
     * 新增
     *
     * @param user       实例对象
     * @param roleIdsStr 角色ID字符串
     * @return 实例对象
     */
    User save(User user, String roleIdsStr);

    /**
     * 更新
     *
     * @param user       实例对象
     * @param roleIdsStr 角色ID字符串
     * @return 实例对象
     */
    User update(User user, String roleIdsStr);

    /**
     * 重置用户密码
     *
     * @param id       主键
     * @param password 密码
     */
    void updatePassword(Long id, String password);

    /**
     * 获取RoleIds
     *
     * @param list 用户列表
     * @return 用户列表
     */
    List<User> setRoleIds(List<User> list);

    /**
     * 获取用户信息
     *
     * @param id 主键
     * @return 用户信息
     */
    UserInfo getUserInfo(Long id);
}
