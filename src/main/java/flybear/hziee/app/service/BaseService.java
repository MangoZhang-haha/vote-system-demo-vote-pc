package flybear.hziee.app.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
 * 服务基础接口
 *
 * @author flybear
 * @since 2020/1/7 13:16
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 翻页查询
     *
     * @param page  翻页对象
     * @param sorts 排序字符串
     * @return 分页数据
     */
    <E extends IPage<T>> E page(E page, String sorts);

    /**
     * 翻页查询
     *
     * @param page    翻页对象
     * @param wrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param sorts   排序字符串
     * @return 分页数据
     */
    <E extends IPage<T>> E page(E page, QueryWrapper<T> wrapper, String sorts);

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     * @return 查询实体
     */
    @Override
    T getById(Serializable id);

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     * @return 是否成功
     */
    @Override
    boolean updateById(T entity);

    /**
     * 删除
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @return 是否成功
     */
    @Override
    boolean removeByIds(Collection<? extends Serializable> idList);
}
