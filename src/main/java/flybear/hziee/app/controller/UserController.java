package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.entity.dto.UserInfo;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.enums.PatternEnum;
import flybear.hziee.app.exception.NoAccessException;
import flybear.hziee.app.service.UserService;
import flybear.hziee.app.util.ResultUtil;
import flybear.hziee.app.util.ValidUtil;
import flybear.hziee.app.util.WrapperUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 用户表控制器
 *
 * @author flybear
 * @since 2019/12/20 0:54
 */
@Log(module = LogModuleEnum.USER_MANAGEMENT)
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  分页数量
     * @param user  查询实体
     * @param sorts 排序
     * @return 分页数据
     */
    @Log("查询用户分页")
    @RequiresPermissions("sys:user")
    @GetMapping
    public Result<IPage<User>> findAll(@RequestParam Integer page, @RequestParam Integer size, @ModelAttribute User user, String sorts) {
        // 用于从获取的用户列表中删除当前用户
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.ne("id", currentUser.getId());
        WrapperUtil.like(wrapper, user, new String[]{"username", "real_name", "phone"});
        IPage<User> iPage = this.userService.page(new Page<>(page, size), wrapper, sorts);
        this.userService.setRoleIds(iPage.getRecords());
        return ResultUtil.success(iPage);
    }

    /**
     * 新增
     *
     * @param user       实例对象
     * @param roleIdsStr 角色ID字符串
     * @return 实例对象
     */
    @Log("新增用户")
    @RequiresPermissions("sys:user:add")
    @PostMapping
    public Result<User> save(@Valid @ModelAttribute User user, String roleIdsStr) {
        ValidUtil.match(user.getPassword(), PatternEnum.PASSWORD);
        return ResultUtil.success(this.userService.save(user, roleIdsStr));
    }

    /**
     * 更新
     *
     * @param id         主键
     * @param user       实例对象
     * @param roleIdsStr 角色ID字符串
     * @return 实例对象
     */
    @Log("更新用户")
    @RequiresPermissions("sys:user:update")
    @PutMapping("{id}")
    public Result<User> update(@PathVariable("id") Long id, @ModelAttribute User user, String roleIdsStr) {
        this.checkIsNotCurrentUser(id);
        user.setId(id);
        if (user.getPassword() != null) {
            ValidUtil.match(user.getPassword(), PatternEnum.PASSWORD);
        }
        return ResultUtil.success(this.userService.update(user, roleIdsStr));
    }

    /**
     * 切换用户启用状态
     *
     * @param id     主键
     * @param active 是否启用
     * @return 是否成功
     */
    @Log("切换用户启用状态")
    @RequiresPermissions("sys:user:update")
    @PutMapping("{id}/active")
    public Result updateActive(@PathVariable("id") Long id, @RequestParam Boolean active) {
        this.checkIsNotCurrentUser(id);
        this.userService.update(new LambdaUpdateWrapper<User>()
                .set(User::getActive, active)
                .eq(User::getId, id));
        return ResultUtil.success();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @Log("删除用户")
    @RequiresPermissions("sys:user:delete")
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        this.checkIsNotCurrentUser(id);
        this.userService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param idsStr 主键字符串列表
     * @return 是否成功
     */
    @Log("批量删除用户")
    @RequiresPermissions("sys:user:delete")
    @DeleteMapping
    public Result deleteAllById(@RequestParam String idsStr) {
        this.userService.removeByIds(Arrays.stream(idsStr.split(",")).map(str -> {
            Long id = Long.valueOf(str);
            this.checkIsNotCurrentUser(id);
            return id;
        }).collect(Collectors.toSet()));
        return ResultUtil.success();
    }

    /**
     * 重置用户密码
     *
     * @param id       主键
     * @param password 密码
     * @return 是否成功
     */
    @Log("重置用户密码")
    @RequiresPermissions("sys:user:update")
    @PutMapping("{id}/password")
    public Result updatePassword(@PathVariable("id") Long id, String password) {
        ValidUtil.match(password, PatternEnum.PASSWORD);
        this.userService.updatePassword(id, password);
        return ResultUtil.success();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Log("获取用户信息")
    @RequiresAuthentication
    @PostMapping("info")
    public Result<UserInfo> findInfo() {
        Subject subject = SecurityUtils.getSubject();
        return ResultUtil.success(new UserInfo((User) subject.getPrincipal()));
    }

    /**
     * 保证当前用户不能修改自己的信息（部分操作可以除外）
     *
     * @param id 被操作对象的用户主键
     */
    private void checkIsNotCurrentUser(Long id) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        // 用户不能在用户管理中修改自己的信息
        if (currentUser.getId().equals(id)) {
            throw new NoAccessException();
        }
    }

    /**
     * 修改本人信息
     *
     * @param realName 姓名
     * @param phone    手机号
     * @return 是否成功
     */
    @Log("修改本人信息")
    @RequiresAuthentication
    @PutMapping("basic")
    public Result updateBasicInfo(String realName, String phone) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        ValidUtil.match(realName, PatternEnum.REAL_NAME);
        ValidUtil.match(phone, PatternEnum.PHONE);
        this.userService.update(new LambdaUpdateWrapper<User>()
                .set(User::getRealName, realName)
                .set(User::getPhone, phone)
                .eq(User::getId, currentUser.getId()));
        return ResultUtil.success();
    }

    /**
     * 修改密码
     *
     * @param password 新密码
     * @return 是否成功
     */
    @Log("修改密码")
    @RequiresAuthentication
    @PutMapping("password")
    public Result updatePassword(String password) {
        User currentUser = (User) SecurityUtils.getSubject().getPrincipal();
        ValidUtil.match(password, PatternEnum.PASSWORD);
        this.userService.updatePassword(currentUser.getId(), password);
        return ResultUtil.success();
    }
}
