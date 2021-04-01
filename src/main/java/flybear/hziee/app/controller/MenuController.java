package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.Menu;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.service.MenuService;
import flybear.hziee.app.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单表控制器
 *
 * @author flybear
 * @since 2019/12/22 00:23
 */
@Log(module = LogModuleEnum.MENU_MANAGEMENT)
@RestController
@RequestMapping("menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 获取所有菜单信息
     *
     * @return 所有菜单信息
     */
    @Log("获取所有菜单信息")
    @RequiresPermissions("sys:menu")
    @GetMapping
    public Result<List<Menu>> findAll() {
        return ResultUtil.success(this.menuService.list());
    }

    /**
     * 新增
     *
     * @param menu 实例对象
     * @return 实例对象
     */
    @Log("新增菜单")
    @RequiresPermissions("sys:menu:add")
    @PostMapping
    public Result<Menu> save(@Valid @ModelAttribute Menu menu) {
        if (menu.getPid() == null) {
            menu.setPid(0L);
        }
        this.menuService.save(menu);
        return ResultUtil.success(menu);
    }

    /**
     * 更新
     *
     * @param id   主键
     * @param menu 实例对象
     * @return 实例对象
     */
    @Log("更新菜单")
    @RequiresPermissions("sys:menu:update")
    @PutMapping("{id}")
    public Result<Menu> update(@PathVariable("id") Long id, @Valid @ModelAttribute Menu menu) {
        menu.setId(id);
        if (menu.getPid() == null) {
            menu.setPid(0L);
        }
        this.menuService.updateById(menu);
        return ResultUtil.success(menu);
    }

    /**
     * 切换菜单启用状态
     *
     * @param id     主键
     * @param active 是否启用
     * @return 是否成功
     */
    @Log("切换菜单启用状态")
    @RequiresPermissions("sys:menu:update")
    @PutMapping("{id}/active")
    public Result updateActive(@PathVariable("id") Long id, @RequestParam Boolean active) {
        this.menuService.update(new LambdaUpdateWrapper<Menu>()
                .set(Menu::getActive, active)
                .eq(Menu::getId, id));
        return ResultUtil.success();
    }

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @Log("删除菜单")
    @RequiresPermissions("sys:menu:delete")
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        this.menuService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param idsStr 主键字符串列表
     * @return 是否成功
     */
    @Log("批量删除菜单")
    @RequiresPermissions("sys:menu:delete")
    @DeleteMapping
    public Result deleteAllById(@RequestParam String idsStr) {
        this.menuService.removeByIds(Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        return ResultUtil.success();
    }

    /**
     * 获取菜单树数据（此接口只返回list，需要前端处理为树结构）
     *
     * @return 菜单列表
     */
    @Log("获取菜单树数据")
    @GetMapping("tree")
    public Result menuTree() {
        return ResultUtil.success(this.menuService.list(Wrappers.<Menu>lambdaQuery().select(
                Menu::getId, Menu::getName, Menu::getPid, Menu::getType
        )));
    }


}
