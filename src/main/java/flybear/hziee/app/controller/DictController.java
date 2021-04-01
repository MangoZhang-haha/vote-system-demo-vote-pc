package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import flybear.hziee.app.anno.Log;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.entity.Dict;
import flybear.hziee.app.enums.LogModuleEnum;
import flybear.hziee.app.service.DictService;
import flybear.hziee.app.util.ResultUtil;
import flybear.hziee.app.util.WrapperUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 字典表控制器
 *
 * @author flybear
 * @since 2020/01/05 14:56
 */
@Log(module = LogModuleEnum.DICT_MANAGEMENT)
@RestController
@RequestMapping("dict")
public class DictController {

    @Resource
    private DictService dictService;

    /**
     * 分页查询
     *
     * @param page  页码
     * @param size  分页数量
     * @param dict  查询实体
     * @param sorts 排序
     * @return 分页数据
     */
    @Log("查询字典分页")
    @RequiresPermissions("sys:dict")
    @GetMapping
    public Result<IPage<Dict>> page(@RequestParam Integer page, @RequestParam Integer size, @ModelAttribute Dict dict, String sorts) {
        QueryWrapper<Dict> wrapper = Wrappers.query();
        WrapperUtil.like(wrapper, dict, new String[]{"code", "label"});
        return ResultUtil.success(this.dictService.page(new Page<>(page, size), wrapper, sorts));
    }

    /**
     * 新增
     *
     * @param dict 实例对象
     * @return 实例对象
     */
    @Log("新增字典")
    @RequiresPermissions("sys:dict:add")
    @PostMapping
    public Result<Dict> save(@Valid @ModelAttribute Dict dict) {
        this.dictService.save(dict);
        return ResultUtil.success(dict);
    }

    /**
     * 更新
     *
     * @param id   主键
     * @param dict 实例对象
     * @return 实例对象
     */
    @Log("更新字典")
    @RequiresPermissions("sys:dict:update")
    @PutMapping("{id}")
    public Result<Dict> update(@PathVariable("id") Long id, @Valid @ModelAttribute Dict dict) {
        dict.setId(id);
        this.dictService.updateById(dict);
        return ResultUtil.success(dict);
    }

    /**
     * 切换字典启用状态
     *
     * @param id     主键
     * @param active 是否启用
     * @return 是否成功
     */
    @Log("切换字典启用状态")
    @RequiresPermissions("sys:dict:update")
    @PutMapping("{id}/active")
    public Result updateActive(@PathVariable("id") Long id, @RequestParam Boolean active) {
        this.dictService.update(new LambdaUpdateWrapper<Dict>()
                .set(Dict::getActive, active)
                .eq(Dict::getId, id));
        return ResultUtil.success();
    }

    /**
     * 删除
     *
     * @param id 主键
     */
    @Log("删除字典")
    @RequiresPermissions("sys:dict:delete")
    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable("id") Long id) {
        this.dictService.removeById(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param idsStr 主键字符串列表
     */
    @Log("批量删除字典")
    @RequiresPermissions("sys:dict:delete")
    @DeleteMapping
    public Result deleteAllById(@RequestParam String idsStr) {
        this.dictService.removeByIds(Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toSet()));
        return ResultUtil.success();
    }

}
