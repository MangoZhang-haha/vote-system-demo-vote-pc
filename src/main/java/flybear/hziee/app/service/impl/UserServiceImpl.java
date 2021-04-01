package flybear.hziee.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.entity.Role;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.entity.UserRole;
import flybear.hziee.app.entity.dto.UserInfo;
import flybear.hziee.app.enums.ConstraintEnum;
import flybear.hziee.app.exception.ConstraintException;
import flybear.hziee.app.exception.DataNotFoundException;
import flybear.hziee.app.mapper.UserMapper;
import flybear.hziee.app.mapper.UserRoleMapper;
import flybear.hziee.app.service.UserRoleService;
import flybear.hziee.app.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户表服务实现类
 *
 * @author flybear
 * @since 2019/12/20 21:38
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private UserRoleService userRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User save(User user, String roleIdsStr) {
        user.setSalty(String.valueOf((int) ((Math.random() * 9 + 1) * 100000)));
        user.setPassword(generatePassword(user.getPassword(), user.getSalty()));

        this.checkUnique(user);
        super.save(user);
        this.updateRoles(user.getId(), roleIdsStr);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User update(User user, String roleIdsStr) {
        this.checkUnique(user);
        this.updateRoles(user.getId(), roleIdsStr);
        super.updateById(user);
        return user;
    }

    /**
     * 判断用户名和手机号唯一性
     *
     * @param user 用户
     */
    private void checkUnique(User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();

        if (user.getId() != null) {
            wrapper.ne(User::getId, user.getId());
        }

        wrapper.and(wrapper1 -> {
            if (StringUtils.isNotBlank(user.getPhone())) {
                wrapper1.eq(User::getPhone, user.getPhone())
                        .or();
            }
            wrapper1.eq(User::getUsername, user.getUsername());
        }).last("limit 1");

        User existUser = this.userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new ConstraintException(
                    user.getUsername().equals(existUser.getUsername()) || StringUtils.isBlank(user.getPhone())
                            ? ConstraintEnum.UK_X_USER_USERNAME
                            : ConstraintEnum.UK_X_USER_PHONE);
        }
    }

    @Override
    public void updatePassword(Long id, String password) {
        super.update(new LambdaUpdateWrapper<User>()
                .set(User::getPassword, generatePassword(password, getSaltyById(id)))
                .eq(User::getId, id));
    }

    /**
     * 设置角色列表，用于保存和更新时
     *
     * @param id         主键
     * @param roleIdsStr 角色ID字符串数组
     */
    private void updateRoles(Long id, String roleIdsStr) {
        this.userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, id));
        if (StringUtils.isNotBlank(roleIdsStr)) {
            String[] roleIds = roleIdsStr.split(",");
            Set<UserRole> urSet = new HashSet<>(roleIds.length);
            for (String roleId : roleIds) {
                urSet.add(new UserRole(id, Long.valueOf(roleId)));
            }
            this.userRoleService.saveBatch(urSet, urSet.size());
        }
    }

    /**
     * 生成加密密码
     *
     * @param password 主键
     * @param salty    盐值
     * @return 加密密码
     */
    private String generatePassword(String password, String salty) {
        return new Md5Hash(new Md5Hash(password, salty), salty).toString();
    }

    /**
     * 获取盐值
     *
     * @param id 主键
     * @return 盐值
     */
    private String getSaltyById(Long id) {
        return Optional.ofNullable(this.userMapper.selectOne(
                Wrappers.<User>lambdaQuery().select(User::getSalty).eq(User::getId, id)
        )).orElseThrow(DataNotFoundException::new).getSalty();
    }

    @Override
    public List<User> setRoleIds(List<User> users) {
        List<Long> ids = users.stream().map(User::getId).collect(Collectors.toList());
        if (ids.isEmpty()) {
            return users;
        }
        List<UserRole> urs = this.userRoleMapper.selectList(
                Wrappers.<UserRole>lambdaQuery().in(UserRole::getUserId, ids)
        );

        // 用 Map 存储 用户ID -> 角色ID集合
        Map<Long, Set<Long>> userIdRoleIdsMap = new HashMap<>(users.size());
        for (UserRole ur : urs) {
            Set<Long> roleIdList = userIdRoleIdsMap.get(ur.getUserId());
            if (roleIdList == null) {
                userIdRoleIdsMap.put(ur.getUserId(), new HashSet<>(Collections.singleton(ur.getRoleId())));
            } else {
                roleIdList.add(ur.getRoleId());
            }
        }

        for (User user : users) {
            Set<Long> roleIds = userIdRoleIdsMap.get(user.getId());
            if (roleIds != null) {
                user.setRoles(new HashSet<>());
                for (Long roleId : roleIds) {
                    Role role = new Role();
                    role.setId(roleId);
                    user.getRoles().add(role);
                }
            }
        }
        return users;
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        return this.userMapper.getUserInfo(id);
    }

}
