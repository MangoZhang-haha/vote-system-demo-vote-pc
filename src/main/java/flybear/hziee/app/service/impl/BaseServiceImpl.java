package flybear.hziee.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.enums.ResultEnum;
import flybear.hziee.app.exception.CustomException;
import flybear.hziee.app.exception.DataNotFoundException;
import flybear.hziee.app.service.BaseService;
import flybear.hziee.app.util.WrapperUtil;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * 服务基础实现类
 *
 * @author flybear
 * @since 2020/1/7 13:37
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    @Override
    public <E extends IPage<T>> E page(E page, String sorts) {
        return page(page, Wrappers.<T>query(), sorts);
    }

    @Override
    public <E extends IPage<T>> E page(E page, QueryWrapper<T> wrapper, String sorts) {
        if (!StringUtils.isEmpty(sorts)) {
            WrapperUtil.orderBy(wrapper, sorts);
        }
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean save(T entity) {
        return check(baseMapper.insert(entity), ResultEnum.SERVICE_SAVE_ERROR);
    }

    @Override
    public boolean removeById(Serializable id) {
        return check(baseMapper.deleteById(id), ResultEnum.SERVICE_DELETE_ERROR);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return check(baseMapper.deleteBatchIds(idList), ResultEnum.SERVICE_DELETE_ERROR);
    }

    @Override
    public boolean updateById(T entity) {
        return check(baseMapper.updateById(entity), ResultEnum.SERVICE_UPDATE_ERROR);
    }

    @Override
    public T getById(Serializable id) {
        return Optional.ofNullable(baseMapper.selectById(id)).orElseThrow(DataNotFoundException::new);
    }

    private boolean check(int i, ResultEnum resultEnum) {
        if (!retBool(i)) {
            throw new CustomException(resultEnum);
        }
        return true;
    }

}
