package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.entity.Role;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.service.RoleService;
import flybear.hziee.app.util.ResultUtil;
import flybear.hziee.app.util.WrapperUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色表控制器
 *
 * @author flybear
 * @since 2019/12/22 00:23
 */
@Log(module = LogModuleEnum.ROLE_MANAGEMENT)
@RestController
@RequestMapping("role")
public class RoleController {

    @Resource
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  分页数量
     * @param role  查询实体
     * @param sorts 排序
     * @return 分页数据
     */
    @Log("查询角色分页")
    @RequiresPermissions("sys:role")
    @GetMapping
    public Result<IPage<Role>> findAll(@RequestParam Integer page, @RequestParam Integer size, @ModelAttribute Role role, String sorts) {
        QueryWrapper<Role> wrapper = Wrappers.query();
        WrapperUtil.like(wrapper, role, new String[]{"name", "role"});
        IPage<Role> iPage = this.roleService.page(new Page<>(page, size), wrapper, sorts);
        return ResultUtil.success(iPage);
    }

    /**
     * 新增
     *
     * @param role 实例对象
     * @return 实例对象
     */
    @Log("新增角色")
    @RequiresPermissions("sys:role:add")
    @PostMapping
    public Result<Role> save(@Valid @ModelAttribute Role role) {
        this.roleService.save(role);
        return ResultUtil.success(role);
    }

    /**
     * 更新
     *
     * @param id   主键
     * @param role 实例对象
     * @return 实例对象
     */
    @Log("更新角色")
    @RequiresPermissions("sys:role:update")
    @PutMapping("{id}")
    public Result<Role> update(@PathVariable("id") Long id, @Valid @ModelAttribute Role role) {
        role.setId(id);
        this.roleService.updateById(role);
        return ResultUtil.success(role);
    }

    /**
     * 切换角色启用状态
     *
     * @param id     主键
     * @param active 是否启用
     * @return 是否成功
     */
    @Log("切换角色启用状态")
    @RequiresPermissions("sys:role:update")
    @PutMapping("{id}/active")
    public Result updateActive(@PathVariable("id") Long id, @RequestParam Boolean active) {
        this.roleService.update(new LambdaUpdateWrapper<Role>()
                .set(Role::getActive, active)
                .eq(Role::getId, id));
        return ResultUtil.success();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @Log("删除角色")
    @RequiresPermissions("sys:role:delete")
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        this.roleService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param idsStr 主键字符串列表
     * @return 是否成功
     */
    @Log("批量删除角色")
    @RequiresPermissions("sys:role:delete")
    @DeleteMapping
    public Result deleteAllById(@RequestParam String idsStr) {
        this.roleService.removeByIds(Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        return ResultUtil.success();
    }

    /**
     * 获取角色下拉列表选择框数据
     *
     * @return 角色列表
     */
    @Log("获取角色下拉列表选择框数据")
    @GetMapping("options")
    public Result roleOptions() {
        return ResultUtil.success(this.roleService.list(Wrappers.<Role>lambdaQuery().select(
                Role::getId, Role::getName
        )));
    }

    /**
     * 获取角色拥有的菜单ID列表
     *
     * @param id 角色ID
     * @return 菜单ID列表
     */
    @Log("获取角色拥有的菜单ID列表")
    @RequiresPermissions("sys:menu:perms")
    @GetMapping("{id}/menus")
    public Result<List<Long>> roleMenus(@PathVariable("id") Long id) {
        return ResultUtil.success(this.roleService.findMenus(id));
    }

    /**
     * 更新角色权限
     *
     * @param id         角色ID
     * @param menuIdsStr 菜单ID字符串列表
     * @return 是否成功
     */
    @Log("更新角色权限")
    @RequiresPermissions("sys:menu:perms")
    @PostMapping("{id}/menus")
    public Result updateRoleMenus(@PathVariable("id") Long id, String menuIdsStr) {
        this.roleService.updateRoleMenus(id, menuIdsStr);
        return ResultUtil.success();
    }

}
